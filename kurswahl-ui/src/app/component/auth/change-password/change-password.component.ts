import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ResultResponse} from "../../../app.responses";
import {HttpService} from "../../../service/http.service";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  public changePasswordFormGroup: FormGroup;
  hideOld = true;
  hideNew = true;
  hideNewRepeat = true;

  constructor(
    private httpService: HttpService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.changePasswordFormGroup = this.formBuilder.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      newPasswordRepeat: ['', Validators.required],
    });
  }

  makeLogin() {
    if (!this.changePasswordFormGroup.valid) {
      return;
    }

    if (!this.matchPassword()) {
      this.snackBar.open('Die Passwörter müssen übereinstimmen', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });

      return;
    }

    this.httpService.put<ResultResponse>('/api/auth/changePassword', this.getPasswordChangeParameter(), response => {
      this.snackBar.open('Passwort wurde geändert', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  getPasswordChangeParameter() {
    return {
      oldPassword: this.changePasswordFormGroup.get('oldPassword')?.value,
      newPassword: this.changePasswordFormGroup.get('newPassword')?.value,
    };
  }

  private matchPassword() {
    return this.changePasswordFormGroup.get('newPassword')?.value === this.changePasswordFormGroup.get('newPasswordRepeat')?.value;
  }
}
