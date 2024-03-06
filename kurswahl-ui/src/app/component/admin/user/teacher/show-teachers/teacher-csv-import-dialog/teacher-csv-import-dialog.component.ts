import {Component} from '@angular/core';

@Component({
  selector: 'app-teacher-csv-import-dialog',
  templateUrl: './teacher-csv-import-dialog.component.html',
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
