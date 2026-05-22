import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ClassTableComponent} from "./class-table/class-table.component";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-show-classes',
  templateUrl: './show-classes.component.html',
  imports: [
    ClassTableComponent,
    HeroComponent,
    MatButton
  ],
  styleUrls: ['./show-classes.component.css']
})
export class ShowClassesComponent {
  constructor(private router: Router, private route: ActivatedRoute) {
  }

  createClass() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }
}
