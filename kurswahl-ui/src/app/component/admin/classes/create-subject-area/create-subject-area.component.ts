import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";

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
    if (!this.createSubjectAreaForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/subjectArea', this.getCreateSubjectAreaRequest(), response => {
      this.router.navigate(['admin', 'subjectAreas']);
    });
  }

  private getCreateSubjectAreaRequest() {
    return {
      name: this.createSubjectAreaForm.get('name')?.value,
    }
  }
}
