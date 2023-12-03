import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../../service/authentication.service";
import {LoginResponse, Role} from "../../../app.responses";
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
    private http: HttpClient,
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
    if(!this.loginFormGroup.valid) {
      return;
    }

    this.http.post<LoginResponse>('/api/auth/login', this.getLoginRequestParameter())
      .subscribe(
        response => {
          this.authenticationService.saveBearer(response);
          this.authenticationService.saveRole(response.role);
          this.authenticationService.saveName(response.name);

          if(response.role.toString() === Role.ADMIN.toString()) {
            this.router.navigate(['admin', 'admins']);
          } else if (response.role.toString() === Role.STUDENT.toString()) {
            this.router.navigate(['student']);
          }
        }, error => {
          if(error.status === 401){
            this.snackBar.open('Nutzername oder Passwort ist falsch', 'Verstanden', {
              horizontalPosition: "center",
              verticalPosition: "bottom",
              duration: 5000
            });
          }
        }
      );
  }

  getLoginRequestParameter() {
    return {
      username: this.loginFormGroup.get('username')?.value,
      password: this.loginFormGroup.get('password')?.value,
    };
  }
}
