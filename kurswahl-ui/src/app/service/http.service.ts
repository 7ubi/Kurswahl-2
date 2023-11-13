import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ResultResponse} from "../app.responses";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar
  ) {}

  private async error(error: ResultResponse) {
    if (error.errorMessages) {
      error.errorMessages.forEach(error => {
        this.snackBar.open(error.message, 'Verstanden', {
          horizontalPosition: "center",
          verticalPosition: "bottom",
          duration: 5000
        });
      });
    }
  }

  private subscribe<Type>(observable: Observable<Type>, subscribe: (response: Type) => void, error?: () => void) {
    observable.subscribe(
      response => subscribe(response),
      err => {
        if(err.status === 401) {
          this.snackBar.open('Bitte melde dich an!', 'Verstanden', {
            horizontalPosition: "center",
            verticalPosition: "bottom",
            duration: 5000
          });
          this.authenticationService.logout();
        }

        this.error(err.error);
        if (error) {
          error();
        }
      }
    );
  }

  public post<Type>(url: string, request: any, subscribe: (response: Type) => void, error?: () => void)
    : void {
    const observable
      = this.http.post<Type>(url, request, { headers: this.authenticationService.getHeaderWithBearer()})
    this.subscribe(observable, subscribe, error);
  }

  public get<Type>(url: string, subscribe: (response: Type) => void, error?: () => void): void {
    const observable
      = this.http.get<Type>(url, { headers: this.authenticationService.getHeaderWithBearer()});
    this.subscribe(observable, subscribe, error);
  }

  public delete<Type>(url: string, subscribe: (response: Type) => void, error?: () => void, request?: any): void {
    const observable
      = this.http.delete<Type>(url, {headers: this.authenticationService.getHeaderWithBearer(), body: request});
    this.subscribe(observable, subscribe, error);
  }

  public put<Type>(url: string, request: any, subscribe: (response: Type) => void, error?: () => void): void {
    const observable
      = this.http.put<Type>(url, request, { headers: this.authenticationService.getHeaderWithBearer()});
    this.subscribe(observable, subscribe, error);
  }
}
