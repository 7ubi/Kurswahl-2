import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, ChildActivationEnd, Router} from "@angular/router";
import {HttpService} from "../../../../service/http.service";
import {Subscription} from "rxjs";
import {
  ChoiceTapeResponse,
  ClassChoiceResponse,
  ClassStudentsResponse,
  StudentChoiceResponse
} from "../../admin.responses";
import {ChoiceTable} from "./choice-table";
import {MatTableDataSource} from "@angular/material/table";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-assign-choice',
  templateUrl: './assign-choice.component.html',
  animations: [
    trigger('detailExpand', [
      state('collapsed,void', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
  styleUrl: './assign-choice.component.css'
})
export class AssignChoiceComponent implements OnDestroy {
  initialStudent: number | null = null;

  year!: number;

  eventSubscription: Subscription;

  classes?: ClassStudentsResponse[];
  tapes?: ChoiceTapeResponse[];
  selectedTape?: ChoiceTapeResponse;
  studentChoice?: StudentChoiceResponse;

  expandedElement: ClassStudentsResponse[] = [];
  dataSourceClassStudents!: MatTableDataSource<ClassStudentsResponse>;
  displayedColumnsClassStudents: string[];

  dataSourceChoiceTable?: MatTableDataSource<ChoiceTable>;
  displayedColumnsChoiceTable: string[];

  loadedClasses = false;

  loadedChoice?: boolean = undefined;

  choiceTables?: ChoiceTable[];

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute) {

    this.displayedColumnsClassStudents = ['expansion', 'Kurs', 'Lehrer', 'Band', 'Status'];
    this.displayedColumnsChoiceTable = ['Band', '1. Wahl', '2. Wahl', 'Alternative', 'Aktion'];

    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ChildActivationEnd) {
        if (this.year != Number(this.route.snapshot.paramMap.get('year'))) {
          this.year = Number(this.route.snapshot.paramMap.get('year'));

          if (this.year < 11 || this.year > 12) {
            this.router.navigate(['admin', 'admins']);
          }
          this.loadedClasses = false;
          this.studentChoice = undefined;
          this.classes = undefined;
          this.initialStudent = Number(this.route.snapshot.paramMap.get('studentId'));
          this.loadClasses();
        }
      }
    });
  }

  loadClasses() {
    this.httpService.get<ClassStudentsResponse[]>(`/api/admin/classesStudents?year=${this.year}`,
      response => {
        this.classes = response;
        this.dataSourceClassStudents = new MatTableDataSource(this.classes);
        this.loadedClasses = true;
      });

    this.httpService.get<ChoiceTapeResponse[]>(`/api/admin/choiceTapes?year=${this.year}`, response => {
      this.tapes = response;
      if (this.initialStudent) {
        this.openChoice(this.initialStudent);
      }
    });
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  openChoice(studentId: number) {
    this.loadedChoice = false;
    this.studentChoice = undefined;
    this.choiceTables = [];
    this.dataSourceChoiceTable = undefined;
    this.httpService.get <StudentChoiceResponse>(`/api/admin/studentChoices?studentId=${studentId}`,
      response => {
        if (this.year === response.year) {
          this.studentChoice = response;
          this.loadedChoice = true;

          this.generateChoiceTable();
        }
      });
  }

  private generateChoiceTable() {
    const firstChoice = this.studentChoice?.choiceResponses.find(choice => choice.choiceNumber === 1);
    const secondChoice = this.studentChoice?.choiceResponses.find(choice => choice.choiceNumber === 2);
    const alternative = this.studentChoice?.choiceResponses.find(choice => choice.choiceNumber === 3);

    this.choiceTables = [];

    this.tapes?.forEach(tape => {
      let choiceTable = new ChoiceTable(tape);

      if (firstChoice) {
        choiceTable.firstChoice = firstChoice.classChoiceResponses.find(classChoice => classChoice.tapeId === tape.tapeId);
      }

      if (secondChoice) {
        choiceTable.secondChoice = secondChoice.classChoiceResponses.find(classChoice => classChoice.tapeId === tape.tapeId);
      }

      if (alternative) {
        choiceTable.alternativeChoice = alternative.classChoiceResponses.find(classChoice => classChoice.tapeId === tape.tapeId);
      }

      this.choiceTables?.push(choiceTable);
    });

    this.dataSourceChoiceTable = new MatTableDataSource(this.choiceTables);
  }

  assignChoice(element: ClassChoiceResponse) {
    if (element && element.selected) {
      this.httpService.delete<StudentChoiceResponse>(`/api/admin/assignChoice?choiceClassId=${element.choiceClassId}`, response => {
        this.studentChoice = response;
        this.generateChoiceTable();
      });
    }

    if (element && !element.selected) {
      this.httpService.put<StudentChoiceResponse>(`/api/admin/assignChoice?choiceClassId=${element.choiceClassId}`, null, response => {
        this.studentChoice = response;
        this.generateChoiceTable();
      });
    }
  }

  assignAlternative(classId: number) {
    this.httpService.post<StudentChoiceResponse>(`/api/admin/assignChoice`, this.getAlternativeRequest(classId),
      response => {
        this.studentChoice = response;
        this.generateChoiceTable();
      });
  }

  getAlternativeRequest(classId: number) {
    return {
      studentId: this.studentChoice?.studentId,
      classId: classId
    };
  }

  selectTape(tapeId: number) {
    this.selectedTape = this.tapes?.find(tape => tape.tapeId === tapeId);
  }

  deleteAlternative(alternative: ClassChoiceResponse) {
    if (alternative) {
      this.httpService.delete<StudentChoiceResponse>(`/api/admin/alternativeChoice?choiceClassId=${alternative.choiceClassId}`, response => {
        this.studentChoice = response;
        this.generateChoiceTable();
      });
    }
  }

  sortData(sort: Sort) {
    this.dataSourceClassStudents.data = this.dataSourceClassStudents.data.sort((a, b) => {
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
