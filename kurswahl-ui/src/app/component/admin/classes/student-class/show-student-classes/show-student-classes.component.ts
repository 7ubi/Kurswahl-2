import {Component, OnInit} from '@angular/core';
import {StudentClassResponse, StudentClassResponses} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-show-student-classes',
  templateUrl: './show-student-classes.component.html',
  styleUrls: ['./show-student-classes.component.css']
})
export class ShowStudentClassesComponent implements OnInit {
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
    this.displayedColumns = ['Name', 'Lehrer', 'Jahrgang', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadStudentClasses();
  }

  private loadStudentClasses() {
    this.httpService.get<StudentClassResponses>('/api/admin/studentClasses', response => {
      this.studentClassResponses = response;
      this.dataSource = new MatTableDataSource(this.studentClassResponses.studentClassResponses);

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

  deleteStudentClass(studentClassId: number) {
    this.httpService.delete<undefined>(`api/admin/studentClass?studentClassId=${studentClassId}`, response => {
      this.loadStudentClasses();
      this.snackBar.open('Klasse wurde erfolgreich gelöscht.', 'Verstanden', {
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

  createStudentClass() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  editStudentClass(studentClassId: number) {
    this.router.navigate(['edit', studentClassId], {relativeTo: this.route});
  }
}
