import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {StudentClassResponse} from "../../../admin.responses";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-student',
  templateUrl: './create-student.component.html',
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
  styleUrls: ['./create-student.component.css']
})
export class CreateStudentComponent implements OnInit {
  createStudentForm: FormGroup;
  studentClasses?: StudentClassResponse[];

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.createStudentForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
      studentClass: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<StudentClassResponse[]>('/api/admin/studentClasses', response => {
      this.studentClasses = response;
      this.cdr.detectChanges();
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
