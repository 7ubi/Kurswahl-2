import {Component, OnInit} from '@angular/core';
import {SubjectAreaResponse, SubjectResponse} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Sort} from "@angular/material/sort";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-show-subjects',
  templateUrl: './show-subjects.component.html',
  styleUrls: ['./show-subjects.component.css']
})
export class ShowSubjectsComponent implements OnInit {
  subjectResponses!: SubjectResponse[];
  dataSource!: MatTableDataSource<SubjectResponse>;
  displayedColumns: string[];
  subjectAreaResponses?: SubjectAreaResponse[];
  subjectAreaFilter: FormGroup;

  lastSort: Sort | null = null;
  selection = new SelectionModel<SubjectResponse>(true, []);

  loadedSubjects = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) {
    this.displayedColumns = ['Auswählen', 'Name', 'Fachbereich', 'Aktionen'];

    this.subjectAreaFilter = this.formBuilder.group({
      name: ['']
    });
  }

  ngOnInit(): void {
    this.loadSubjectAreas();

    this.httpService.get<SubjectAreaResponse[]>('/api/admin/subjectAreas', response => {
      this.subjectAreaResponses = response;
      this.loadedSubjects = true;
    });
  }

  private loadSubjectAreas() {
    this.httpService.get<SubjectResponse[]>('/api/admin/subjects', response => {
      this.setDataSource(response);
    });
  }

  private setDataSource(response: SubjectResponse[]) {
    this.subjectResponses = response;
    this.dataSource = new MatTableDataSource(this.subjectResponses);
    if (this.lastSort) {
      this.sortData(this.lastSort);
    } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    }
  }

  applyFilter() {
    const filterValue = this.subjectAreaFilter.get('name')?.value;
    if (filterValue !== '') {
      this.dataSource = new MatTableDataSource(this.subjectResponses
        .filter(value => value.subjectAreaResponse.subjectAreaId === Number(filterValue)));
    } else {
      this.dataSource = new MatTableDataSource(this.subjectResponses);
    }
  }

  createSubject() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  deleteSubject(subjectId: number) {
    this.httpService.delete<SubjectResponse[]>(`api/admin/subject?subjectId=${subjectId}`, response => {
      this.setDataSource(response)
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
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.subjectResponses);
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

  editSubject(subjectId: number) {
    this.router.navigate(['edit', subjectId], {relativeTo: this.route});
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

  deleteSubjects() {
    this.httpService.delete<SubjectResponse[]>(`api/admin/subjects`, response => {
      this.setDataSource(response)
      this.selection.clear();
      this.snackBar.open('Fächer wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteSubjectsRequest());
  }

  private getDeleteSubjectsRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(subject => ids.push(subject.subjectId));

    return ids;
  }
}
