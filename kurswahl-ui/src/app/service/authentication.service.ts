import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {LoginResponse} from "../app.responses";
import {StorageService} from "./storage.service";
import {HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private type = 'Bearer';

  constructor(private router: Router) {
  }

  public saveBearer(loginResponse: LoginResponse) {
    StorageService.saveData(loginResponse.type, loginResponse.token);
  }

  public getBearer(): string | null {
    return StorageService.getData(this.type);
  }

  public isLoggedIn(): boolean {
    return StorageService.getData(this.type) !== '';
  }

  public getHeaderWithBearer(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.getBearer()}`
    })
  }

  public logout(): void {
    StorageService.clearData();
    this.router.navigate(['/']);
  }
}