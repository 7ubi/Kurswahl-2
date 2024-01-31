import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, ChildActivationEnd, Router} from "@angular/router";
import {HttpService} from "../../../../service/http.service";
import {Subscription} from "rxjs";
import {ClassChoiceResponse, ClassStudentsResponse, StudentChoiceResponse, TapeResponse} from "../../admin.responses";
import {ChoiceTable} from "./choice-table";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-assign-choice',
  templateUrl: './assign-choice.component.html',
  styleUrl: './assign-choice.component.css'
})
export class AssignChoiceComponent implements OnDestroy {
  year!: number;

  eventSubscription: Subscription;

  classes?: ClassStudentsResponse[];
  tapes?: TapeResponse[];
  studentChoice?: StudentChoiceResponse;

  dataSource!: MatTableDataSource<ChoiceTable>;
  displayedColumns: string[];

  loadedClasses = false;

  loadedChoice?: boolean = undefined;

  choiceTables?: ChoiceTable[];

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute) {

    this.displayedColumns = ['Band', '1. Wahl', '2. Wahl'];

    this.eventSubscription = router.events.subscribe(event => {
      if (event instanceof ChildActivationEnd) {
        if (this.year != Number(this.route.snapshot.paramMap.get('year'))) {
          this.year = Number(this.route.snapshot.paramMap.get('year'));

          if (this.year < 11 || this.year > 12) {
            this.router.navigate(['admin', 'admins']);
          }
          this.loadedClasses = false;
          this.loadClasses();
        }
      }
    });
  }

  loadClasses() {
    this.httpService.get<ClassStudentsResponse[]>(`/api/admin/classesStudents?year=${this.year}`,
      response => {
        this.classes = response;
        this.loadedClasses = true;
      });

    this.httpService.get<TapeResponse[]>(`/api/admin/tapes?year=${this.year}`, response => this.tapes = response)
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  openChoice(studentId: number) {
    this.loadedChoice = true;
    this.httpService.get <StudentChoiceResponse>(`/api/admin/studentChoices?studentId=${studentId}`,
      response => {
        this.studentChoice = response;
        this.loadedChoice = true;

        this.generateChoiceTable();
      });
  }

  private generateChoiceTable() {
    const firstChoice = this.studentChoice?.choiceResponses.find(choice => choice.choiceNumber === 1);
    const secondChoice = this.studentChoice?.choiceResponses.find(choice => choice.choiceNumber === 2);

    this.choiceTables = [];

    this.tapes?.forEach(tape => {
      let choiceTable = new ChoiceTable(tape);

      if (firstChoice) {
        choiceTable.firstChoice = firstChoice.classChoiceResponses.find(classChoice => classChoice.tapeId === tape.tapeId);
      }

      if (secondChoice) {
        choiceTable.secondChoice = secondChoice.classChoiceResponses.find(classChoice => classChoice.tapeId === tape.tapeId);
      }

      this.choiceTables?.push(choiceTable);
    });

    this.dataSource = new MatTableDataSource(this.choiceTables);
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
}
