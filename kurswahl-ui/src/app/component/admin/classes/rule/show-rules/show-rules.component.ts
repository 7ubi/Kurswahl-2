import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-show-rules',
  templateUrl: './show-rules.component.html',
  styleUrl: './show-rules.component.css'
})
export class ShowRulesComponent {
  constructor(private router: Router, private route: ActivatedRoute) {
  }

  createRule() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }
}
