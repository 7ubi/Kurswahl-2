import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-show-classes',
    templateUrl: './show-classes.component.html',
    styleUrls: ['./show-classes.component.css']
})
export class ShowClassesComponent {
    constructor(private router: Router, private route: ActivatedRoute) {
    }

    createClass() {
        this.router.navigate(['create'], {relativeTo: this.route});
    }
}
