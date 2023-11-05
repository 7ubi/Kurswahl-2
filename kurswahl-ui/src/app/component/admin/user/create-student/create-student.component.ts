import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";
import {ResultResponse} from "../../../../app.responses";

@Component({
  selector: 'app-create-student',
  templateUrl: './create-student.component.html',
  styleUrls: ['./create-student.component.css']
})
export class CreateStudentComponent {
  createStudentForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createStudentForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
    });
  }

  createStudent() {
    this.httpService.post<ResultResponse>('/api/admin/student', this.getCreateStudentRequest(), response => {
      this.router.navigate(['admin', 'students']);
    });
  }

  private getCreateStudentRequest() {
    return {
      firstname: this.createStudentForm.get('firstname')?.value,
      surname: this.createStudentForm.get('surname')?.value,
    }
  }
}
