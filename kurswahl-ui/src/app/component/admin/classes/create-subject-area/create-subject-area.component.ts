import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";
import {ResultResponse} from "../../../../app.responses";

@Component({
  selector: 'app-create-subject-area',
  templateUrl: './create-subject-area.component.html',
  styleUrls: ['./create-subject-area.component.css']
})
export class CreateSubjectAreaComponent {
  createSubjectAreaForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createSubjectAreaForm = this.formBuilder.group({
      name: ['', Validators.required],
    })
  }

  createSubjectArea() {
    this.httpService.post<ResultResponse>('/api/admin/subjectArea', this.getCreateAdminRequest(), response => {
      this.router.navigate(['admin', 'subjectAreas']);
    });
  }

  private getCreateAdminRequest() {
    return {
      name: this.createSubjectAreaForm.get('name')?.value,
    }
  }
}
