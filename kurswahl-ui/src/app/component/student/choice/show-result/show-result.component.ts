import {ChangeDetectionStrategy, ChangeDetectorRef, Component} from '@angular/core';
import {ChoiceResponse, TapeClassResponse} from "../../stundet.responses";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";
import {ChoiceTableComponent} from "../choice-table/choice-table.component";
import {HeroComponent} from "../../../common/hero/hero.component";

@Component({
  selector: 'app-show-result',
  templateUrl: './show-result.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ChoiceTableComponent,
    HeroComponent
  ],
  styleUrl: './show-result.component.css'
})
export class ShowResultComponent {
  choiceResponse!: ChoiceResponse;
  tapeClassResponses!: TapeClassResponse[];

  constructor(private httpService: HttpService, private router: Router,
              private cdr: ChangeDetectorRef) {
    this.loadTapes();

    this.httpService.get<ChoiceResponse>('/api/student/choiceResult', response => {
      this.choiceResponse = response;
      this.cdr.detectChanges();
    }, () => this.router.navigate(['student']));
  }

  private loadTapes() {
    this.httpService.get<TapeClassResponse[]>(`/api/student/tapeClasses`, response => {
      this.tapeClassResponses = response;
      this.cdr.detectChanges();
    }, () => this.router.navigate(['student']));
  }

}
