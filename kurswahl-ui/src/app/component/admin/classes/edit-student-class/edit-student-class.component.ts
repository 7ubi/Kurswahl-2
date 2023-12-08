import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ResultResponse, StudentClassResponse, TeacherResponses} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-student-class',
  templateUrl: './edit-student-class.component.html',
  styleUrls: ['./edit-student-class.component.css']
})
export class EditStudentClassComponent implements OnInit {
  editStudentClassForm: FormGroup;
  teachers?: TeacherResponses;
  studentClass?: StudentClassResponse;
  id: string | null;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editStudentClassForm = this.formBuilder.group({
      name: ['', Validators.required],
      year: ['', Validators.required],
      teacher: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<StudentClassResponse>(`/api/admin/studentClass?studentClassId=${this.id}`,
      response => {
        this.studentClass = response;
        this.editStudentClassForm.controls['name'].setValue(this.studentClass.name);
        this.editStudentClassForm.controls['year'].setValue(this.studentClass.year);
        this.editStudentClassForm.controls['teacher'].setValue(this.studentClass.teacher.teacherId);
      }, () => this.router.navigate(['admin', 'studentClasses']));

    this.httpService.get<TeacherResponses>('/api/admin/teachers', response => {
      response.teacherResponses.sort(
        (a, b) => a.abbreviation.localeCompare(b.abbreviation));
      this.teachers = response;
    });
  }

  editStudentClass() {
    if (!this.editStudentClassForm.valid) {
      return;
    }

    this.httpService.put<ResultResponse>(`/api/admin/studentClass?studentClassId=${this.id}`,
      this.getStudentClassRequest(), response =>
        this.router.navigate(['admin', 'studentClasses']));
  }

  private getStudentClassRequest() {
    console.log(this.editStudentClassForm.get('teacher')?.value)
    return {
      name: this.editStudentClassForm.get('name')?.value,
      year: this.editStudentClassForm.get('year')?.value,
      teacherId: this.editStudentClassForm.get('teacher')?.value
    }
  }

  compareSubjectAreaObjects(object1: number, object2: number) {
    return object1 == object2;
  }
}
