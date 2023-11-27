import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ResultResponse, SubjectResponses, TapeResponses, TeacherResponses} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-class',
  templateUrl: './create-class.component.html',
  styleUrls: ['./create-class.component.css']
})
export class CreateClassComponent {
  createClassForm: FormGroup;
  subjectResponses?: SubjectResponses;
  teacherResponses?: TeacherResponses;
  tapeResponses!: TapeResponses;

  constructor(
      private formBuilder: FormBuilder,
      private httpService: HttpService,
      private router: Router
  ) {
    this.createClassForm = this.formBuilder.group({
      name: ['', Validators.required],
      subject: ['', Validators.required],
      teacher: ['', Validators.required],
      tape: ['', Validators.required],
      year: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectResponses>('/api/admin/subjects', response => {
      response.subjectResponses.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectResponses = response;
    });

    this.httpService.get<TeacherResponses>('/api/admin/teachers', response => {
      response.teacherResponses.sort((a, b) =>
          a.abbreviation.localeCompare(b.abbreviation));
      this.teacherResponses = response;
    });
  }

  createClass() {
    if (!this.createClassForm.valid || !this.isTapeFormFieldActive()) {
      return;
    }

    this.httpService.post<ResultResponse>('/api/admin/class', this.getCreateClassRequest(), response => {
      this.router.navigate(['admin', 'classes']);
    });
  }

  private getCreateClassRequest() {
    return {
      name: this.createClassForm.get('name')?.value,
      subjectId: this.createClassForm.get('subject')?.value,
      teacherId: this.createClassForm.get('teacher')?.value,
      tapeId: this.createClassForm.get('tape')?.value
    }
  }

  loadTapes($event: KeyboardEvent) {
    const year = Number((event?.target as HTMLInputElement).value);


    this.httpService.get<TapeResponses>(`/api/admin/tapes?year=${year}`, response => {
      response.tapeResponses.sort((a, b) => a.name.localeCompare(b.name));
      this.tapeResponses = response;
    });
  }

  isTapeFormFieldActive(): boolean {
    return this.tapeResponses?.tapeResponses.length > 0;
  }
}
