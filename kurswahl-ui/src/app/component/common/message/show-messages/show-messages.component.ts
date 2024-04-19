import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MessageResponse} from "../../common.response";
import {HttpService} from "../../../../service/http.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-show-messages',
  templateUrl: './show-messages.component.html',
  styleUrl: './show-messages.component.css'
})
export class ShowMessagesComponent implements OnInit {
  messageResponses!: MessageResponse[];
  dataSource!: MatTableDataSource<MessageResponse>;
  displayedColumns: string[];
  loadedMessages: boolean = false;

  constructor(
    private httpService: HttpService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.displayedColumns = ['Titel', 'Absender', 'Nachricht'];
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
}
