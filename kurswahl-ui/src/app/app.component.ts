import {Component} from '@angular/core';
import {AuthenticationService} from "./service/authentication.service";
import packageInfo from '../../package.json';
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {RouterLink, RouterOutlet} from "@angular/router";
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {SidenavComponent} from "./component/common/sidenav/sidenav.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [
    MatToolbar,
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuItem,
    RouterLink,
    MatMenuTrigger,
    MatSidenavContainer,
    MatSidenav,
    SidenavComponent,
    MatSidenavContent,
    RouterOutlet
  ],
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
