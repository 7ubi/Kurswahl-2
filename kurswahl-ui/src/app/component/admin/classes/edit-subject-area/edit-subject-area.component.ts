import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SubjectAreaResponse} from "../../../../app.responses";

@Component({
  selector: 'app-edit-subject-area',
  templateUrl: './edit-subject-area.component.html',
  styleUrls: ['./edit-subject-area.component.css']
})
export class EditSubjectAreaComponent implements OnInit {
  editSubjectAreaForm: FormGroup;
  id: string | null;
  subjectArea?: SubjectAreaResponse;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id');

    this.editSubjectAreaForm = this.formBuilder.group({
      name: ['', Validators.required],
    })
  }

  ngOnInit(): void {
    this.httpService.get<SubjectAreaResponse>(`/api/admin/subjectArea?subjectAreaId=${this.id}`, response => {
      this.subjectArea = response;
      this.editSubjectAreaForm.controls['name'].setValue(this.subjectArea.name);
    }, () => this.router.navigate(['admin', 'subjectAreas']));
  }

  editSubjectArea() {
    if (!this.editSubjectAreaForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/subjectArea?subjectAreaId=${this.id}`,
      this.getCreateSubjectAreaRequest(), response => {
        this.router.navigate(['admin', 'subjectAreas']);
      });
  }

  private getCreateSubjectAreaRequest() {
    return {
      name: this.editSubjectAreaForm.get('name')?.value,
    }
  }
}
