import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {ResultResponse, TapeResponse, TapeResponses} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {LessonsTable} from "./lessons-table";
import {FormControl, FormGroup} from "@angular/forms";

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

  readonly maxHours = 15;

  tapeFormGroup: FormGroup = new FormGroup({
    tapeOptions: new FormControl<Number | undefined>(undefined)
  });

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

  private loadTapes(tapeId?: number) {
    this.httpService.get<TapeResponses>(`/api/admin/tapes?year=${this.year}`, response => {
      this.tapeResponses = response;
      this.generateTable();

      if (tapeId) {
        this.selectedTape = this.tapeResponses.tapeResponses.find(tape => tape.tapeId === tapeId);
        this.tapeFormGroup.controls['tapeOptions'].setValue(this.selectedTape?.tapeId);
      }
      console.log(this.tapeFormGroup.controls['tapeOptions']);
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

    if (element && element == this.selectedTape) {
      elementClass += ' selected-tape'
    } else if (element) {
      elementClass += ' taken';
    }

    return elementClass;
  }

  selectLesson(day: number, hour: number) {
    if (this.selectedTape) {
      const tapeId = this.selectedTape.tapeId;
      let deletedLesson = false;

      this.selectedTape.lessonResponses.forEach(lesson => {
        if (lesson.day === day && lesson.hour === hour - 1) {
          this.httpService.delete<ResultResponse>(`/api/admin/lesson?lessonId=${lesson.lessonId}`,
            response => {
              this.loadTapes(tapeId);
            });
          deletedLesson = true;
        }
      });

      if (!deletedLesson) {
        this.httpService.post<ResultResponse>('/api/admin/lesson', this.getLessonRequest(day, hour - 1),
          response => {
            this.loadTapes(tapeId);
          });
      }
    }
  }

  private getLessonRequest(day: number, hour: number) {
    return {
      tapeId: this.selectedTape?.tapeId,
      day: day,
      hour: hour,
    };
  }

  compareTapeObjects(object1: any, object2: any) {
    return Number(object1) === Number(object2);
  }
}
