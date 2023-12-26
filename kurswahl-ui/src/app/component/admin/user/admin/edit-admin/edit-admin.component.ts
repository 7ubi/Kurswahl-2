import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminResponse} from "../../../admin.responses";

@Component({
  selector: 'app-edit-admin',
  templateUrl: './edit-admin.component.html',
  styleUrls: ['./edit-admin.component.css']
})
export class EditAdminComponent implements OnInit {
  editAdminForm: FormGroup;
  admin?: AdminResponse;
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

    this.httpService.get<AdminResponse>(`/api/admin/admin?adminId=${this.id}`, response => {
      this.admin = response;
      this.editAdminForm.controls['firstname'].setValue(this.admin.firstname);
      this.editAdminForm.controls['surname'].setValue(this.admin.surname);
    }, () => this.router.navigate(['admin', 'admins']));
  }

  editAdmin() {
    if (!this.editAdminForm.valid) {
      return;
    }

    this.httpService.put<undefined>(`/api/admin/admin?adminId=${this.id}`, this.getCreateAdminRequest(), response => {
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
