import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './component/common/header/header.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    ShowAdminsComponent,
    CreateAdminComponent
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
    MatSnackBarModule
  ],
  providers: [MatSnackBarModule, LoginRequired, AdminRequired],
  bootstrap: [AppComponent]
})
export class AppModule { }
