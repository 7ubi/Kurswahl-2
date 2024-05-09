import {Component, Input, OnInit} from '@angular/core';
import {StudentChoiceResponse, StudentSurveillanceResponse} from "../../../admin.responses";
import {MatTableDataSource} from '@angular/material/table';
import {Sort} from "@angular/material/sort";
import {HttpService} from "../../../../../service/http.service";
import {AssignChoiceComponent} from "../assign-choice.component";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-class-students-table',
  templateUrl: './class-students-table.component.html',
  styleUrl: './class-students-table.component.css'
})
export class ClassStudentsTableComponent implements OnInit {
  @Input() studentSurveillanceResponses?: StudentSurveillanceResponse[];
  @Input() parent!: AssignChoiceComponent;
  dataSourceClassStudents!: MatTableDataSource<StudentSurveillanceResponse>;
  displayedColumnsClassStudents: string[];

  selection = new SelectionModel<StudentSurveillanceResponse>(true, []);

  constructor(private httpService: HttpService) {
    this.displayedColumnsClassStudents = ['Auswählen', 'Vorname', 'Nachname'];
  }

  ngOnInit(): void {
    this.studentSurveillanceResponses?.sort((a, b) =>
      this.compare(a.firstname, b.firstname, true));
    this.dataSourceClassStudents = new MatTableDataSource<StudentSurveillanceResponse>(this.studentSurveillanceResponses);
  }

  sortData(sort: Sort) {
    this.dataSourceClassStudents.data = this.dataSourceClassStudents.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'firstname':
          return this.compare(a.firstname, b.firstname, isAsc);
        case 'surname':
          return this.compare(a.surname, b.surname, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  openChoice(studentId: any) {
    this.parent.loadedChoice = false;
    this.parent.studentChoice = undefined;
    this.parent.choiceTables = [];
    this.parent.dataSourceChoiceTable = undefined;
    this.httpService.get<StudentChoiceResponse>(`/api/admin/studentChoices?studentId=${studentId}`,
      response => {
        if (this.parent.year === response.year) {
          this.parent.studentChoice = response;
          this.parent.loadedChoice = true;

          this.parent.generateChoiceTable();
        }
      });
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSourceClassStudents.filteredData.length;
    return numSelected >= numRows;
  }

  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSourceClassStudents.filteredData);
    this.parent.components.toArray().forEach(component => {
      if (component !== this) {
        component.selection.clear();
      }
    });
  }

  changeSelection($event: MouseEvent) {
    $event.stopPropagation();
    this.parent.components.toArray().forEach(component => {
      if (component !== this) {
        component.selection.clear();
      }
    });
  }
}
