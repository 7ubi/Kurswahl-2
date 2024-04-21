import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserMessageResponse} from "../../common.response";
import {HttpService} from "../../../../service/http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-message',
  templateUrl: './create-message.component.html',
  styleUrl: './create-message.component.css'
})
export class CreateMessageComponent implements OnInit {
  createMessageForm: FormGroup;
  users?: UserMessageResponse[];

  constructor(private formBuilder: FormBuilder, private httpService: HttpService, private router: Router) {
    this.createMessageForm = this.formBuilder.group({
      title: ['', Validators.required, Validators.max(100)],
      message: ['', Validators.required, Validators.max(1000)],
      addressee: [[], Validators.required],
    })
  }

  ngOnInit(): void {
    this.httpService.get<UserMessageResponse[]>('/api/common/users', response => this.users = response);
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
