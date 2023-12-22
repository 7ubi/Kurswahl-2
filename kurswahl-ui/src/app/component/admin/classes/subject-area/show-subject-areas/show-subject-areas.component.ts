import {Component, OnInit} from '@angular/core';
import {SubjectAreaResponse, SubjectAreaResponses} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-show-subject-areas',
  templateUrl: './show-subject-areas.component.html',
  styleUrls: ['./show-subject-areas.component.css']
})
export class ShowSubjectAreasComponent implements OnInit {
  subjectAreaResponses!: SubjectAreaResponses;
  dataSource!: MatTableDataSource<SubjectAreaResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Name', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadSubjectAreas();
  }

  private loadSubjectAreas() {
    this.httpService.get<SubjectAreaResponses>('/api/admin/subjectAreas', response => {
      this.subjectAreaResponses = response;
      this.dataSource = new MatTableDataSource(this.subjectAreaResponses.subjectAreaResponses);
      if(this.lastSort) {
        this.sortData(this.lastSort);
      } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
      }
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
    this.httpService.delete<undefined>(`api/admin/subjectArea?subjectAreaId=${subjectAreaId}`, response => {
      this.loadSubjectAreas();
      this.snackBar.open('Fachbereich wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  sortData(sort: Sort) {
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.subjectAreaResponses.subjectAreaResponses);
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
}
