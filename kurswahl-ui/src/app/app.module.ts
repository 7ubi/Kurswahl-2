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
import {ShowAdminsComponent} from './component/admin/user/admin/show-admins/show-admins.component';
import {CreateAdminComponent} from './component/admin/user/admin/create-admin/create-admin.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {LoginRequired} from "./routing-helper/login-required";
import {AdminRequired} from "./routing-helper/admin-required";
import {MatTableModule} from "@angular/material/table";
import {MatSidenavModule} from "@angular/material/sidenav";
import {HeroComponent} from './component/common/hero/hero.component';
import {SidenavComponent} from './component/common/sidenav/sidenav.component';
import {ShowStudentsComponent} from './component/admin/user/student/show-students/show-students.component';
import {CreateStudentComponent} from './component/admin/user/student/create-student/create-student.component';
import {ShowTeachersComponent} from './component/admin/user/teacher/show-teachers/show-teachers.component';
import {CreateTeacherComponent} from './component/admin/user/teacher/create-teacher/create-teacher.component';
import {
  ShowSubjectAreasComponent
} from './component/admin/classes/subject-area/show-subject-areas/show-subject-areas.component';
import {
  CreateSubjectAreaComponent
} from './component/admin/classes/subject-area/create-subject-area/create-subject-area.component';
import {ShowSubjectsComponent} from './component/admin/classes/subject/show-subjects/show-subjects.component';
import {CreateSubjectComponent} from './component/admin/classes/subject/create-subject/create-subject.component';
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSortModule} from "@angular/material/sort";
import {
  ShowStudentClassesComponent
} from './component/admin/classes/student-class/show-student-classes/show-student-classes.component';
import {
  CreateStudentClassComponent
} from './component/admin/classes/student-class/create-student-class/create-student-class.component';
import {EditAdminComponent} from './component/admin/user/admin/edit-admin/edit-admin.component';
import {EditStudentComponent} from './component/admin/user/student/edit-student/edit-student.component';
import {EditTeacherComponent} from './component/admin/user/teacher/edit-teacher/edit-teacher.component';
import {ChangePasswordComponent} from './component/auth/change-password/change-password.component';
import {MatListModule} from "@angular/material/list";
import {EditSubjectComponent} from './component/admin/classes/subject/edit-subject/edit-subject.component';
import {
  EditSubjectAreaComponent
} from './component/admin/classes/subject-area/edit-subject-area/edit-subject-area.component';
import {
  EditStudentClassComponent
} from './component/admin/classes/student-class/edit-student-class/edit-student-class.component';
import {ShowTapesComponent} from './component/admin/classes/tape/show-tapes/show-tapes.component';
import {CreateTapeComponent} from './component/admin/classes/tape/create-tape/create-tape.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {EditTapeComponent} from './component/admin/classes/tape/edit-tape/edit-tape.component';
import {TapeTableComponent} from './component/admin/classes/tape/show-tapes/tape-table/tape-table.component';
import {ShowClassesComponent} from './component/admin/classes/class/show-classes/show-classes.component';
import {ClassTableComponent} from './component/admin/classes/class/show-classes/class-table/class-table.component';
import {CreateClassComponent} from './component/admin/classes/class/create-class/create-class.component';
import {EditClassComponent} from './component/admin/classes/class/edit-class/edit-class.component';
import {ShowLessonsComponent} from './component/admin/classes/tape/show-lessons/show-lessons.component';
import {MatChipsModule} from "@angular/material/chips";
import {HomePageComponent} from './component/student/home-page/home-page.component';
import {StudentRequired} from "./routing-helper/student-required";
import {MakeChoiceComponent} from "./component/student/choice/make-choice/make-choice.component";
import {ChoiceTableComponent} from "./component/student/choice/choice-table/choice-table.component";
import {ShowChoicesComponent} from "./component/student/choice/show-choices/show-choices.component";
import {ShowRulesComponent} from "./component/admin/classes/rule/show-rules/show-rules.component";
import {RuleTableComponent} from "./component/admin/classes/rule/show-rules/rule-table/rule-table.component";
import {CreateRuleComponent} from "./component/admin/classes/rule/create-rule/create-rule.component";
import {EditRuleComponent} from "./component/admin/classes/rule/edit-rule/edit-rule.component";
import {MatExpansionModule} from "@angular/material/expansion";
import {ChoiceSurveillanceComponent} from "./component/admin/choice/choice-surveillance/choice-surveillance.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {AssignChoiceComponent} from "./component/admin/choice/assign-choice/assign-choice.component";
import {MatMenuModule} from "@angular/material/menu";

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
    CreateClassComponent,
    EditClassComponent,
    ShowLessonsComponent,
    HomePageComponent,
    MakeChoiceComponent,
    ChoiceTableComponent,
    ShowChoicesComponent,
    ShowRulesComponent,
    RuleTableComponent,
    CreateRuleComponent,
    EditRuleComponent,
    ChoiceSurveillanceComponent,
    AssignChoiceComponent
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
    MatCheckboxModule,
    MatChipsModule,
    MatExpansionModule,
    MatProgressSpinnerModule,
    MatMenuModule
  ],
  providers: [MatSnackBarModule, LoginRequired, AdminRequired, StudentRequired],
  exports: [
    HeroComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
