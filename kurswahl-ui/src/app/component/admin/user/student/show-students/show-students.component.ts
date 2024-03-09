import {Component, OnInit} from '@angular/core';
import {StudentClassResponse, StudentResponse} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SelectionModel} from "@angular/cdk/collections";
import {MatDialog} from "@angular/material/dialog";
import {CsvImportDialogComponent} from "./csv-import-dialog/csv-import-dialog.component";
import autoTable from "jspdf-autotable";
import jsPDF from "jspdf";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-show-students',
  templateUrl: './show-students.component.html',
  styleUrls: ['./show-students.component.css']
})
export class ShowStudentsComponent implements OnInit {
  studentResponses!: StudentResponse[];
  dataSource!: MatTableDataSource<StudentResponse>;
  displayedColumns: string[];

  studentClassResponses?: StudentClassResponse[];
  studentClassFilter: FormGroup;
  selectedStudentClass?: StudentClassResponse;

  selection = new SelectionModel<StudentResponse>(true, []);
  loadedStudents = false;

  file: string | null = null;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private matDialog: MatDialog,
    private formBuilder: FormBuilder
  ) {
    this.displayedColumns = ['Auswählen', 'Nutzername', 'Vorname', 'Nachname', 'Klasse', 'Generiertes Passwort', 'Aktionen'];

    this.studentClassFilter = this.formBuilder.group({
      name: ['']
    });
  }

  ngOnInit(): void {
    this.httpService.get<StudentClassResponse[]>('/api/admin/studentClasses', response => {
      this.studentClassResponses = response;
    });

    this.loadStudents();
  }

  applySearch($event: KeyboardEvent) {
    const filterValue = ($event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  applyFilter() {
    const filterValue = this.studentClassFilter.get('name')?.value;
    if (filterValue !== '') {
      this.dataSource = new MatTableDataSource(this.studentResponses!
        .filter(value => value.studentClassResponse.studentClassId === Number(filterValue)));
      this.selectedStudentClass = this.studentClassResponses?.find(studentClass =>
        studentClass.studentClassId === Number(filterValue));
    } else {
      this.dataSource = new MatTableDataSource(this.studentResponses);
      this.selectedStudentClass = undefined;
    }
  }

  createStudent(): void {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  deleteStudent(studentId: number) {
    this.httpService.delete<StudentResponse[]>(`api/admin/student?studentId=${studentId}`, response => {
      this.studentResponses = response;
      this.dataSource = new MatTableDataSource(this.studentResponses);
      this.snackBar.open('Schüler wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  editStudent(studentId: number) {
    this.router.navigate(['edit', studentId], {relativeTo: this.route});
  }

  resetPassword(userId: number) {
    this.httpService.put<undefined>('api/auth/resetPassword', {userId: userId}, response => {
      this.snackBar.open('Passwort wurde zurück gesetzt.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
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

  resetPasswords() {
    this.httpService.put<undefined>('api/auth/resetPasswords', this.getPasswordResetRequests(), response => {
      this.selection.clear();
      this.snackBar.open('Passwörter wurden zurück gesetzt.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  deleteStudents() {

    this.httpService.delete<StudentResponse[]>(`api/admin/students`, response => {
      this.studentResponses = response;
      this.dataSource = new MatTableDataSource(this.studentResponses);
      this.selection.clear();
      this.snackBar.open('Schüler wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteStudentsRequest());
  }

  private loadStudents() {
    this.httpService.get<StudentResponse[]>('/api/admin/students', response => {
      this.studentResponses = response;
      this.dataSource = new MatTableDataSource(this.studentResponses);
      this.loadedStudents = true;
    });
  }

  private getPasswordResetRequests() {
    const ids: { userId: number }[] = [];

    this.selection.selected.forEach(student => ids.push({userId: student.userId}));

    return ids;
  }

  private getDeleteStudentsRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(student => ids.push(student.studentId));

    return ids;
  }

  openDialog() {
    const dialogReference = this.matDialog.open(CsvImportDialogComponent);

    dialogReference.afterClosed().subscribe(result => {
      if (result) {
        this.httpService.post<StudentResponse[]>('/api/admin/csvStudents', result, response => {
          this.studentResponses = response;
          this.dataSource = new MatTableDataSource(this.studentResponses);
        });
      }
    });
  }

  exportStudents() {
    if (this.studentResponses!.length > 0) {
      const doc = new jsPDF();

      const head = ['Klasse', 'Vorname', 'Nachname', 'Generiertes Password'];
      const info: {}[] = [];
      this.dataSource.filteredData.forEach(student =>
        info.push([student.studentClassResponse.name, student.firstname, student.surname, student.generatedPassword]));

      autoTable(doc, {
        head: [head],
        body: info,
      });

      doc.save(`Schueler${this.selectedStudentClass ? this.selectedStudentClass.name : ''}.pdf`);
    }
  }
}
