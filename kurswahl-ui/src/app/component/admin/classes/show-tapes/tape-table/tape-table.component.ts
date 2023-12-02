import {Component, Input, numberAttribute, OnInit} from '@angular/core';
import {ResultResponse, TapeResponse, TapeResponses} from "../../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-tape-table',
  templateUrl: './tape-table.component.html',
  styleUrls: ['./tape-table.component.css']
})
export class TapeTableComponent implements OnInit {
  @Input({required: true, transform: numberAttribute}) year!: number;

  tapeResponses!: TapeResponses;
  dataSource!: MatTableDataSource<TapeResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Name', 'LK', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadTapes();
  }

  private loadTapes() {
    this.httpService.get<TapeResponses>(`/api/admin/tapes?year=${this.year}`, response => {
      this.tapeResponses = response;

      this.dataSource = new MatTableDataSource(this.tapeResponses.tapeResponses);
      if (this.lastSort) {
        this.sortData(this.lastSort);
      } else {
        this.dataSource.data
          = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
      }
    });
  }

  deleteTape(tapeId: number) {
    this.httpService.delete<ResultResponse>(`api/admin/tape?tapeId=${tapeId}`, response => {
      this.loadTapes();
      this.snackBar.open('Band wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  applySearch($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  sortData(sort: Sort) {
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.tapeResponses.tapeResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        case 'year':
          return this.compare(a.year, b.year, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  editTape(subjectId: number) {
    this.router.navigate(['edit', subjectId], {relativeTo: this.route});
  }

  assignLesson() {
    this.router.navigate(['admin', 'lessons', this.year]);
  }
}
