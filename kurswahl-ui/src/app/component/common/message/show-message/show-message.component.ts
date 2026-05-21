import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpService} from "../../../../service/http.service";
import {MessageResponse} from "../../common.response";
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {HeroComponent} from "../../hero/hero.component";

@Component({
  selector: 'app-show-message',
  templateUrl: './show-message.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatCard,
    HeroComponent,
    MatCardHeader,
    MatCardContent
  ],
  styleUrl: './show-message.component.css'
})
export class ShowMessageComponent implements OnInit {
  id: string | null;

  messageResponse?: MessageResponse;

  constructor(
    private httpService: HttpService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {
    this.id = this.route.snapshot.paramMap.get('id');
  }

  ngOnInit(): void {
    this.httpService.get<MessageResponse>(`/api/common/message?messageId=${this.id}`,
      response => {
        this.messageResponse = response;
        this.cdr.detectChanges();
      });
  }
}
