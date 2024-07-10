import {Component, OnInit} from '@angular/core';
import {TeacherResponse} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SelectionModel} from "@angular/cdk/collections";
import {MatDialog} from "@angular/material/dialog";
import {TeacherCsvImportDialogComponent} from "./teacher-csv-import-dialog/teacher-csv-import-dialog.component";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

@Component({
  selector: 'app-show-teachers',
  templateUrl: './show-teachers.component.html',
  styleUrls: ['./show-teachers.component.css']
})
export class ShowTeachersComponent implements OnInit {
  teacherResponses!: TeacherResponse[];
  dataSource!: MatTableDataSource<TeacherResponse>;
  displayedColumns: string[];
  selection = new SelectionModel<TeacherResponse>(true, []);

  loadedTeachers = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private matDialog: MatDialog
  ) {
    this.displayedColumns = ['Auswählen', 'Kürzel', 'Nutzername', 'Vorname', 'Nachname', 'Generiertes Passwort', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadTeachers();
  }

  private loadTeachers() {
    this.httpService.get<TeacherResponse[]>('/api/admin/teachers', response => {
      this.teacherResponses = response;
      this.dataSource = new MatTableDataSource(this.teacherResponses);
      this.loadedTeachers = true;
    });
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = ($event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteTeacher(teacherId: number) {
    this.httpService.delete<TeacherResponse[]>(`api/admin/teacher?teacherId=${teacherId}`, response => {
      this.teacherResponses = response;
      this.dataSource = new MatTableDataSource(this.teacherResponses);
      this.snackBar.open('Lehrer wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  createTeacher() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  editTeacher(teacherId: number) {
    this.router.navigate(['edit', teacherId], {relativeTo: this.route});
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

  deleteTeachers() {

    this.httpService.delete<TeacherResponse[]>(`api/admin/teachers`, response => {
      this.teacherResponses = response;
      this.dataSource = new MatTableDataSource(this.teacherResponses);
      this.selection.clear();
      this.snackBar.open('Lehrer wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteTeachersRequest());
  }

  private getDeleteTeachersRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(teacher => ids.push(teacher.teacherId));

    return ids;
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

  private getPasswordResetRequests() {
    const ids: { userId: number }[] = [];

    this.selection.selected.forEach(student => ids.push({userId: student.userId}));

    return ids;
  }

  openDialog() {
    const dialogReference = this.matDialog.open(TeacherCsvImportDialogComponent);

    dialogReference.afterClosed().subscribe(result => {
      if (result) {
        this.httpService.post<TeacherResponse[]>('/api/admin/csvTeachers', result, response => {
          this.teacherResponses = response;
          this.dataSource = new MatTableDataSource(this.teacherResponses);
        });
      }
    });
  }


  exportTeachers() {
    if (this.teacherResponses!.length > 0) {
      const doc = new jsPDF();

      const head = ['Nutzername', 'Vorname', 'Nachname', 'Generiertes Password'];
      const info: {}[] = [];
      this.dataSource.filteredData.forEach(teacher =>
        info.push([teacher.username, teacher.firstname, teacher.surname, teacher.generatedPassword]));

      autoTable(doc, {
        head: [head],
        body: info,
      });

      doc.save(`Lehrer.pdf`);
    }
  }
}
