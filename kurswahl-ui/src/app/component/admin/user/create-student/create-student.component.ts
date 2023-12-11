import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";
import {StudentClassResponses} from "../../../../app.responses";

@Component({
  selector: 'app-create-student',
  templateUrl: './create-student.component.html',
  styleUrls: ['./create-student.component.css']
})
export class CreateStudentComponent implements OnInit {
  createStudentForm: FormGroup;
  studentClasses?: StudentClassResponses;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createStudentForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
      studentClass: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<StudentClassResponses>('/api/admin/studentClasses', response => {
      this.studentClasses = response;
    });
  }

  createStudent() {
    if (!this.createStudentForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/student', this.getCreateStudentRequest(), response => {
      this.router.navigate(['admin', 'students']);
    });
  }

  private getCreateStudentRequest() {
    return {
      firstname: this.createStudentForm.get('firstname')?.value,
      surname: this.createStudentForm.get('surname')?.value,
      studentClassId: this.createStudentForm.get('studentClass')?.value,
    }
  }
}
