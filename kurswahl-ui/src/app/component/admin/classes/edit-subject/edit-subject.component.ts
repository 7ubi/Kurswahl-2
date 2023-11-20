import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ResultResponse, SubjectAreaResponses, SubjectResultResponse} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-subject',
  templateUrl: './edit-subject.component.html',
  styleUrls: ['./edit-subject.component.css']
})
export class EditSubjectComponent {
  editSubjectForm: FormGroup;
  subjectAreaResponses?: SubjectAreaResponses;
  id: string | null;
  subject?: SubjectResultResponse

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editSubjectForm = this.formBuilder.group({
      name: ['', Validators.required],
      subjectArea: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectResultResponse>(`/api/admin/subject?subjectId=${this.id}`, response => {
      this.subject = response;
      this.editSubjectForm.controls['name'].setValue(this.subject.subjectResponse.name);
      this.editSubjectForm.controls['subjectArea']
        .setValue(this.subject.subjectResponse.subjectAreaResponse.subjectAreaId);
    }, () => this.router.navigate(['admin', 'subjects']));

    this.httpService.get<SubjectAreaResponses>('/api/admin/subjectAreas', response => {
      response.subjectAreaResponses.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectAreaResponses = response;
    });
  }

  editSubject() {
    if (!this.editSubjectForm.valid) {
      return;
    }

    this.httpService.put<ResultResponse>(`/api/admin/subject?subjectId=${this.id}`, this.getCreateSubjectRequest(),
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
