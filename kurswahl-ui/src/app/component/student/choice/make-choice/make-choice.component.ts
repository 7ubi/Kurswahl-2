import {Component, OnDestroy} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, ActivationEnd, Router} from "@angular/router";
import {
  ChoiceResponse,
  ClassChoiceResponse,
  ClassResponse,
  SubjectTapeResponse,
  TapeClassResponse
} from "../../stundet.responses";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-make-choice',
  templateUrl: './make-choice.component.html',
  styleUrl: './make-choice.component.css'
})
export class MakeChoiceComponent implements OnDestroy {
  readonly maxChoices: number = 2;

  choiceNumber: number | null | undefined;
  subjectTapeResponses!: SubjectTapeResponse[];
  choiceResponse!: ChoiceResponse;

  selectedTape?: TapeClassResponse;

  eventSubscription: Subscription;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ActivationEnd) {
        this.choiceNumber = Number(this.route.snapshot.paramMap.get('choiceNumber'));
        if (this.choiceNumber < 1 || this.choiceNumber > this.maxChoices) {
          this.router.navigate(['student']);
        }
        this.loadChoice();
      }
    });

    this.loadSubjects();
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  private loadSubjects() {
    this.httpService.get<SubjectTapeResponse[]>(`/api/student/subjectTapes`, response => {
      this.subjectTapeResponses = response;
    });
  }

  private loadChoice() {
    this.httpService.get<ChoiceResponse>(`/api/student/choice?choiceNumber=${this.choiceNumber}`, response => {
      this.choiceResponse = response;
    });
  }

  alterChoice(classId: number) {
    this.httpService.put<undefined>('/api/student/choice', this.getAlterChoiceRequest(classId),
        response => this.loadChoice());
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
      this.router.navigate(['student', 'choice', 2]);
    }
    this.router.navigate(['student', 'choices']);
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

    if(classResponse) {
      this.httpService.delete<undefined>('/api/student/choice', response => this.loadChoice(),
        () => {}, this.getDeleteClassFromChoiceRequest(classResponse))
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
