import {Component, OnInit} from '@angular/core';
import {StudentClassResponse} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-show-student-classes',
  templateUrl: './show-student-classes.component.html',
  styleUrls: ['./show-student-classes.component.css']
})
export class ShowStudentClassesComponent implements OnInit {
  studentClassResponses!: StudentClassResponse[];
  dataSource!: MatTableDataSource<StudentClassResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;
  selection = new SelectionModel<StudentClassResponse>(true, []);

  loadedStudentClasses = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Auswählen', 'Name', 'Lehrer', 'Jahrgang', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadStudentClasses();
  }

  private loadStudentClasses() {
    this.httpService.get<StudentClassResponse[]>('/api/admin/studentClasses', response => {
      this.setDataSource(response);
      this.loadedStudentClasses = true;
    });
  }

  private setDataSource(response: StudentClassResponse[]) {
    this.studentClassResponses = response;
    this.dataSource = new MatTableDataSource(this.studentClassResponses);

    if (this.lastSort) {
      this.sortData(this.lastSort);
    } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    }
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteStudentClass(studentClassId: number) {
    this.httpService.delete<StudentClassResponse[]>(`api/admin/studentClass?studentClassId=${studentClassId}`, response => {
      this.setDataSource(response);
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
      this.dataSource = new MatTableDataSource(this.studentClassResponses);
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

  deleteStudentClasses() {
    this.httpService.delete<StudentClassResponse[]>(`api/admin/studentClasses`, response => {
      this.setDataSource(response);
      this.selection.clear();
      this.snackBar.open('Klassen wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteStudentClassesRequest());
  }

  private getDeleteStudentClassesRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(studentClass => ids.push(studentClass.studentClassId));

    return ids;
  }
}
