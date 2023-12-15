import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {ChoiceResponse, TapeClassResponse} from "../../stundet.responses";
import {LessonForTable, LessonTable} from "./lesson-table";

@Component({
  selector: 'app-make-choice',
  templateUrl: './make-choice.component.html',
  styleUrl: './make-choice.component.css'
})
export class MakeChoiceComponent implements OnInit {
  readonly maxChoices: number = 2;
  readonly maxHours = 15;

  lessons: LessonTable[] = [];
  choiceNumber: number | null;
  dataSource!: MatTableDataSource<LessonTable>;
  displayedColumns: string[];
  tapeClassResponses!: TapeClassResponse[];
  choiceResponse!: ChoiceResponse;

  selectedTape?: TapeClassResponse;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.choiceNumber = Number(this.route.snapshot.paramMap.get('choiceNumber'));

    if (this.choiceNumber < 1 || this.choiceNumber > this.maxChoices) {
      this.router.navigate(['student']);
    }

    this.displayedColumns = ['Stunde', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag'];
  }

  ngOnInit(): void {
    this.httpService.get<TapeClassResponse[]>(`/api/student/tapeChoice`, response => {
      this.tapeClassResponses = response;
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
    this.lessons.forEach(lesson => {
      this.choiceResponse.classChoiceResponses.forEach(classChoice => {
        lesson.days.filter(l => l.tapeClass?.tapeId === classChoice.tapeId)
          .forEach(l => l.choice = classChoice);
      });
    });
  }

  getClassForCell(element?: TapeClassResponse): string {
    let elementClass: string = 'day';

    if (element && element == this.selectedTape) {
      elementClass += ' selected-tape'
    } else if (element) {
      elementClass += ' taken';
    }

    return elementClass;
  }

  selectTape(tapeClass: TapeClassResponse | null) {
    if (null === tapeClass || tapeClass === this.selectedTape) {
      this.selectedTape = undefined;
    } else {
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
}
