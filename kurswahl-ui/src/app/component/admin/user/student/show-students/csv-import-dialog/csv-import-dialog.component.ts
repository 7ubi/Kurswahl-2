import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatTooltip} from "@angular/material/tooltip";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-csv-import-dialog',
  templateUrl: './csv-import-dialog.component.html',
  imports: [
    MatDialogContent,
    MatDialogTitle,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatTooltip,
    MatButton,
    MatDialogActions,
    MatDialogClose
  ],
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
    this.fileText = await file.text();
    this.fileName = file.name;
  }

  getRequest() {
    return {
      year: this.yearForm.get('year')?.value,
      csv: this.fileText
    }
  }
}
