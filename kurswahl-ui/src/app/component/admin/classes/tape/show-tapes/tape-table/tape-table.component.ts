import {Component, Input, numberAttribute, OnInit} from '@angular/core';
import {TapeResponse} from "../../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-tape-table',
  templateUrl: './tape-table.component.html',
  styleUrls: ['./tape-table.component.css']
})
export class TapeTableComponent implements OnInit {
  @Input({required: true, transform: numberAttribute}) year!: number;

  tapeResponses!: TapeResponse[];
  dataSource!: MatTableDataSource<TapeResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;
  selection = new SelectionModel<TapeResponse>(true, []);

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Auswählen', 'Name', 'LK', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadTapes();
  }

  private loadTapes() {
    this.httpService.get<TapeResponse[]>(`/api/admin/tapes?year=${this.year}`, response => {
      this.setDataSource(response);
    });
  }

  private setDataSource(response: TapeResponse[]) {
    this.tapeResponses = response;

    this.dataSource = new MatTableDataSource(this.tapeResponses);
    if (this.lastSort) {
      this.sortData(this.lastSort);
    } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    }
  }

  deleteTape(tapeId: number) {
    this.httpService.delete<TapeResponse[]>(`api/admin/tape?tapeId=${tapeId}`, response => {
      this.setDataSource(response)
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
      this.dataSource = new MatTableDataSource(this.tapeResponses);
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

  editTape(tapeId: number) {
    this.router.navigate(['edit', tapeId], {relativeTo: this.route});
  }

  assignLesson() {
    this.router.navigate(['admin', 'lessons', this.year]);
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.filteredData.length;
    return numSelected >= numRows;
  }

  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.filteredData);
  }


  deleteTapes() {
    this.httpService.delete<TapeResponse[]>(`api/admin/tapes`, response => {
      this.setDataSource(response);
      this.selection.clear();
      this.snackBar.open('Bänder wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteTapesRequest());
  }

  private getDeleteTapesRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(tape => ids.push(tape.tapeId));

    return ids;
  }
}
