import {Component, OnInit} from '@angular/core';
import {ResultResponse, SubjectAreaResponse, SubjectAreaResponses} from "../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-show-subject-areas',
  templateUrl: './show-subject-areas.component.html',
  styleUrls: ['./show-subject-areas.component.css']
})
export class ShowSubjectAreasComponent implements OnInit {
  subjectAreaResponses!: SubjectAreaResponses;
  dataSource!: MatTableDataSource<SubjectAreaResponse>;
  displayedColumns: string[];


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
}
