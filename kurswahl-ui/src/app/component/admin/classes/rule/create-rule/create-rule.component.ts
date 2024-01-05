import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SubjectResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-rule',
  templateUrl: './create-rule.component.html',
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
