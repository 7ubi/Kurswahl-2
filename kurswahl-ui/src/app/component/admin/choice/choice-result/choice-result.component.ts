import {Component, OnDestroy} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, ChildActivationEnd, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {ChoiceResultResponse, ClassStudentsResponse} from "../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-choice-result',
  templateUrl: './choice-result.component.html',
  styleUrl: './choice-result.component.css'
})
export class ChoiceResultComponent implements OnDestroy {
  year!: number;

  eventSubscription: Subscription;

  results?: ChoiceResultResponse;

  displayedColumns: string[];
  dataSource!: MatTableDataSource<ClassStudentsResponse>;
  selection = new SelectionModel<ClassStudentsResponse>(true, []);
  expandedElement?: ClassStudentsResponse;
  isExpansionDetailRow = (i: number, row: Object) => row.hasOwnProperty('detailRow');

  loadedResults = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute) {
    this.displayedColumns = ['Auswählen', 'Kurs', 'Lehrer', 'Band'];

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

  }

  sortData($event: Sort) {

  }
}
