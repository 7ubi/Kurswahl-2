import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SubjectResponse, TapeResponse, TeacherResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-create-class',
  templateUrl: './create-class.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
    MatInput,
    MatButton
  ],
  styleUrls: ['./create-class.component.css']
})
export class CreateClassComponent implements OnInit, OnDestroy {
  createClassForm: FormGroup;
  subjectResponses?: SubjectResponse[];
  teacherResponses?: TeacherResponse[];
  tapeResponses!: TapeResponse[];
  private subjectChangeSub?: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.createClassForm = this.formBuilder.group({
      name: ['', Validators.required],
      subject: ['', Validators.required],
      teacher: ['', Validators.required],
      tape: ['', Validators.required],
      year: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectResponse[]>('/api/admin/subjects', response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectResponses = response;
      this.cdr.detectChanges();
    });

    this.httpService.get<TeacherResponse[]>('/api/admin/teachers', response => {
      response.sort((a, b) =>
        a.abbreviation.localeCompare(b.abbreviation));
      this.teacherResponses = response;
      this.cdr.detectChanges();
    });

    // When a subject is selected, if the name field is empty, set it to the subject's name
    const subjectControl = this.createClassForm.get('subject');
    const nameControl = this.createClassForm.get('name');
    if (subjectControl && nameControl) {
      this.subjectChangeSub = subjectControl.valueChanges.subscribe(selectedId => {
        const currentName = (nameControl.value || '').toString();
        if (currentName.trim().length > 0) {
          return; // don't overwrite a user-provided name
        }

        const subject = this.subjectResponses?.find(s => s.subjectId == selectedId);
        if (subject) {
          nameControl.setValue(subject.name);
          // OnPush change detection: mark for check
          this.cdr.detectChanges();
        }
      });
    }
  }

  ngOnDestroy(): void {
    if (this.subjectChangeSub) {
      this.subjectChangeSub.unsubscribe();
    }
  }

  createClass() {
    if (!this.createClassForm.valid || !this.isTapeFormFieldActive()) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/class', this.getCreateClassRequest(), () => {
      this.router.navigate(['admin', 'classes']);
    });
  }

  private getCreateClassRequest() {
    return {
      name: this.createClassForm.get('name')?.value,
      subjectId: this.createClassForm.get('subject')?.value,
      teacherId: this.createClassForm.get('teacher')?.value,
      tapeId: this.createClassForm.get('tape')?.value
    }
  }

  loadTapes(event: KeyboardEvent) {
    const year = Number((event?.target as HTMLInputElement).value);

    this.httpService.get<TapeResponse[]>(`/api/admin/tapes?year=${year}`, response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.tapeResponses = response;
      this.cdr.detectChanges();
    });
  }

  isTapeFormFieldActive(): boolean {
    return this.tapeResponses && this.tapeResponses.length > 0;
  }
}
