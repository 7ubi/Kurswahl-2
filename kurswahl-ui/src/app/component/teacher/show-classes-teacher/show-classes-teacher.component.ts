import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {TeacherClassResponse, TeacherClassStudentResponse} from "../teacher.response";
import {HttpService} from "../../../service/http.service";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-show-classes-teacher',
  templateUrl: './show-classes-teacher.component.html',
  styleUrl: './show-classes-teacher.component.css'
})
export class ShowClassesTeacherComponent implements OnInit {
  teacherClassResponses: TeacherClassResponse[] = [];
  dataSources: TeacherClassDataSource[] = [];
  displayedColumns: string[];
  loadedClasses = false;

  constructor(private httpService: HttpService) {
    this.displayedColumns = ['Nachname', 'Vorname', 'Klasse'];
  }

  ngOnInit(): void {
    this.httpService.get<TeacherClassResponse[]>('/api/teacher/classes', response => {
      this.teacherClassResponses = response;
      this.teacherClassResponses.forEach(teacherClass => {
        console.log(teacherClass.teacherClassStudentResponses);
        this.dataSources.push(new TeacherClassDataSource(teacherClass.name, teacherClass.tapeName, teacherClass.year,
          new MatTableDataSource(teacherClass.teacherClassStudentResponses)))
      });
      this.loadedClasses = true;
      console.log(this.teacherClassResponses)
      console.log(this.dataSources);
    });
  }

  sortData(sort: Sort, dataSource: MatTableDataSource<TeacherClassStudentResponse>) {
    dataSource.data = dataSource.data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'surname':
          return this.compare(a.surname, b.surname, isAsc);
        case 'firstname':
          return this.compare(a.firstname, b.firstname, isAsc);
        case 'studentClass':
          return this.compare(a.studentClassName, b.studentClassName, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }
}

export class TeacherClassDataSource {

  name: string;
  tapeName: string;
  year: number;
  dataSource: MatTableDataSource<TeacherClassStudentResponse>;

  constructor(name: string, tapeName: string, year: number, dataSource: MatTableDataSource<TeacherClassStudentResponse>) {
    this.name = name;
    this.tapeName = tapeName;
    this.year = year;
    this.dataSource = dataSource;
  }
}
