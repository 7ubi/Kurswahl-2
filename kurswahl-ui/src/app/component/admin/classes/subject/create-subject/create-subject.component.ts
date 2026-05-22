import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {SubjectAreaResponse} from "../../../admin.responses";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-subject',
  templateUrl: './create-subject.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatSelect,
    MatOption,
    MatButton
  ],
  styleUrls: ['./create-subject.component.css']
})
export class CreateSubjectComponent implements OnInit {
  createSubjectForm: FormGroup;
  subjectAreaResponses?: SubjectAreaResponse[];

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.createSubjectForm = this.formBuilder.group({
      name: ['', Validators.required],
      subjectArea: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectAreaResponse[]>('/api/admin/subjectAreas', response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectAreaResponses = response;
      this.cdr.detectChanges();
    });
  }

  createSubject() {
    if (!this.createSubjectForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/subject', this.getCreateSubjectRequest(), response => {
      this.router.navigate(['admin', 'subjects']);
    });
  }

  private getCreateSubjectRequest() {
    return {
      name: this.createSubjectForm.get('name')?.value,
      subjectAreaId: this.createSubjectForm.get('subjectArea')?.value
    }
  }
}
