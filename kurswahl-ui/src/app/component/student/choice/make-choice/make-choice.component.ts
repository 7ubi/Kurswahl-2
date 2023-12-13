import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {TapeClassResponse} from "../../stundet.responses";
import {LessonTable} from "../../home-page/lesson-table";

@Component({
  selector: 'app-make-choice',
  templateUrl: './make-choice.component.html',
  styleUrl: './make-choice.component.css'
})
export class MakeChoiceComponent implements OnInit {
  readonly maxChoices: number = 2;
  readonly maxHours = 15;

  choiceNumber: number | null;
  dataSource!: MatTableDataSource<LessonTable>;
  displayedColumns: string[];
  tapeClassResponses!: TapeClassResponse[];

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
    this.httpService.get<TapeClassResponse[]>(`/api/student/choice`, response => {
      this.tapeClassResponses = response;
      this.generateTable();
    });
  }

  private generateTable() {
    let lessons: LessonTable[] = [];
    for (let i = 1; i <= this.maxHours; i++) {
      let lesson: LessonTable = {
        hour: i,
        monday: null,
        tuesday: null,
        wednesday: null,
        thursday: null,
        friday: null,
      };
      lessons.push(lesson)
    }

    this.tapeClassResponses.forEach(tape => {
      tape.lessonResponses.forEach(lesson => {
        switch (lesson.day) {
          case 0:
            lessons[lesson.hour].monday = tape;
            break;
          case 1:
            lessons[lesson.hour].tuesday = tape;
            break;
          case 2:
            lessons[lesson.hour].wednesday = tape;
            break;
          case 3:
            lessons[lesson.hour].thursday = tape;
            break;
          case 4:
            lessons[lesson.hour].friday = tape;
            break;
        }
      });
    });

    this.dataSource = new MatTableDataSource(lessons);
  }

  getClassForCell(day: number, hour: number, element?: TapeClassResponse): string {
    let elementClass: string = '';

    if (this.selectedTape) {
      elementClass += 'day';
    }

    if (element && element == this.selectedTape) {
      elementClass += ' selected-tape'
    } else if (element) {
      elementClass += ' taken';
    }

    return elementClass;
  }

  selectLesson(day: number, hour: number) {
  }
}
