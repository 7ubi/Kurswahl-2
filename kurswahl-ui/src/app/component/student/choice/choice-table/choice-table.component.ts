import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {ChoiceResponse, TapeClassResponse} from "../../stundet.responses";
import {LessonForTable, LessonTable} from "../lesson-table";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../service/http.service";

@Component({
  selector: 'app-choice-table',
  templateUrl: './choice-table.component.html',
  styleUrl: './choice-table.component.css'
})
export class ChoiceTableComponent implements OnChanges {
  readonly maxHours = 15;

  @Input() choiceResponse!: ChoiceResponse;
  @Input() selectable = true;
  @Output() selectedTapeOutput = new EventEmitter<TapeClassResponse>();

  lessons: LessonTable[] = [];
  dataSource!: MatTableDataSource<LessonTable>;
  displayedColumns: string[];
  tapeClassResponses!: TapeClassResponse[];
  selectedTape?: TapeClassResponse;

  constructor(private httpService: HttpService) {
    this.loadTapes();
    this.displayedColumns = ['Stunde', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag'];
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log("CHANGES");
    this.updateTable();
  }

  private loadTapes() {
    this.httpService.get<TapeClassResponse[]>(`/api/student/tapeClasses`, response => {
      this.tapeClassResponses = response;

      this.generateTable();
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

    this.tapeClassResponses?.forEach(tape => {
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
      this.selectedTape = tapeClass;
    }

    this.selectedTapeOutput.emit(this.selectedTape);
  }
}
