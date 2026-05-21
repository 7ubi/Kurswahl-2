import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-teacher',
  templateUrl: './create-teacher.component.html',
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatButton
  ],
  styleUrls: ['./create-teacher.component.css']
})
export class CreateTeacherComponent {
  createTeacherForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createTeacherForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
      abbreviation: ['', Validators.required],
    });
  }

  createTeacher() {
    if (!this.createTeacherForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/teacher', this.getCreateTeacherRequest(), response => {
      this.router.navigate(['admin', 'teachers']);
    });
  }

  private getCreateTeacherRequest() {
    return {
      firstname: this.createTeacherForm.get('firstname')?.value,
      surname: this.createTeacherForm.get('surname')?.value,
      abbreviation: this.createTeacherForm.get('abbreviation')?.value,
    }
  }
}
