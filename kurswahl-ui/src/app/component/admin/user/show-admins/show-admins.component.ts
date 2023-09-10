import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../../service/http.service";
import {AdminResponse, AdminResponses} from "../../../../app.responses";
import {MatTableDataSource} from "@angular/material/table";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-show-admins',
  templateUrl: './show-admins.component.html',
  styleUrls: ['./show-admins.component.css']
})
export class ShowAdminsComponent implements OnInit {

  adminResponses!: AdminResponses;
  dataSource!: MatTableDataSource<AdminResponse>;
  displayedColumns: string[];

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.displayedColumns = ['Nutzername', 'Vorname', 'Nachname', 'Generiertes Passwort']
  }

  ngOnInit(): void {
    this.httpService.get<AdminResponses>('/api/admin/admins', response => {
      this.adminResponses = response;
      this.dataSource = new MatTableDataSource(this.adminResponses.adminResponses);
    });
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  createAdmin(): void {
    this.router.navigate(['erstellen'], {relativeTo: this.route});
  }
}
