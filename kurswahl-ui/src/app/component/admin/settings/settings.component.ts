import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpService} from "../../../service/http.service";
import {ClassSizeSettingResponse} from "../admin.responses";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  settingsForm: FormGroup;
  hasChanges: boolean = false;

  classSizeSettingResponse?: ClassSizeSettingResponse;

  constructor(
    private formBuilder: FormBuilder,
    private httpService: HttpService
  ) {
    this.settingsForm = this.formBuilder.group({
      warning: ['', [Validators.required, Validators.min(0)]],
      critical: ['', [Validators.required, Validators.min(0)]]
    });

    this.httpService.get<ClassSizeSettingResponse>('/api/admin/classSize', response => {
      this.classSizeSettingResponse = response;
      this.settingsForm.controls['warning'].setValue(this.classSizeSettingResponse.classSizeWarning);
      this.settingsForm.controls['critical'].setValue(this.classSizeSettingResponse.classSizeCritical);
    });
  }

  onChangeInput() {
    this.hasChanges = (this.settingsForm.get('warning')?.value !== this.classSizeSettingResponse?.classSizeWarning
        || this.settingsForm.get('critical')?.value !== this.classSizeSettingResponse?.classSizeCritical)
      && this.settingsForm.valid;
  }

  saveSettings() {
    this.httpService.put<ClassSizeSettingResponse>('/api/admin/classSize', this.getEditClassSizeRequest(),
      response => {
        this.classSizeSettingResponse = response;
        this.hasChanges = false;
      });
  }

  private getEditClassSizeRequest() {
    return {
      classSizeWarning: this.settingsForm.get('warning')?.value,
      classSizeCritical: this.settingsForm.get('critical')?.value,
    };
  }
}
