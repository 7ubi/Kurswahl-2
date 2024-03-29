import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Observable} from "rxjs";
import {Router} from "@angular/router";
import {Role} from "../component/admin/admin.responses";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
  }

  private subscribe<Type>(observable: Observable<Type>, subscribe: (response: Type) => void, error?: () => void) {
    observable.subscribe(
      response => subscribe(response),
      err => {
        if (err.status === 401) {
          this.snackBar.open('Bitte melde dich an!', 'Verstanden', {
            horizontalPosition: "center",
            verticalPosition: "bottom",
            duration: 5000
          });
          this.authenticationService.logout();
        }
        if (err.status === 403 && this.authenticationService.getRole() === Role.STUDENT) {
          this.router.navigate(['student']);
        }
        this.snackBar.open(err.error, 'Verstanden', {
          horizontalPosition: "center",
          verticalPosition: "bottom",
          duration: 5000
        });
        if (error) {
          error();
        }
      }
    );
  }

  public post<Type>(url: string, request: any, subscribe: (response: Type) => void, error?: () => void)
    : void {
    const observable
      = this.http.post<Type>(url, request, {headers: this.authenticationService.getHeaderWithBearer()})
    this.subscribe(observable, subscribe, error);
  }

  public get<Type>(url: string, subscribe: (response: Type) => void, error?: () => void): void {
    const observable
      = this.http.get<Type>(url, {headers: this.authenticationService.getHeaderWithBearer()});
    this.subscribe(observable, subscribe, error);
  }

  public delete<Type>(url: string, subscribe: (response: Type) => void, error?: () => void, request?: any): void {
    const observable
      = this.http.delete<Type>(url, {headers: this.authenticationService.getHeaderWithBearer(), body: request});
    this.subscribe(observable, subscribe, error);
  }

  public put<Type>(url: string, request: any, subscribe: (response: Type) => void, error?: () => void): void {
    const observable
      = this.http.put<Type>(url, request, {headers: this.authenticationService.getHeaderWithBearer()});
    this.subscribe(observable, subscribe, error);
  }
}
