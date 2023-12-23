import {Component, OnDestroy} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, ActivationEnd, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {
  ChoiceResponse,
  ClassChoiceResponse,
  ClassResponse,
  SubjectTapeResponse,
  TapeClassResponse,
  TapeResponses
} from "../../stundet.responses";
import {LessonForTable, LessonTable} from "../lesson-table";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-make-choice',
  templateUrl: './make-choice.component.html',
  styleUrl: './make-choice.component.css'
})
export class MakeChoiceComponent implements OnDestroy {
  readonly maxChoices: number = 2;
  readonly maxHours = 15;

  lessons: LessonTable[] = [];
  choiceNumber: number | null | undefined;
  dataSource!: MatTableDataSource<LessonTable>;
  displayedColumns: string[];
  tapeClassResponses!: TapeClassResponse[];
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
        this.loadTapes();
      }
    });

    this.displayedColumns = ['Stunde', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag'];
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  private loadTapes() {
    this.httpService.get<TapeResponses>(`/api/student/tapes`, response => {
      this.tapeClassResponses = response.tapeClassResponses;
      this.subjectTapeResponses = response.subjectTapeResponses;
      this.loadChoice();
    });
  }

  private loadChoice() {
    this.httpService.get<ChoiceResponse>(`/api/student/choice?choiceNumber=${this.choiceNumber}`, response => {
      this.choiceResponse = response;
      this.updateTable();
    });
  }

  private generateTable() {
    this.lessons = [];
    for (let i = 1; i <= this.maxHours; i++) {
      let lesson: LessonTable = {
        hour: i,
        days: [
          new LessonForTable(),
          new LessonForTable(),
          new LessonForTable(),
          new LessonForTable(),
          new LessonForTable()
        ]
      };
      this.lessons.push(lesson)
    }

    this.tapeClassResponses.forEach(tape => {
      tape.lessonResponses.forEach(lesson => {
        this.lessons[lesson.hour].days[lesson.day].tapeClass = tape;
      });
    });

    this.dataSource = new MatTableDataSource(this.lessons);
  }

  private updateTable() {
    this.generateTable();
    if (this.choiceResponse && this.choiceResponse.classChoiceResponses) {
      this.lessons.forEach(lesson => {
        this.choiceResponse.classChoiceResponses.forEach(classChoice => {
          lesson.days.filter(l => l.tapeClass?.tapeId === classChoice.tapeId)
            .forEach(l => l.choice = classChoice);
        });
      });
    }
  }

  getClassForCell(element?: LessonForTable): string {
    let elementClass: string = 'day';


    if (element?.tapeClass && element.tapeClass === this.selectedTape) {
      elementClass += ' selected-tape'
    } else if (element?.choice) {
      elementClass += ' taken';
    } else if (element?.tapeClass) {
      elementClass += ' choose';
    }

    return elementClass;
  }

  selectTape(tapeClass: TapeClassResponse | null) {
    if (null === tapeClass || tapeClass === this.selectedTape) {
      this.selectedTape = undefined;
    } else {
      console.log(tapeClass);
      this.selectedTape = tapeClass;
    }
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
}
