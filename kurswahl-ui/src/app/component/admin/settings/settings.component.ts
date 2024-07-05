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
      critical: ['', [Validators.required, Validators.min(0)]],
      choiceOpen: ['', [Validators.required]],
      resultOpen11: ['', [Validators.required]],
      resultOpen12: ['', [Validators.required]]
    });

    this.httpService.get<ClassSizeSettingResponse>('/api/admin/settings', response => {
      this.classSizeSettingResponse = response;
      this.settingsForm.controls['warning'].setValue(this.classSizeSettingResponse.classSizeWarning);
      this.settingsForm.controls['critical'].setValue(this.classSizeSettingResponse.classSizeCritical);
      this.settingsForm.controls['choiceOpen'].setValue(this.classSizeSettingResponse.choiceOpen);
      this.settingsForm.controls['resultOpen11'].setValue(this.classSizeSettingResponse.resultOpen11);
      this.settingsForm.controls['resultOpen12'].setValue(this.classSizeSettingResponse.resultOpen12);
    });
  }

  onChangeInput() {
    console.log(this.settingsForm.get('choiceOpen')?.value, this.classSizeSettingResponse?.choiceOpen)
    this.hasChanges = (this.settingsForm.get('warning')?.value !== this.classSizeSettingResponse?.classSizeWarning
        || this.settingsForm.get('critical')?.value !== this.classSizeSettingResponse?.classSizeCritical
        || this.settingsForm.get('choiceOpen')?.value !== this.classSizeSettingResponse?.choiceOpen
        || this.settingsForm.get('resultOpen11')?.value !== this.classSizeSettingResponse?.resultOpen11
        || this.settingsForm.get('resultOpen12')?.value !== this.classSizeSettingResponse?.resultOpen12)
      && this.settingsForm.valid;
  }

  saveSettings() {
    this.httpService.put<ClassSizeSettingResponse>('/api/admin/settings', this.getEditClassSizeRequest(),
      response => {
        this.classSizeSettingResponse = response;
        this.hasChanges = false;
      });
  }

  private getEditClassSizeRequest() {
    return {
      classSizeWarning: this.settingsForm.get('warning')?.value,
      classSizeCritical: this.settingsForm.get('critical')?.value,
      choiceOpen: this.settingsForm.get('choiceOpen')?.value,
      resultOpen11: this.settingsForm.get('resultOpen11')?.value,
      resultOpen12: this.settingsForm.get('resultOpen12')?.value
    };
  }
}
