import {Component} from '@angular/core';
import {AuthenticationService} from "../../../service/authentication.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  name!: string | null;

  constructor(private authenticationService: AuthenticationService) {
    this.name = authenticationService.getName();
  }
}
