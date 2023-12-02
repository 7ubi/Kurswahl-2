import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {TapeResponse, TapeResponses} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {Lesson, LessonsTable} from "./lessons-table";

@Component({
  selector: 'app-show-lessons',
  templateUrl: './show-lessons.component.html',
  styleUrls: ['./show-lessons.component.css']
})
export class ShowLessonsComponent implements OnInit {
  year: string | null;
  tapeResponses!: TapeResponses;
  dataSource!: MatTableDataSource<LessonsTable>;
  displayedColumns: string[];

  selectedTape?: TapeResponse;
  selectedLessons: Lesson[] = [];

  readonly maxHours = 15;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.year = this.route.snapshot.paramMap.get('year');

    this.displayedColumns = ['Stunde', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag'];
  }

  ngOnInit(): void {
    this.loadTapes();
  }

  private loadTapes() {
    this.httpService.get<TapeResponses>(`/api/admin/tapes?year=${this.year}`, response => {
      this.tapeResponses = response;
      this.generateTable();
    });
  }

  private generateTable() {
    let lessons: LessonsTable[] = [];
    for (let i = 1; i <= this.maxHours; i++) {
      let lesson: LessonsTable = {
        hour: i,
        monday: null,
        tuesday: null,
        wednesday: null,
        thursday: null,
        friday: null,
      };
      lessons.push(lesson)
    }

    this.tapeResponses.tapeResponses.forEach(tape => {
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

  selected(tape: TapeResponse) {
    if (this.selectedTape !== tape) {
      this.selectedTape = tape;
    } else {
      this.selectedTape = undefined;
    }
  }

  getClassForCell(day: number, hour: number, element?: TapeResponse): string {
    let elementClass: string = '';

    if (this.selectedTape) {
      elementClass += 'day';
    }

    if (element) {
      elementClass += ' taken';
    }

    this.selectedLessons.forEach(lesson => {
      if (lesson.day === day && lesson.hour === hour) {
        elementClass += ' selected';
      }
    });

    return elementClass;
  }

  selectLesson(day: number, hour: number) {
    if (this.selectedTape) {

      let alreadySelected = false

      this.selectedLessons.forEach(lesson => {
        if (lesson.day === day && lesson.hour === hour) {
          alreadySelected = true;
          const index = this.selectedLessons.indexOf(lesson, 0);
          if (index > -1) {
            this.selectedLessons.splice(index, 1);
          }
        }
      });

      if (!alreadySelected) {
        this.selectedLessons.push({day: day, hour: hour});
      }
    }
  }

  saveSelectedLessons() {

  }
}
