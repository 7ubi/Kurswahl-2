import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, numberAttribute, OnInit} from '@angular/core';
import {ClassResponse} from "../../../../admin.responses";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {MatSort, Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SelectionModel} from "@angular/cdk/collections";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-class-table',
  templateUrl: './class-table.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatFormField,
    MatLabel,
    MatMiniFabButton,
    MatIcon,
    MatTable,
    MatSort,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCheckbox,
    MatCell,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatProgressSpinner,
    MatInput
  ],
  styleUrls: ['./class-table.component.css']
})
export class ClassTableComponent implements OnInit {
  @Input({required: true, transform: numberAttribute}) year!: number;

  classResponses!: ClassResponse[];
  dataSource!: MatTableDataSource<ClassResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;
  selection = new SelectionModel<ClassResponse>(true, []);

  loadedClasses = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.displayedColumns = ['Auswählen', 'Name', 'Band', 'Fach', 'Lehrer', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadClasses();
  }

  private loadClasses() {
    this.httpService.get<ClassResponse[]>(`/api/admin/classes?year=${this.year}`, response => {
      this.setDataSource(response);
      this.loadedClasses = true;
      this.cdr.detectChanges();
    });
  }

  private setDataSource(response: ClassResponse[]) {
    this.classResponses = response;

    this.dataSource = new MatTableDataSource(this.classResponses);
    if (this.lastSort) {
      this.sortData(this.lastSort);
    } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    }
  }

  deleteClass(classId: number) {
    this.httpService.delete<ClassResponse[]>(`api/admin/class?classId=${classId}`, response => {
      this.setDataSource(response);
      this.snackBar.open('Kurs wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
      this.cdr.detectChanges();
    });
  }

  applySearch($event: KeyboardEvent) {
    const filterValue = ($event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  sortData(sort: Sort) {
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.classResponses);
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

  editClass(classId: number) {
    this.router.navigate(['edit', classId], {relativeTo: this.route});
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

  deleteClasses() {
    this.httpService.delete<ClassResponse[]>(`api/admin/classes`, response => {
      this.setDataSource(response);
      this.selection.clear();
      this.snackBar.open('Kurse wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteClassesRequest());
  }

  private getDeleteClassesRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(c => ids.push(c.classId));

    return ids;
  }
}
