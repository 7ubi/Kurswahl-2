import {Component, Input, numberAttribute} from '@angular/core';
import {ClassResponse, ClassResponses, ResultResponse} from "../../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-class-table',
  templateUrl: './class-table.component.html',
  styleUrls: ['./class-table.component.css']
})
export class ClassTableComponent {
  @Input({required: true, transform: numberAttribute}) year!: number;

  classResponses!: ClassResponses;
  dataSource!: MatTableDataSource<ClassResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;

  constructor(
      private httpService: HttpService,
      private router: Router,
      private route: ActivatedRoute,
      private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Name', 'Band', 'Fach', 'Lehrer', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadClasses();
  }

  private loadClasses() {
    this.httpService.get<ClassResponses>(`/api/admin/classes?year=${this.year}`, response => {
      this.classResponses = response;
      this.classResponses.classResponses.filter(aclass => aclass.tapeResponse.year == this.year);

      this.dataSource = new MatTableDataSource(this.classResponses.classResponses);
      if (this.lastSort) {
        this.sortData(this.lastSort);
      } else {
        this.dataSource.data
            = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
      }
    });
  }

  deleteTape(tapeId: number) {
    this.httpService.delete<ResultResponse>(`api/admin/tape?tapeId=${tapeId}`, response => {
      this.loadClasses();
      this.snackBar.open('Band wurde erfolgreich gelöscht.', 'Verstanden', {
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
      this.dataSource = new MatTableDataSource(this.classResponses.classResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        case 'subject':
          return this.compare(a.subjectResponse.name, b.subjectResponse.name, isAsc);
        case 'teacher':
          return this.compare(a.teacherResponse.abbreviation, b.teacherResponse.abbreviation, isAsc);
        case 'tape':
          return this.compare(a.tapeResponse.name, b.tapeResponse.name, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  editTape(subjectId: number) {
    this.router.navigate(['edit', subjectId], {relativeTo: this.route});
  }
}
