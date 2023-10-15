import {Component, OnInit} from '@angular/core';
import {
  ResultResponse,
  SubjectAreaResponses,
  SubjectResponse,
  SubjectResponses
} from "../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-show-subjects',
  templateUrl: './show-subjects.component.html',
  styleUrls: ['./show-subjects.component.css']
})
export class ShowSubjectsComponent implements OnInit {
  subjectResponses!: SubjectResponses;
  dataSource!: MatTableDataSource<SubjectResponse>;
  displayedColumns: string[];
  subjectAreaResponses?: SubjectAreaResponses;
  subjectAreaFilter: FormGroup;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) {
    this.displayedColumns = ['Name', 'Fachbereich', 'Aktionen'];

    this.subjectAreaFilter = this.formBuilder.group({
      name: ['']
    });
  }

  ngOnInit(): void {
    this.loadSubjectAreas();

    this.httpService.get<SubjectAreaResponses>('/api/admin/subjectAreas', response => {
      this.subjectAreaResponses = response;
    });
  }

  private loadSubjectAreas() {
    this.httpService.get<SubjectResponses>('/api/admin/subjects', response => {
      this.subjectResponses = response;
      this.dataSource = new MatTableDataSource(this.subjectResponses.subjectResponses);
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    });
  }

  applyFilter() {
    const filterValue = this.subjectAreaFilter.get('name')?.value;
    console.log(filterValue, filterValue !== '')
    if(filterValue !== '') {
      this.dataSource = new MatTableDataSource(this.subjectResponses.subjectResponses
        .filter(value => value.subjectAreaResponse.subjectAreaId === Number(filterValue)));
    } else {
      this.dataSource = new MatTableDataSource(this.subjectResponses.subjectResponses);
    }
  }

  createSubject() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  deleteSubject(subjectId: number) {
    this.httpService.delete<ResultResponse>(`api/admin/subject?subjectId=${subjectId}`, response => {
      this.loadSubjectAreas();
      this.snackBar.open('Fach wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  applySearch($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  sortData(sort: Sort) {
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.subjectResponses.subjectResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        case 'fachbereich':
          return this.compare(a.subjectAreaResponse.name, b.subjectAreaResponse.name, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }
}
