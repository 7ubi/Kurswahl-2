import {Component, OnInit} from '@angular/core';
import {ResultResponse, StudentResponse, StudentResponses} from "../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-show-students',
  templateUrl: './show-students.component.html',
  styleUrls: ['./show-students.component.css']
})
export class ShowStudentsComponent implements OnInit {
  studentResponses!: StudentResponses;
  dataSource!: MatTableDataSource<StudentResponse>;
  displayedColumns: string[];

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Nutzername', 'Vorname', 'Nachname', 'Klasse', 'Generiertes Passwort', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadStudents();
  }

  private loadStudents() {
    this.httpService.get<StudentResponses>('/api/admin/students', response => {
      this.studentResponses = response;
      this.dataSource = new MatTableDataSource(this.studentResponses.studentResponses);
    });
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  createStudent(): void {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  deleteStudent(studentId: number) {
    this.httpService.delete<ResultResponse>(`api/admin/student?studentId=${studentId}`, response => {
      this.loadStudents();
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
    this.httpService.put<ResultResponse>('api/auth/resetPassword', {userId: userId}, response => {
      this.snackBar.open('Passwort wurde zurück gesetzt.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }
}
