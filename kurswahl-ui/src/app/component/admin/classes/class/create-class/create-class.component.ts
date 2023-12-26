import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SubjectResponse, TapeResponse, TeacherResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-class',
  templateUrl: './create-class.component.html',
  styleUrls: ['./create-class.component.css']
})
export class CreateClassComponent implements OnInit {
  createClassForm: FormGroup;
  subjectResponses?: SubjectResponse[];
  teacherResponses?: TeacherResponse[];
  tapeResponses!: TapeResponse[];

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
    this.httpService.get<SubjectResponse[]>('/api/admin/subjects', response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectResponses = response;
    });

    this.httpService.get<TeacherResponse[]>('/api/admin/teachers', response => {
      response.sort((a, b) =>
        a.abbreviation.localeCompare(b.abbreviation));
      this.teacherResponses = response;
    });
  }

  createClass() {
    if (!this.createClassForm.valid || !this.isTapeFormFieldActive()) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/class', this.getCreateClassRequest(), response => {
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

    this.httpService.get<TapeResponse[]>(`/api/admin/tapes?year=${year}`, response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.tapeResponses = response;
    });
  }

  isTapeFormFieldActive(): boolean {
    return this.tapeResponses && this.tapeResponses.length > 0;
  }
}
