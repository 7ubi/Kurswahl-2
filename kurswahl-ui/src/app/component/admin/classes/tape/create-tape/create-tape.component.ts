import {ChangeDetectionStrategy, Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-tape',
  templateUrl: './create-tape.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatCheckbox,
    MatButton
  ],
  styleUrls: ['./create-tape.component.css']
})
export class CreateTapeComponent {
  createStudentClassForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createStudentClassForm = this.formBuilder.group({
      name: ['', Validators.required],
      year: ['', Validators.required],
      lk: [false, Validators.required]
    });
  }

  createStudentClass() {
    if (!this.createStudentClassForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/tape', this.getStudentClassRequest(), response => {
      this.router.navigate(['admin', 'tapes']);
    });
  }

  private getStudentClassRequest() {
    return {
      name: this.createStudentClassForm.get('name')?.value,
      year: this.createStudentClassForm.get('year')?.value,
      lk: this.createStudentClassForm.get('lk')?.value
    }
  }
}
