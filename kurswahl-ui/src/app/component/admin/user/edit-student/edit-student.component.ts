import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ResultResponse, StudentClassResponses, StudentResultResponse} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-student',
  templateUrl: './edit-student.component.html',
  styleUrls: ['./edit-student.component.css']
})
export class EditStudentComponent {
  editStudentForm: FormGroup;
  studentClasses?: StudentClassResponses;
  student?: StudentResultResponse;
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

    this.httpService.get<StudentResultResponse>(`/api/admin/student?studentId=${this.id}`, response => {
      this.student = response;
      this.editStudentForm.controls['firstname'].setValue(this.student.studentResponse.firstname);
      this.editStudentForm.controls['surname'].setValue(this.student.studentResponse.surname);
      this.editStudentForm.controls['studentClass']
        .setValue(this.student.studentResponse.studentClassResponse.studentClassId);
    }, () => this.router.navigate(['admin', 'students']));
  }

  editStudent() {
    if (!this.editStudentForm.valid) {
      return;
    }

    this.httpService.put<ResultResponse>(`/api/admin/student?studentId=${this.id}`, this.getCreateStudentRequest(), response => {
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

  compareCategoryObjects(object1: number, object2: number) {
    return object1 == object2;
  }
}
