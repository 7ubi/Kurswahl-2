import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {SubjectAreaResponses} from "../../../admin.responses";

@Component({
  selector: 'app-create-subject',
  templateUrl: './create-subject.component.html',
  styleUrls: ['./create-subject.component.css']
})
export class CreateSubjectComponent implements OnInit{
  createSubjectForm: FormGroup;
  subjectAreaResponses?: SubjectAreaResponses;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createSubjectForm = this.formBuilder.group({
      name: ['', Validators.required],
      subjectArea: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectAreaResponses>('/api/admin/subjectAreas', response => {
      response.subjectAreaResponses.sort((a, b) =>  a.name.localeCompare(b.name));
      this.subjectAreaResponses = response;
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
