import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserMessageResponse} from "../../common.response";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";
import {HeroComponent} from "../../hero/hero.component";
import {MatFormField, MatHint, MatInput, MatLabel} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-message',
  templateUrl: './create-message.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    HeroComponent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatHint,
    MatSelect,
    MatButton,
    MatOption
  ],
  styleUrl: './create-message.component.css'
})
export class CreateMessageComponent implements OnInit {
  createMessageForm: FormGroup;
  users?: UserMessageResponse[];

  constructor(private formBuilder: FormBuilder, private httpService: HttpService, private router: Router,
              private cdr: ChangeDetectorRef) {
    this.createMessageForm = this.formBuilder.group({
      title: ['', Validators.required, Validators.max(100)],
      message: ['', Validators.required, Validators.max(1000)],
      addressee: [[], Validators.required],
    })
  }

  ngOnInit(): void {
    this.httpService.get<UserMessageResponse[]>('/api/common/users', response => {
      this.users = response;
      this.cdr.detectChanges();
    });
  }

  createMessage() {
    this.httpService.post<undefined>('/api/common/message', this.getCreateMessageRequest(), response => {
      this.router.navigate(['/messages'])
    });
  }

  private getCreateMessageRequest() {
    return {
      title: this.createMessageForm.get('title')?.value,
      message: this.createMessageForm.get('message')?.value,
      addresseeIds: this.createMessageForm.get('addressee')?.value.map(Number),
    }
  }
}
