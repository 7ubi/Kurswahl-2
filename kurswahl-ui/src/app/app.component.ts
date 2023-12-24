import {Component} from '@angular/core';
import {AuthenticationService} from "./service/authentication.service";
import packageInfo from '../../package.json';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Kurswahl';
  isOpen = false;
  year: number = new Date().getFullYear();
  version = packageInfo.version;

  constructor(private authenticationService: AuthenticationService) {
  }

  isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn();
  }

  logout(): void {
    this.authenticationService.logout();
  }
}
