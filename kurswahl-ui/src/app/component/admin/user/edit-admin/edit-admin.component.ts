import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminResponses, ResultResponse} from "../../../../app.responses";

@Component({
  selector: 'app-edit-admin',
  templateUrl: './edit-admin.component.html',
  styleUrls: ['./edit-admin.component.css']
})
export class EditAdminComponent implements OnInit {
  editAdminForm: FormGroup;
  id?: string | null;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.editAdminForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
    })
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');

    this.httpService.get<AdminResponses>(`/api/admin/admin?adminId=${this.id}`, response => {
    }, () => this.router.navigate(['admin', 'admins']));
  }

  createAdmin() {
    if (!this.editAdminForm.valid) {
      return;
    }

    this.httpService.post<ResultResponse>('/api/admin/admin', this.getCreateAdminRequest(), response => {
      this.router.navigate(['admin', 'admins']);
    });
  }

  private getCreateAdminRequest() {
    return {
      firstname: this.editAdminForm.get('firstname')?.value,
      surname: this.editAdminForm.get('surname')?.value,
    }
  }
}
