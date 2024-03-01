import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-csv-import-dialog',
  templateUrl: './csv-import-dialog.component.html',
  styleUrl: './csv-import-dialog.component.css'
})
export class CsvImportDialogComponent {

  fileText: string | null = null;
  fileName: string | null = null;

  constructor(private dialogRef: MatDialogRef<CsvImportDialogComponent>) {
  }

  closeDialog() {
    this.dialogRef.close();
  }

  async importDataFromCSV($event: Event) {
    console.log($event)
    const file = ($event.target as HTMLInputElement)!.files![0];
    this.fileText = await file.text()
    this.fileName = file.name;
  }

  importData() {
    this.closeDialog();
  }
}
