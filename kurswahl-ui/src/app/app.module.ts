import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/auth/login/login.component';
import {ReactiveFormsModule} from "@angular/forms";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {HttpClientModule} from "@angular/common/http";
import { ShowAdminsComponent } from './component/admin/user/show-admins/show-admins.component';
import { CreateAdminComponent } from './component/admin/user/create-admin/create-admin.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {LoginRequired} from "./login-required";
import {AdminRequired} from "./admin-required";
import {MatTableModule} from "@angular/material/table";
import {MatSidenavModule} from "@angular/material/sidenav";
import { HeroComponent } from './component/common/hero/hero.component';
import { SidenavComponent } from './component/common/sidenav/sidenav.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ShowAdminsComponent,
    CreateAdminComponent,
    HeroComponent,
    SidenavComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        MatInputModule,
        MatButtonModule,
        MatToolbarModule,
        MatIconModule,
        HttpClientModule,
        MatSnackBarModule,
        MatTableModule,
        MatSidenavModule
    ],
  providers: [MatSnackBarModule, LoginRequired, AdminRequired],
  bootstrap: [AppComponent]
})
export class AppModule { }
