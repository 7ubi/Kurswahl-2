import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './component/auth/login/login.component';
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {HttpClientModule} from "@angular/common/http";
import {ShowAdminsComponent} from './component/admin/user/show-admins/show-admins.component';
import {CreateAdminComponent} from './component/admin/user/create-admin/create-admin.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {LoginRequired} from "./login-required";
import {AdminRequired} from "./admin-required";
import {MatTableModule} from "@angular/material/table";
import {MatSidenavModule} from "@angular/material/sidenav";
import {HeroComponent} from './component/common/hero/hero.component';
import {SidenavComponent} from './component/common/sidenav/sidenav.component';
import {ShowStudentsComponent} from './component/admin/user/show-students/show-students.component';
import {CreateStudentComponent} from './component/admin/user/create-student/create-student.component';
import {ShowTeachersComponent} from './component/admin/user/show-teachers/show-teachers.component';
import {CreateTeacherComponent} from './component/admin/user/create-teacher/create-teacher.component';
import {ShowSubjectAreasComponent} from './component/admin/classes/show-subject-areas/show-subject-areas.component';
import {CreateSubjectAreaComponent} from './component/admin/classes/create-subject-area/create-subject-area.component';
import {ShowSubjectsComponent} from './component/admin/classes/show-subjects/show-subjects.component';
import {CreateSubjectComponent} from './component/admin/classes/create-subject/create-subject.component';
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSortModule} from "@angular/material/sort";
import {
    ShowStudentClassesComponent
} from './component/admin/classes/show-student-classes/show-student-classes.component';
import {
    CreateStudentClassComponent
} from './component/admin/classes/create-student-class/create-student-class.component';
import {EditAdminComponent} from './component/admin/user/edit-admin/edit-admin.component';
import {EditStudentComponent} from './component/admin/user/edit-student/edit-student.component';
import {EditTeacherComponent} from './component/admin/user/edit-teacher/edit-teacher.component';
import {ChangePasswordComponent} from './component/auth/change-password/change-password.component';
import {MatListModule} from "@angular/material/list";
import {EditSubjectComponent} from './component/admin/classes/edit-subject/edit-subject.component';
import {EditSubjectAreaComponent} from './component/admin/classes/edit-subject-area/edit-subject-area.component';
import {EditStudentClassComponent} from './component/admin/classes/edit-student-class/edit-student-class.component';
import {ShowTapesComponent} from './component/admin/classes/show-tapes/show-tapes.component';
import {CreateTapeComponent} from './component/admin/classes/create-tape/create-tape.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {EditTapeComponent} from './component/admin/classes/edit-tape/edit-tape.component';
import {TapeTableComponent} from './component/admin/classes/show-tapes/tape-table/tape-table.component';
import {ShowClassesComponent} from './component/admin/classes/show-classes/show-classes.component';
import {ClassTableComponent} from './component/admin/classes/show-classes/class-table/class-table.component';
import {CreateClassComponent} from './component/admin/classes/create-class/create-class.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ShowAdminsComponent,
    CreateAdminComponent,
    HeroComponent,
    SidenavComponent,
    ShowStudentsComponent,
    CreateStudentComponent,
    ShowTeachersComponent,
    CreateTeacherComponent,
    ShowSubjectAreasComponent,
    CreateSubjectAreaComponent,
    ShowSubjectsComponent,
    CreateSubjectComponent,
    ShowStudentClassesComponent,
    CreateStudentClassComponent,
    EditAdminComponent,
    EditStudentComponent,
    EditTeacherComponent,
    ChangePasswordComponent,
    EditSubjectComponent,
    EditSubjectAreaComponent,
    EditStudentClassComponent,
    ShowTapesComponent,
    CreateTapeComponent,
    EditTapeComponent,
      TapeTableComponent,
      ShowClassesComponent,
      ClassTableComponent,
      CreateClassComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatToolbarModule,
    MatIconModule,
    HttpClientModule,
    MatSnackBarModule,
    MatTableModule,
    MatSidenavModule,
    MatSelectModule,
    MatSortModule,
    MatListModule,
    MatCheckboxModule
  ],
  providers: [MatSnackBarModule, LoginRequired, AdminRequired],
  bootstrap: [AppComponent]
})
export class AppModule { }
