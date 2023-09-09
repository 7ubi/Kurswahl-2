import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {AuthenticationService} from "./service/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class LoginRequired {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar
  ) {}

  canActivate(): Observable<boolean>|Promise<boolean>|boolean {
    if (!this.authenticationService.isLoggedIn()) {

      this.snackBar.open('Bitte melde dich an!', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
      this.router.navigate(['/']);
    }
    return true;
  }
}
