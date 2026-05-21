import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TapeTableComponent} from "./tape-table/tape-table.component";
import {MatButton} from "@angular/material/button";
import {HeroComponent} from "../../../../common/hero/hero.component";

@Component({
  selector: 'app-show-tapes',
  templateUrl: './show-tapes.component.html',
  imports: [
    TapeTableComponent,
    MatButton,
    HeroComponent
  ],
  styleUrls: ['./show-tapes.component.css']
})
export class ShowTapesComponent {
  constructor(private router: Router, private route: ActivatedRoute) {
  }

  createTape() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }
}
