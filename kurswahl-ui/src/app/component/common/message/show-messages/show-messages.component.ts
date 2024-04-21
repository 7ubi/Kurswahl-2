import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MessageResponse} from "../../common.response";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup} from '@angular/forms';

export enum ShowMessageModes {
  ALL = "Empfange",
  UNREAD = "Ungelesene",
  SENT = "Gesendete"
}

@Component({
  selector: 'app-show-messages',
  templateUrl: './show-messages.component.html',
  styleUrl: './show-messages.component.css'
})
export class ShowMessagesComponent implements OnInit {

  protected readonly ShowMessageModes = ShowMessageModes;

  messageResponses?: MessageResponse[];
  unreadMessageResponses?: MessageResponse[];
  sentMessageResponses?: MessageResponse[];
  dataSource!: MatTableDataSource<MessageResponse>;
  displayedColumns: string[];
  loadedMessages: boolean = false;
  shownMessages: FormGroup;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {
    this.displayedColumns = ['Titel', 'Absender', 'Nachricht'];

    this.shownMessages = this.formBuilder.group({
      mode: [ShowMessageModes.ALL]
    });
  }

  ngOnInit(): void {
    this.httpService.get<MessageResponse[]>('/api/common/messages', response => {
      this.messageResponses = response;
      this.dataSource = new MatTableDataSource(this.messageResponses);
      this.loadedMessages = true;
    });
  }

  createMessage() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }

  applyFilter($event: KeyboardEvent) {
    const filterValue = ($event?.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  showMessage(messageId: string) {
    this.router.navigate(['/message', messageId]);
  }

  changeShownMessages() {
    const mode = this.shownMessages.get('mode')?.value;

    if (mode === ShowMessageModes.ALL) {
      this.dataSource = new MatTableDataSource(this.messageResponses);
    } else if (mode === ShowMessageModes.UNREAD) {
      if (!this.unreadMessageResponses) {
        this.unreadMessageResponses = this.messageResponses?.filter(message => !message.readMessage);
      }
      this.dataSource = new MatTableDataSource(this.unreadMessageResponses);
    } else if (mode === ShowMessageModes.SENT) {
      if (!this.sentMessageResponses) {
        this.httpService.get<MessageResponse[]>('/api/common/messages/sent', response => {
          this.sentMessageResponses = response;
          this.dataSource = new MatTableDataSource(this.sentMessageResponses);
        });
      } else {
        this.dataSource = new MatTableDataSource(this.sentMessageResponses);
      }
    }
  }
}
