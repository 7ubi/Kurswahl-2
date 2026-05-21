import {Component, Input} from '@angular/core';
import {MatToolbar, MatToolbarRow} from "@angular/material/toolbar";

@Component({
  selector: 'app-hero',
  templateUrl: './hero.component.html',
  imports: [
    MatToolbar,
    MatToolbarRow
  ],
  styleUrls: ['./hero.component.css']
})
export class HeroComponent {
  @Input()
  title?: string;
}
