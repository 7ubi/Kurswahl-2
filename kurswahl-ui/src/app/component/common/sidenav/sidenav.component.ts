import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../service/authentication.service";
import {NavigationEnd, Router} from "@angular/router";
import {MatListItem, MatNavList} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";
import {filter} from "rxjs/operators";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  imports: [
    MatNavList,
    MatListItem,
    MatIcon
  ],
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit, OnDestroy {
  isLoading: boolean = false;
  private routeSubscription!: Subscription;

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
    this.routeSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.isLoading = false;
      });
  }

  ngOnDestroy() {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  getRole(): string | null {
    return this.authenticationService.getRole();
  }

  navigateTo(url: string) {
    if (url === '/student/choice/1' && this.router.url !== '/student/choice/1') {
      this.isLoading = true;
    }
    this.router.navigate([url]);
  }
}
