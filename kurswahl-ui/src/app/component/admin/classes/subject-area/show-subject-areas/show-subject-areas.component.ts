import {Component, OnInit} from '@angular/core';
import {SubjectAreaResponse} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Sort} from "@angular/material/sort";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-show-subject-areas',
  templateUrl: './show-subject-areas.component.html',
  styleUrls: ['./show-subject-areas.component.css']
})
export class ShowSubjectAreasComponent implements OnInit {
  subjectAreaResponses!: SubjectAreaResponse[];
  dataSource!: MatTableDataSource<SubjectAreaResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;
  selection = new SelectionModel<SubjectAreaResponse>(true, []);

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Auswählen', 'Name', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadSubjectAreas();
  }

  private loadSubjectAreas() {
    this.httpService.get<SubjectAreaResponse[]>('/api/admin/subjectAreas', response => {
      this.setDataSource(response);
    });
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  createSubjectArea() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  deleteSubjectArea(subjectAreaId: number) {
    this.httpService.delete<SubjectAreaResponse[]>(`api/admin/subjectArea?subjectAreaId=${subjectAreaId}`, response => {
      this.setDataSource(response);
      this.snackBar.open('Fachbereich wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  private setDataSource(response: SubjectAreaResponse[]) {
    this.subjectAreaResponses = response;
    this.dataSource = new MatTableDataSource(this.subjectAreaResponses);
    if (this.lastSort) {
      this.sortData(this.lastSort);
    } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    }
  }

  sortData(sort: Sort) {
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.subjectAreaResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  editSubjectArea(subjectAreaId: number) {
    this.router.navigate(['edit', subjectAreaId], {relativeTo: this.route});
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


  deleteSubjectAreas() {
    this.httpService.delete<SubjectAreaResponse[]>(`api/admin/subjectAreas`, response => {
      this.setDataSource(response);
      this.selection.clear();
      this.snackBar.open('Fachbereiche wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteSubjectAreasRequest());
  }

  private getDeleteSubjectAreasRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(subjectArea => ids.push(subjectArea.subjectAreaId));

    return ids;
  }
}
