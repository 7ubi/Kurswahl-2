import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ClassResponse, SubjectResponses, TapeResponses, TeacherResponses} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-class',
  templateUrl: './edit-class.component.html',
  styleUrls: ['./edit-class.component.css']
})
export class EditClassComponent implements OnInit {
  editClassForm: FormGroup;
  subjectResponses?: SubjectResponses;
  teacherResponses?: TeacherResponses;
  tapeResponses!: TapeResponses;
  classResultResponse?: ClassResponse;
  id: string | null;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editClassForm = this.formBuilder.group({
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

    this.httpService.get<ClassResponse>(`/api/admin/class?classId=${this.id}`, response => {
      this.classResultResponse = response;

      this.editClassForm.controls['name'].setValue(this.classResultResponse.name);
      this.editClassForm.controls['year'].setValue(this.classResultResponse.tapeResponse.year);
      this.editClassForm.controls['teacher'].setValue(this.classResultResponse.teacherResponse.teacherId);
      this.editClassForm.controls['tape'].setValue(this.classResultResponse.tapeResponse.tapeId);
      this.editClassForm.controls['subject'].setValue(this.classResultResponse.subjectResponse.subjectId);

      this.httpService.get<TapeResponses>(
          `/api/admin/tapes?year=${this.classResultResponse?.tapeResponse.year}`, response => {
          response.tapeResponses.sort((a, b) => a.name.localeCompare(b.name));
          this.tapeResponses = response;
        });
    });

  }

  createClass() {
    if (!this.editClassForm.valid || !this.isTapeFormFieldActive()) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/class?classId=${this.id}`, this.getCreateClassRequest(), response => {
      this.router.navigate(['admin', 'classes']);
    });
  }

  private getCreateClassRequest() {
    return {
      name: this.editClassForm.get('name')?.value,
      subjectId: this.editClassForm.get('subject')?.value,
      teacherId: this.editClassForm.get('teacher')?.value,
      tapeId: this.editClassForm.get('tape')?.value
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


  compareObjects(object1: number, object2: number) {
    return object1 == object2;
  }
}
