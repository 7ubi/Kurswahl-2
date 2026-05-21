import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SubjectResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-rule',
  templateUrl: './create-rule.component.html',
  imports: [
    ReactiveFormsModule,
    HeroComponent,
    MatFormField,
    MatLabel,
    MatInput,
    MatSelect,
    MatOption,
    MatButton
  ],
  styleUrl: './create-rule.component.css'
})
export class CreateRuleComponent implements OnInit {
  createRuleForm: FormGroup;
  subjectResponses?: SubjectResponse[];

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createRuleForm = this.formBuilder.group({
      name: ['', Validators.required],
      year: ['', Validators.required],
      subjects: [[], Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectResponse[]>('/api/admin/subjects', response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectResponses = response;
    });
  }

  createRule() {
    if (!this.createRuleForm.valid) {
      return;
    }

    this.httpService.post<undefined>('/api/admin/rule', this.getCreateRuleRequest(), response => {
      this.router.navigate(['admin', 'rules']);
    });
  }

  private getCreateRuleRequest() {
    return {
      name: this.createRuleForm.get('name')?.value,
      subjectIds: this.createRuleForm.get('subjects')?.value.map(Number),
      year: this.createRuleForm.get('year')?.value
    }
  }
}
