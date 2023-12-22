import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TapeResponse} from "../../../admin.responses";

@Component({
  selector: 'app-edit-tape',
  templateUrl: './edit-tape.component.html',
  styleUrls: ['./edit-tape.component.css']
})
export class EditTapeComponent implements OnInit {
  editStudentClassForm: FormGroup;
  id: string | null;
  tape?: TapeResponse;

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
      lk: [false, Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<TapeResponse>(`/api/admin/tape?tapeId=${this.id}`,
      response => {
        this.tape = response;
        this.editStudentClassForm.controls['name'].setValue(this.tape.name);
        this.editStudentClassForm.controls['year'].setValue(this.tape.year);
        this.editStudentClassForm.controls['lk'].setValue(this.tape.lk);
      }, () => this.router.navigate(['admin', 'tapes']));
  }

  editStudentClass() {
    if (!this.editStudentClassForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/tape?tapeId=${this.id}`, this.getStudentClassRequest(),
      response => this.router.navigate(['admin', 'tapes']));
  }

  private getStudentClassRequest() {
    return {
      name: this.editStudentClassForm.get('name')?.value,
      year: this.editStudentClassForm.get('year')?.value,
      lk: this.editStudentClassForm.get('lk')?.value
    }
  }
}
