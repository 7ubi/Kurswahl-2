import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StudentClassResponses, StudentResponse} from "../../admin.responses";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-student',
  templateUrl: './edit-student.component.html',
  styleUrls: ['./edit-student.component.css']
})
export class EditStudentComponent implements OnInit {
  editStudentForm: FormGroup;
  studentClasses?: StudentClassResponses;
  student?: StudentResponse;
  id: string | null;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editStudentForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
      studentClass: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<StudentClassResponses>('/api/admin/studentClasses', response => {
      this.studentClasses = response;
    });

    this.httpService.get<StudentResponse>(`/api/admin/student?studentId=${this.id}`, response => {
      this.student = response;
      this.editStudentForm.controls['firstname'].setValue(this.student.firstname);
      this.editStudentForm.controls['surname'].setValue(this.student.surname);
      this.editStudentForm.controls['studentClass']
        .setValue(this.student.studentClassResponse.studentClassId);
    }, () => this.router.navigate(['admin', 'students']));
  }

  editStudent() {
    if (!this.editStudentForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/student?studentId=${this.id}`, this.getCreateStudentRequest(), response => {
      this.router.navigate(['admin', 'students']);
    });
  }

  private getCreateStudentRequest() {
    return {
      firstname: this.editStudentForm.get('firstname')?.value,
      surname: this.editStudentForm.get('surname')?.value,
      studentClassId: this.editStudentForm.get('studentClass')?.value,
    }
  }

  compareStudentClassObjects(object1: number, object2: number) {
    return object1 == object2;
  }
}
