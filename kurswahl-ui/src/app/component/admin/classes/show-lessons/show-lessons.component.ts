import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {TapeResponses} from "../../../../app.responses";
import {HttpService} from "../../../../service/http.service";
import {LessonsTable} from "./lessons-table";

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
        monday: this.tapeResponses.tapeResponses.at(0),
        tuesday: null,
        wednesday: null,
        thursday: null,
        friday: null,
      };
      lessons.push(lesson)
    }
    this.dataSource = new MatTableDataSource(lessons);
  }
}
