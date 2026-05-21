import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SubjectAreaResponse, SubjectResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-edit-subject',
  templateUrl: './edit-subject.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    MatFormField,
    MatLabel,
    MatInput,
    MatSelect,
    ReactiveFormsModule,
    MatButton,
    MatProgressSpinner,
    MatOption
  ],
  styleUrls: ['./edit-subject.component.css']
})
export class EditSubjectComponent implements OnInit {
  editSubjectForm: FormGroup;
  subjectAreaResponses?: SubjectAreaResponse[];
  id: string | null;
  subject?: SubjectResponse;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editSubjectForm = this.formBuilder.group({
      name: ['', Validators.required],
      subjectArea: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectResponse>(`/api/admin/subject?subjectId=${this.id}`, response => {
      this.subject = response;
      this.editSubjectForm.controls['name'].setValue(this.subject.name);
      this.editSubjectForm.controls['subjectArea']
        .setValue(this.subject.subjectAreaResponse.subjectAreaId);
      this.cdr.detectChanges();
    }, () => this.router.navigate(['admin', 'subjects']));

    this.httpService.get<SubjectAreaResponse[]>('/api/admin/subjectAreas', response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectAreaResponses = response;
      this.cdr.detectChanges();
    });
  }

  editSubject() {
    if (!this.editSubjectForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/subject?subjectId=${this.id}`, this.getCreateSubjectRequest(),
      response => this.router.navigate(['admin', 'subjects']));
  }

  private getCreateSubjectRequest() {
    return {
      name: this.editSubjectForm.get('name')?.value,
      subjectAreaId: this.editSubjectForm.get('subjectArea')?.value
    }
  }


  compareSubjectAreaObjects(object1: number, object2: number) {
    return object1 == object2;
  }
}
