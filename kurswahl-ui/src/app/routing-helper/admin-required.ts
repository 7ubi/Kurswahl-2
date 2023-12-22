import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {AuthenticationService} from "../service/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Observable} from "rxjs";

@Injectable()
export class AdminRequired {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar
  ) {}

  canActivate(): Observable<boolean>|Promise<boolean>|boolean {
    if (this.authenticationService.getRole() !== 'ADMIN') {

      this.snackBar.open('Du musst ein Admin sein, bitte melde dich erneut an!', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
      this.authenticationService.logout();
      this.router.navigate(['/']);
    }
    return true;
  }
}
