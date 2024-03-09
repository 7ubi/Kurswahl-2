import {Component} from '@angular/core';
import {ChoiceResponse} from "../../stundet.responses";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-show-result',
  templateUrl: './show-result.component.html',
  styleUrl: './show-result.component.css'
})
export class ShowResultComponent {
  choiceResponse!: ChoiceResponse;

  constructor(private httpService: HttpService, private router: Router) {
    this.httpService.get<ChoiceResponse>('/api/student/choiceResult', response => {
      this.choiceResponse = response;
    }, () => this.router.navigate(['student']));
  }

}
