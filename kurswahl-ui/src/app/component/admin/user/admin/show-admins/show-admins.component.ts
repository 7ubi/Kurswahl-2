import {Component, OnInit} from '@angular/core';
import {HttpService} from "../../../../../service/http.service";
import {AdminResponse} from "../../../admin.responses";
import {MatTableDataSource} from "@angular/material/table";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-show-admins',
  templateUrl: './show-admins.component.html',
  styleUrls: ['./show-admins.component.css']
})
export class ShowAdminsComponent implements OnInit {

  adminResponses!: AdminResponse[];
  dataSource!: MatTableDataSource<AdminResponse>;
  displayedColumns: string[];

  selection = new SelectionModel<AdminResponse>(true, []);

  loadedAdmins = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.displayedColumns = ['Auswählen', 'Nutzername', 'Vorname', 'Nachname', 'Generiertes Passwort', 'Aktionen'];
  }

  ngOnInit(): void {
    this.loadAdmins();
  }

  private loadAdmins() {
    this.httpService.get<AdminResponse[]>('/api/admin/admins', response => {
      this.adminResponses = response;
      this.dataSource = new MatTableDataSource(this.adminResponses);
      this.loadedAdmins = true;
    });
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = (event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  createAdmin(): void {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  deleteAdmin(adminId: number) {
    this.httpService.delete<AdminResponse[]>(`api/admin/admin?adminId=${adminId}`, response => {
      this.adminResponses = response;
      this.dataSource = new MatTableDataSource(this.adminResponses);
      this.snackBar.open('Admin wurde erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  editAdmin(adminId: number) {
    this.router.navigate(['edit', adminId], {relativeTo: this.route});
  }

  resetPassword(userId: number) {
    this.httpService.put<undefined>('api/auth/resetPassword', {userId: userId}, response => {
      this.snackBar.open('Passwort wurde zurück gesetzt.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
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

  resetPasswords() {
    this.httpService.put<undefined>('api/auth/resetPasswords', this.getPasswordResetRequests(), response => {
      this.selection.clear();
      this.snackBar.open('Passwörter wurden zurück gesetzt.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    });
  }

  private getPasswordResetRequests() {
    const ids: { userId: number }[] = [];

    this.selection.selected.forEach(admin => ids.push({userId: admin.userId}));

    return ids;
  }

  deleteAdmins() {
    this.httpService.delete<AdminResponse[]>(`api/admin/admins`, response => {
      this.selection.clear();
      this.adminResponses = response;
      this.dataSource = new MatTableDataSource(this.adminResponses);
      this.snackBar.open('Admins wurden erfolgreich gelöscht.', 'Verstanden', {
        horizontalPosition: "center",
        verticalPosition: "bottom",
        duration: 5000
      });
    }, () => {
    }, this.getSelectedAdminIds());
  }

  private getSelectedAdminIds() {
    const ids: number[] = [];

    this.selection.selected.forEach(admin => ids.push(admin.adminId));

    return ids;
  }
}
