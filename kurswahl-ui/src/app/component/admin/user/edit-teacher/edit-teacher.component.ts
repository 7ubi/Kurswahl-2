import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TeacherResponse} from "../../../../app.responses";

@Component({
  selector: 'app-edit-teacher',
  templateUrl: './edit-teacher.component.html',
  styleUrls: ['./edit-teacher.component.css']
})
export class EditTeacherComponent implements OnInit {
  editTeacherForm: FormGroup;
  id: string | null;
  teacher?: TeacherResponse;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editTeacherForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
      abbreviation: ['', Validators.required],
    });
  }

  ngOnInit(): void {

    this.httpService.get<TeacherResponse>(`/api/admin/teacher?teacherId=${this.id}`, response => {
      this.teacher = response;
      this.editTeacherForm.controls['firstname'].setValue(this.teacher.firstname);
      this.editTeacherForm.controls['surname'].setValue(this.teacher.surname);
      this.editTeacherForm.controls['abbreviation'].setValue(this.teacher.abbreviation);
    }, () => this.router.navigate(['admin', 'teachers']));
  }

  editTeacher() {
    if (!this.editTeacherForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/teacher?teacherId=${this.id}`, this.getCreateTeacherRequest(), response => {
      this.router.navigate(['admin', 'teachers']);
    });
  }

  private getCreateTeacherRequest() {
    return {
      firstname: this.editTeacherForm.get('firstname')?.value,
      surname: this.editTeacherForm.get('surname')?.value,
      abbreviation: this.editTeacherForm.get('abbreviation')?.value,
    }
  }
}
