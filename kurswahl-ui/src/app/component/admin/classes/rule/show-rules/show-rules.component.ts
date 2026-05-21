import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {HeroComponent} from "../../../../common/hero/hero.component";
import {RuleTableComponent} from "./rule-table/rule-table.component";

@Component({
  selector: 'app-show-rules',
  templateUrl: './show-rules.component.html',
  imports: [
    MatButton,
    HeroComponent,
    RuleTableComponent
  ],
  styleUrl: './show-rules.component.css'
})
export class ShowRulesComponent {
  constructor(private router: Router, private route: ActivatedRoute) {
  }

  createRule() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }
}
