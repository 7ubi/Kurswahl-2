import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-csv-import-dialog',
  templateUrl: './csv-import-dialog.component.html',
  styleUrl: './csv-import-dialog.component.css'
})
export class CsvImportDialogComponent {
  yearForm: FormGroup;

  fileText: string | null = null;
  fileName: string | null = null;

  constructor(private formBuilder: FormBuilder) {
    this.yearForm = this.formBuilder.group({
      year: ['', [Validators.required, Validators.min(11), Validators.max(12)]]
    })
  }

  async importDataFromCSV($event: Event) {
    const file = ($event.target as HTMLInputElement)!.files![0];
    this.fileText = await file.text()
    this.fileName = file.name;
  }
}
