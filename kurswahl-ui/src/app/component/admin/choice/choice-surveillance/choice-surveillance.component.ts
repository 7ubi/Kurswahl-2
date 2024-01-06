import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {ChoiceSurveillanceResponse} from "../../admin.responses";
import {HttpService} from "../../../../service/http.service";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-choice-surveillance',
  templateUrl: './choice-surveillance.component.html',
  styleUrl: './choice-surveillance.component.css'
})
export class ChoiceSurveillanceComponent implements OnInit {

  choiceSurveillanceResponses?: ChoiceSurveillanceResponse[];
  dataSource!: MatTableDataSource<ChoiceSurveillanceResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;

  constructor(
    private httpService: HttpService
  ) {
    this.displayedColumns = ['Schüler', 'Gewählt', 'Wahlbedingungen erfüllt'];

  }

  ngOnInit(): void {
    this.httpService.get<ChoiceSurveillanceResponse[]>('/api/admin/choiceSurveillance',
      response => {
        this.choiceSurveillanceResponses = response
        this.dataSource = new MatTableDataSource(this.choiceSurveillanceResponses);
      });
  }

  applySearch($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  sortData(sort: Sort) {
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.choiceSurveillanceResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.studentSurveillanceResponse.firstname + a.studentSurveillanceResponse.surname,
            b.studentSurveillanceResponse.firstname + b.studentSurveillanceResponse.surname, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }
}
