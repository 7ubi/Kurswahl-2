import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../../service/authentication.service";
import {LoginResponse} from "../../../app.responses";

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
    private formBuilder: FormBuilder
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

          this.router.navigate(['/']);
        }, error => {
          if(error.status === 401){

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
