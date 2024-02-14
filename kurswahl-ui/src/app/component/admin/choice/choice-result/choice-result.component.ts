import {Component, OnDestroy} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, ChildActivationEnd, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {ChoiceResultResponse} from "../../admin.responses";

@Component({
  selector: 'app-choice-result',
  templateUrl: './choice-result.component.html',
  styleUrl: './choice-result.component.css'
})
export class ChoiceResultComponent implements OnDestroy {
  year!: number;

  eventSubscription: Subscription;

  results?: ChoiceResultResponse;

  loadedResults = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute) {

    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ChildActivationEnd) {
        if (this.year != Number(this.route.snapshot.paramMap.get('year'))) {
          this.year = Number(this.route.snapshot.paramMap.get('year'));

          if (this.year < 11 || this.year > 12) {
            this.router.navigate(['admin', 'admins']);
          }

          this.loadedResults = false;
          this.results = undefined;
          this.loadClasses();
        }
      }
    });
  }

  loadClasses() {
    this.httpService.get<ChoiceResultResponse>(`/api/admin/result?year=${this.year}`,
      response => {
        this.results = response;
        this.loadedResults = true;
      });
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  goToAssignChoice(studentId: number, year: number) {
    this.router.navigate(['/admin', 'assignChoices', year, studentId]);
  }
}
