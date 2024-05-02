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
        this.dataSources.push(new TeacherClassDataSource(teacherClass.name,
          new MatTableDataSource(teacherClass.teacherClassStudentResponses)))
      });
      this.loadedClasses = true;
      console.log(this.teacherClassResponses)
      console.log(this.dataSources);
    });
  }

  sortData($event: Sort) {

  }
}

export class TeacherClassDataSource {

  name: string;
  dataSource: MatTableDataSource<TeacherClassStudentResponse>;

  constructor(name: string, dataSource: MatTableDataSource<TeacherClassStudentResponse>) {
    this.name = name;
    this.dataSource = dataSource;
  }
}
