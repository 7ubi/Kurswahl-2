import {Component} from '@angular/core';
import {ChoiceResponse, TapeClassResponse} from "../../stundet.responses";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";
import jsPDF from "jspdf";
import {AuthenticationService} from "../../../../service/authentication.service";
import autoTable from "jspdf-autotable";

@Component({
  selector: 'app-show-choices',
  templateUrl: './show-choices.component.html',
  styleUrl: './show-choices.component.css'
})
export class ShowChoicesComponent {
  choiceResponses!: ChoiceResponse[];
  tapeClassResponses!: TapeClassResponse[];

  constructor(private httpService: HttpService, private router: Router,
              private authenticationService: AuthenticationService) {
    this.loadTapes();
    this.loadChoices();
  }

  private loadChoices() {
    this.httpService.get<ChoiceResponse[]>('/api/student/choices', response => {
      this.choiceResponses = response.sort((a, b) => a.choiceNumber - b.choiceNumber);
    }, () => this.router.navigate(['student']));
  }

  private loadTapes() {
    this.httpService.get<TapeClassResponse[]>(`/api/student/tapeClasses`, response => {
      this.tapeClassResponses = response.sort((a, b) => a.name.localeCompare(b.name));
    }, () => this.router.navigate(['student']));
  }

  editChoice(number: number) {
    this.router.navigate(['student', 'choice', number])
  }

  exportChoices() {
    if (this.choiceResponses.length === 2) {
      const doc = new jsPDF();

      const head = ['Band', '1. Wahl', '2. Wahl'];

      const info: {}[] = [];

      this.tapeClassResponses.forEach(tape => {
        const item: string[] = [tape.name, '', ''];

        const firstChoice = this.choiceResponses[0].classChoiceResponses
          .find(element => element.tapeId === tape.tapeId);
        if (firstChoice) {
          item[1] = firstChoice.name;
        }
        const secondChoice = this.choiceResponses[0].classChoiceResponses
          .find(element => element.tapeId === tape.tapeId);
        if (secondChoice) {
          item[2] = secondChoice.name;
        }

        info.push(item);
      });


      autoTable(doc, {
        head: [[this.authenticationService.getName()]]
      });
      autoTable(doc, {
        startY: 21,
        head: [head],
        body: info,
      });

      doc.save(`Wahlen-${this.authenticationService.getName()?.replace(' ', '-')}.pdf`);
    }
  }
}
