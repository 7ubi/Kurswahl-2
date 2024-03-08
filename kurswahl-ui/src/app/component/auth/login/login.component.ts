import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../../service/authentication.service";
import {LoginResponse, Role} from "../../admin/admin.responses";
import {HttpService} from "../../../service/http.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public loginFormGroup: FormGroup;
  hide = true;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.loginFormGroup = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  makeLogin() {
    if (!this.loginFormGroup.valid) {
      return;
    }

    this.httpService.post<LoginResponse>('/api/auth/login', this.getLoginRequestParameter(), response => {
      this.authenticationService.saveBearer(response);
      this.authenticationService.saveRole(response.role);
      this.authenticationService.saveName(response.name);

      if (!response.changedPassword) {
        this.router.navigate(['changePassword']);
        this.snackBar.open('Bitte ändere dein Passwort für mehr Sicherheit', 'Verstanden', {
          horizontalPosition: "center",
          verticalPosition: "bottom",
          duration: 5000
        });
        return;
      }

      if (response.role.toString() === Role.ADMIN.toString()) {
        this.router.navigate(['admin', 'admins']);
      } else if (response.role.toString() === Role.STUDENT.toString()) {
        this.router.navigate(['student']);
      }
    });
  }

  getLoginRequestParameter() {
    return {
      username: this.loginFormGroup.get('username')?.value,
      password: this.loginFormGroup.get('password')?.value,
    };
  }
}
