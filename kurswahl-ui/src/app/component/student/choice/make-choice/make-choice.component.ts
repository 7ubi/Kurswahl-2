import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, ActivationEnd, NavigationCancel, NavigationEnd, NavigationError, Router} from "@angular/router";
import {
  ChoiceResponse,
  ClassChoiceResponse,
  ClassResponse,
  SubjectTapeResponse,
  TapeClassResponse
} from "../../stundet.responses";
import {Subscription} from "rxjs";
import {ChoiceTableComponent} from "../choice-table/choice-table.component";
import {HeroComponent} from "../../../common/hero/hero.component";
import {MatExpansionPanel, MatExpansionPanelHeader, MatExpansionPanelTitle} from "@angular/material/expansion";
import {MatButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-make-choice',
  templateUrl: './make-choice.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    ChoiceTableComponent,
    HeroComponent,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatExpansionPanelTitle,
    MatButton,
    MatProgressSpinner
  ],
  styleUrl: './make-choice.component.css'
})
export class MakeChoiceComponent implements OnDestroy {
  readonly maxChoices: number = 2;

  choiceNumber: number | null | undefined;
  subjectTapeResponses!: SubjectTapeResponse[];
  choiceResponse!: ChoiceResponse;
  tapeClassResponses!: TapeClassResponse[];

  selectedTape?: TapeClassResponse;
  isLoading: boolean = false;

  eventSubscription: Subscription;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {
    this.loadTapes();

    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ActivationEnd) {
        if (this.choiceNumber != Number(this.route.snapshot.paramMap.get('choiceNumber'))) {
          this.choiceNumber = Number(this.route.snapshot.paramMap.get('choiceNumber'));
          if (this.choiceNumber < 1 || this.choiceNumber > this.maxChoices) {
            this.router.navigate(['student']);
          }
          this.loadChoice();
        }
      }

      if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError) {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });

    this.loadSubjects();
  }

  private loadTapes() {
    this.httpService.get<TapeClassResponse[]>(`/api/student/tapeClasses`, response => {
      this.tapeClassResponses = response;
      this.cdr.detectChanges();
    }, () => this.router.navigate(['student']));
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  private loadSubjects() {
    this.httpService.get<SubjectTapeResponse[]>(`/api/student/subjectTapes`, response => {
      this.subjectTapeResponses = response;
      this.cdr.detectChanges();
    });
  }

  private loadChoice() {
    this.httpService.get<ChoiceResponse>(`/api/student/choice?choiceNumber=${this.choiceNumber}`, response => {
      this.choiceResponse = response;
      this.cdr.detectChanges();
    });
  }

  alterChoice(classId: number) {
    this.isLoading = true;
    this.httpService.put<ChoiceResponse>('/api/student/choice', this.getAlterChoiceRequest(classId),
      response => {
        this.choiceResponse = response;
        this.isLoading = false;
        this.cdr.detectChanges();
      });
  }

  getAlterChoiceRequest(classId: number) {
    return {
      choiceNumber: this.choiceNumber,
      classId: classId
    }
  }

  getClassForSelectClass(classResponse: ClassResponse): string {
    if (this.choiceResponse && this.choiceResponse.classChoiceResponses &&
      this.choiceResponse.classChoiceResponses.filter(c =>
        c.classId === classResponse.classId).length > 0) {
      return 'taken';
    }

    return '';
  }

  nextStep() {
    if (this.choiceNumber! === 1) {
      this.isLoading = true;
      this.router.navigate(['student', 'choice', 2]);
    } else {
      this.router.navigate(['student', 'choices']);
    }
  }

  getNextStepText(): string {
    if (this.choiceNumber! === 1) {
      return "Zur zweit Wahl"
    }
    return "Wahl beenden";
  }


  deleteClassFromChoice() {
    const classResponse = this.choiceResponse!.classChoiceResponses!.filter(c =>
      c.tapeId === this.selectedTape?.tapeId)[0];

    if (classResponse) {
      this.isLoading = true;
      this.httpService.delete<ChoiceResponse>('/api/student/choice', response => {
          this.choiceResponse = response;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        () => {
          this.isLoading = false;
        }, this.getDeleteClassFromChoiceRequest(classResponse))
    }
  }

  private getDeleteClassFromChoiceRequest(classResponse: ClassChoiceResponse) {
    return {
      choiceId: this.choiceResponse.choiceId,
      classId: classResponse.classId
    };
  }

  selectTape($event: TapeClassResponse) {
    this.selectedTape = $event;
  }
}
