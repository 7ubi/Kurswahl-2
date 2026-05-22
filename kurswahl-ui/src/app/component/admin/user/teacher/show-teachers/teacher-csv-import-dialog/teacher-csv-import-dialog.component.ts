import {Component} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-teacher-csv-import-dialog',
  templateUrl: './teacher-csv-import-dialog.component.html',
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  styleUrl: './teacher-csv-import-dialog.component.css'
})
export class TeacherCsvImportDialogComponent {

  fileText: string | null = null;
  fileName: string | null = null;

  async importDataFromCSV($event: Event) {
    const file = ($event.target as HTMLInputElement)!.files![0];
    this.fileText = await file.text();
    this.fileName = file.name;
  }
}
