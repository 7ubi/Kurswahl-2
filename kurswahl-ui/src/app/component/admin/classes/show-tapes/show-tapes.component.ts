import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-show-tapes',
  templateUrl: './show-tapes.component.html',
  styleUrls: ['./show-tapes.component.css']
})
export class ShowTapesComponent {
  constructor(private router: Router, private route: ActivatedRoute) {
  }

  createTape() {
    this.router.navigate(['create'], {relativeTo: this.route});
  }
}
