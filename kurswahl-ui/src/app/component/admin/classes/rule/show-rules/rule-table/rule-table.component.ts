import {Component, Input, numberAttribute, OnInit} from '@angular/core';
import {RuleResponse} from "../../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {Sort} from "@angular/material/sort";
import {SelectionModel} from "@angular/cdk/collections";
import {HttpService} from "../../../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-rule-table',
  templateUrl: './rule-table.component.html',
  styleUrl: './rule-table.component.css'
})
export class RuleTableComponent implements OnInit {
  @Input({required: true, transform: numberAttribute}) year!: number;

  ruleResponses!: RuleResponse[];
  dataSource!: MatTableDataSource<RuleResponse>;
  displayedColumns: string[];

  lastSort: Sort | null = null;
  selection = new SelectionModel<RuleResponse>(true, []);

  loadedRules = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Auswählen', 'Name', 'Fächer', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadRules();
  }

  private loadRules() {
    this.httpService.get<RuleResponse[]>(`/api/admin/rules?year=${this.year}`, response => {
      this.setDataSource(response);
      this.loadedRules = true;
    });
  }

  private setDataSource(response: RuleResponse[]) {
    this.ruleResponses = response;

    this.dataSource = new MatTableDataSource(this.ruleResponses);
    if (this.lastSort) {
      this.sortData(this.lastSort);
    } else {
      this.dataSource.data
        = this.dataSource.data.sort((a, b) => this.compare(a.name, b.name, true));
    }
  }

  deleteRule(ruleId: number) {
    this.httpService.delete<RuleResponse[]>(`api/admin/rule?ruleId=${ruleId}`, response => {
      this.setDataSource(response)
      this.snackBar.open('Regel wurde erfolgreich gelöscht.', 'Verstanden', {
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
      this.dataSource = new MatTableDataSource(this.ruleResponses);
      return;
    }

    this.dataSource.data = this.dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return this.compare(a.name, b.name, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  editRule(ruleId: number) {
    this.router.navigate(['edit', ruleId], {relativeTo: this.route});
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


  deleteTapes() {
    this.httpService.delete<RuleResponse[]>(`api/admin/rules`, response => {
      this.setDataSource(response);
      this.selection.clear();
      this.snackBar.open('Regeln wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getDeleteRulesRequest());
  }

  private getDeleteRulesRequest() {
    const ids: number[] = [];

    this.selection.selected.forEach(rule => ids.push(rule.ruleId));

    return ids;
  }
}
