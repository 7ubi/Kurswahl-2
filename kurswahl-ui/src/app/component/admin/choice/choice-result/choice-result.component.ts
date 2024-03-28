import {Component, OnDestroy} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, ChildActivationEnd, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {ChoiceResultResponse, ClassStudentsResponse} from "../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {SelectionModel} from "@angular/cdk/collections";
import {animate, state, style, transition, trigger} from "@angular/animations";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

@Component({
  selector: 'app-choice-result',
  templateUrl: './choice-result.component.html',
  animations: [
    trigger('detailExpand', [
      state('collapsed,void', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
  styleUrl: './choice-result.component.css'
})
export class ChoiceResultComponent implements OnDestroy {
  year!: number;

  eventSubscription: Subscription;

  results?: ChoiceResultResponse;

  displayedColumns: string[];
  dataSource!: MatTableDataSource<ClassStudentsResponse>;
  selection = new SelectionModel<ClassStudentsResponse>(true, []);
  expandedElement: ClassStudentsResponse[] = [];

  loadedResults = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute) {
    this.displayedColumns = ['expansion', 'Auswählen', 'Kurs', 'Lehrer', 'Band', 'Status'];

    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ChildActivationEnd) {
        if (this.year != Number(this.route.snapshot.paramMap.get('year'))) {
          this.year = Number(this.route.snapshot.paramMap.get('year'));

          if (this.year < 11 || this.year > 12) {
            this.router.navigate(['admin', 'admins']);
          }

          this.loadedResults = false;
          this.results = undefined;
          this.loadClasses();
        }
      }
    });
  }

  loadClasses() {
    this.httpService.get<ChoiceResultResponse>(`/api/admin/result?year=${this.year}`,
      response => {
        this.results = response;
        this.dataSource = new MatTableDataSource(this.results.classStudentsResponses);
        this.loadedResults = true;
      });
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  goToAssignChoice(studentId: number, year: number) {
    this.router.navigate(['/admin', 'assignChoices', year, studentId]);
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

  exportResult() {
    if (this.selection.selected.length > 0) {
      const doc = new jsPDF();

      const head = ['Vorname', 'Nachname', 'Klasse'];

      let firstElement = true;
      this.selection.selected.forEach(classStudents => {
        if (!firstElement) {
          doc.addPage();
        } else {
          firstElement = false;
        }

        const info: {}[] = [];
        classStudents.studentSurveillanceResponses.forEach(student =>
          info.push([student.firstname, student.surname, student.name]));

        autoTable(doc, {
          head: [[classStudents.name]]
        });
        autoTable(doc, {
          startY: 21,
          head: [head],
          body: info,
        });
      });

      doc.save(`Ergebnisse.pdf`);
    }
  }

  sortData(sort: Sort) {
    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        case 'teacher':
          return this.compare(a.teacherResponse.abbreviation, b.teacherResponse.abbreviation, isAsc);
        case 'tape':
          return this.compare(a.tapeName, b.tapeName, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  expandElement(element: ClassStudentsResponse) {
    const index = this.expandedElement.indexOf(element);

    if (index < 0) {
      this.expandedElement.push(element);
    } else {
      this.expandedElement.splice(index);
    }
  }
}
