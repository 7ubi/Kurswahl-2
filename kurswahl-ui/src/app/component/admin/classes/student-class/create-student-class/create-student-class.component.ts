import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TeacherResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-student-class',
  templateUrl: './create-student-class.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
    MatButton,
    MatInput
  ],
  styleUrls: ['./create-student-class.component.css']
})
export class CreateStudentClassComponent implements OnInit {
  createStudentClassForm: FormGroup;
  teachers?: TeacherResponse[];

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.createStudentClassForm = this.formBuilder.group({
      name: ['', Validators.required],
      year: ['', Validators.required],
      teacher: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<TeacherResponse[]>('/api/admin/teachers', response => {
      response.sort(
        (a, b) => a.abbreviation.localeCompare(b.abbreviation));
      this.teachers = response;
      this.cdr.detectChanges();
    });
  }

  createStudentClass() {
    if (!this.createStudentClassForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/studentClass', this.getStudentClassRequest(), response => {
      this.router.navigate(['admin', 'studentClasses']);
    });
  }

  private getStudentClassRequest() {
    return {
      name: this.createStudentClassForm.get('name')?.value,
      year: this.createStudentClassForm.get('year')?.value,
      teacherId: this.createStudentClassForm.get('teacher')?.value
    }
  }
}
