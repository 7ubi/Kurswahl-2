import { Component } from '@angular/core';
import {AuthenticationService} from "../../../service/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent {
  constructor(private authenticationService: AuthenticationService, private router: Router) {}

  getRole(): string | null {
    return this.authenticationService.getRole();
  }

  navigateTo(url: string) {
    this.router.navigate([url]);
  }
}
