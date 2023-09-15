import {Component, OnInit} from '@angular/core';
import {ResultResponse, TeacherResponse, TeacherResponses} from "../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-show-teachers',
  templateUrl: './show-teachers.component.html',
  styleUrls: ['./show-teachers.component.css']
})
export class ShowTeachersComponent implements OnInit {
  teacherResponses!: TeacherResponses;
  dataSource!: MatTableDataSource<TeacherResponse>;
  displayedColumns: string[];


  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Nutzername', 'Vorname', 'Nachname', 'Generiertes Passwort', 'Aktionen'];
  }


  ngOnInit(): void {
    this.loadTeachers();
  }

  private loadTeachers() {
    this.httpService.get<TeacherResponses>('/api/admin/teachers', response => {
      this.teacherResponses = response;
      this.dataSource = new MatTableDataSource(this.teacherResponses.teacherResponses);
    });
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteTeacher(teacherId: number) {
    this.httpService.delete<ResultResponse>(`api/admin/teacher?teacherId=${teacherId}`, response => {
      this.loadTeachers();
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
}
