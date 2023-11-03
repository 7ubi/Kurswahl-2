import {Component} from '@angular/core';
import {ResultResponse, StudentClassResponse, StudentClassResponses} from "../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-show-student-classes',
  templateUrl: './show-student-classes.component.html',
  styleUrls: ['./show-student-classes.component.css']
})
export class ShowStudentClassesComponent {
  studentClassResponses!: StudentClassResponses;
  dataSource!: MatTableDataSource<StudentClassResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Name', 'Lehrer', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadSubjectAreas();
  }

  private loadSubjectAreas() {
    this.httpService.get<StudentClassResponses>('/api/admin/studentClasses', response => {
      this.studentClassResponses = response;
      this.dataSource = new MatTableDataSource(this.studentClassResponses.studentClassResponses);
      console.log(this.dataSource)
      if (this.lastSort) {
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
    this.httpService.delete<ResultResponse>(`api/admin/subjectArea?subjectAreaId=${subjectAreaId}`, response => {
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
      this.dataSource = new MatTableDataSource(this.studentClassResponses.studentClassResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        case 'teacher':
          return this.compare(a.teacher.abbreviation, b.teacher.abbreviation, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }
}
