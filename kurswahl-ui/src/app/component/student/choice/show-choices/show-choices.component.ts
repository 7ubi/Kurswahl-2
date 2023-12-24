import {Component} from '@angular/core';
import {ChoiceResponse} from "../../stundet.responses";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-show-choices',
  templateUrl: './show-choices.component.html',
  styleUrl: './show-choices.component.css'
})
export class ShowChoicesComponent {
  choiceResponses!: ChoiceResponse[];

  constructor(private httpService: HttpService, private router: Router) {
    this.loadChoices();
  }

  private loadChoices() {
    this.httpService.get<ChoiceResponse[]>('/api/student/choices', response => {
      this.choiceResponses = response;
    }, () => this.router.navigate(['student']));
  }
}
