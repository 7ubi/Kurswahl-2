import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, ChildActivationEnd, Router} from "@angular/router";
import {HttpService} from "../../../../service/http.service";
import {Subscription} from "rxjs";
import {ClassChoiceResponse} from "../../admin.responses";

@Component({
  selector: 'app-assign-choice',
  templateUrl: './assign-choice.component.html',
  styleUrl: './assign-choice.component.css'
})
export class AssignChoiceComponent implements OnDestroy {
  year!: number;

  eventSubscription: Subscription;

  classes?: ClassChoiceResponse[];

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute) {
    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ChildActivationEnd) {
        console.log(this.route, event)
        if (this.year != Number(this.route.snapshot.paramMap.get('year'))) {
          this.year = Number(this.route.snapshot.paramMap.get('year'));

          if (this.year < 11 || this.year > 12) {
            this.router.navigate(['admin', 'admins']);
          }

          this.loadClasses();
        }
      }
    });
  }

  loadClasses() {
    this.httpService.get <ClassChoiceResponse[]>(`/api/admin/classesChoices?year=${this.year}`,
      response => this.classes = response);
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }
}
