import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../../service/http.service";
import {ResultResponse} from "../../../../app.responses";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-admin',
  templateUrl: './create-admin.component.html',
  styleUrls: ['./create-admin.component.css']
})
export class CreateAdminComponent {
  createAdminForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService,
    private router: Router
  ) {
    this.createAdminForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required],
    })
  }

  createAdmin() {
    this.httpService.post<ResultResponse>('/api/admin/admin', this.getCreateAdminRequest(), response => {
      this.router.navigate(['admin', 'admins']);
    });
  }

  private getCreateAdminRequest() {
    return {
      firstname: this.createAdminForm.get('firstname')?.value,
      surname: this.createAdminForm.get('surname')?.value,
    }
  }
}
