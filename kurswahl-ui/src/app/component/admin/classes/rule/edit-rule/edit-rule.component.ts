import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {RuleResponse, SubjectResponse} from "../../../admin.responses";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {runE2e} from "@angular/cli/src/commands/mcp/tools/e2e";

@Component({
  selector: 'app-edit-rule',
  templateUrl: './edit-rule.component.html',
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatSelect,
    MatOption,
    MatButton,
    MatProgressSpinner
  ],
  styleUrl: './edit-rule.component.css'
})
export class EditRuleComponent implements OnInit {
  editRuleForm: FormGroup;
  subjectResponses?: SubjectResponse[];

  id: string | null;
  rule?: RuleResponse;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');
    this.editRuleForm = this.formBuilder.group({
      name: ['', Validators.required],
      year: ['', Validators.required],
      subjects: [[], Validators.required]
    });
  }

  ngOnInit(): void {
    this.httpService.get<SubjectResponse[]>('/api/admin/subjects', response => {
      response.sort((a, b) => a.name.localeCompare(b.name));
      this.subjectResponses = response;
      this.httpService.get<RuleResponse>(`/api/admin/rule?ruleId=${this.id}`, r => {
        this.rule = r;
        this.editRuleForm.controls['name'].setValue(this.rule.name);
        this.editRuleForm.controls['year'].setValue(this.rule.year);
        let subjectIds: string[] = [];
        this.rule.subjectResponses.forEach(subject => {
          subjectIds.push(subject.subjectId.toString());
        });
        this.editRuleForm.controls['subjects'].setValue(subjectIds);
      }, () => this.router.navigate(['admin', 'rules']));
    });
  }

  createRule() {
    if (!this.editRuleForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/rule?ruleId=${this.id}`, this.getCreateRuleRequest(), response => {
      this.router.navigate(['admin', 'rules']);
    });
  }

  private getCreateRuleRequest() {
    return {
      name: this.editRuleForm.get('name')?.value,
      subjectIds: this.editRuleForm.get('subjects')?.value.map(Number),
      year: this.editRuleForm.get('year')?.value
    }
  }

  protected readonly runE2e = runE2e;
}
