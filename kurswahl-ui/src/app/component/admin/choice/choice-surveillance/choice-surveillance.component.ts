import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
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
import {ChoiceSurveillanceResponse, StudentClassResponse} from "../../admin.responses";
import {HttpService} from "../../../../service/http.service";
import {MatSort, Sort} from "@angular/material/sort";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {HeroComponent} from "../../../common/hero/hero.component";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-choice-surveillance',
  templateUrl: './choice-surveillance.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
    MatInput,
    MatTable,
    MatSort,
    MatColumnDef,
    MatHeaderCellDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatIcon,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatProgressSpinner
  ],
  styleUrl: './choice-surveillance.component.css'
})
export class ChoiceSurveillanceComponent implements OnInit {

  choiceSurveillanceResponses?: ChoiceSurveillanceResponse[];
  dataSource!: MatTableDataSource<ChoiceSurveillanceResponse>;
  displayedColumns: string[];

  studentClassResponses?: StudentClassResponse[];
  studentClassFilter: FormGroup;

  lastSort: Sort | null = null;

  loadedChoice = false;

  constructor(
    private httpService: HttpService,
    private formBuilder: FormBuilder,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.displayedColumns = ['Schüler', 'Gewählt', 'Wahlbedingungen erfüllt'];

    this.studentClassFilter = this.formBuilder.group({
      name: ['']
    });
  }

  ngOnInit(): void {
    this.httpService.get<ChoiceSurveillanceResponse[]>('/api/admin/choiceSurveillance',
      response => {
        this.choiceSurveillanceResponses = response
        this.dataSource = new MatTableDataSource(this.choiceSurveillanceResponses);

        this.dataSource.filterPredicate = (data: ChoiceSurveillanceResponse, filter: string) => {
          return (`${data.studentSurveillanceResponse.firstname} ${data.studentSurveillanceResponse.surname}`)
            .toLocaleLowerCase().includes(filter) || (`${data.studentSurveillanceResponse.surname}`)
            .toLocaleLowerCase().includes(filter);
        }

        this.loadedChoice = true;
        this.cdr.detectChanges();
      });

    this.httpService.get<StudentClassResponse[]>('/api/admin/studentClasses', response => {
      this.studentClassResponses = response;
      this.cdr.detectChanges();
    });
  }

  applySearch($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  sortData(sort: Sort) {
    this.lastSort = sort;
    if (!sort.active || sort.direction === '') {
      this.dataSource = new MatTableDataSource(this.choiceSurveillanceResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.studentSurveillanceResponse.firstname + a.studentSurveillanceResponse.surname,
            b.studentSurveillanceResponse.firstname + b.studentSurveillanceResponse.surname, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  applyFilter() {
    const filterValue = this.studentClassFilter.get('name')?.value;
    if (filterValue !== '') {
      this.dataSource = new MatTableDataSource(this.choiceSurveillanceResponses!
        .filter(value => value.studentSurveillanceResponse.studentClassId === Number(filterValue)));
    } else {
      this.dataSource = new MatTableDataSource(this.choiceSurveillanceResponses);
    }
  }

  goToAssignChoice(studentId: number, year: number) {
    this.router.navigate(['/admin', 'assignChoices', year, studentId]);
  }
}
