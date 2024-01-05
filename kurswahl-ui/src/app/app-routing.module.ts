import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./component/auth/login/login.component";
import {ShowAdminsComponent} from "./component/admin/user/admin/show-admins/show-admins.component";
import {LoginRequired} from "./routing-helper/login-required";
import {AdminRequired} from "./routing-helper/admin-required";
import {CreateAdminComponent} from "./component/admin/user/admin/create-admin/create-admin.component";
import {ShowStudentsComponent} from "./component/admin/user/student/show-students/show-students.component";
import {CreateStudentComponent} from "./component/admin/user/student/create-student/create-student.component";
import {ShowTeachersComponent} from "./component/admin/user/teacher/show-teachers/show-teachers.component";
import {CreateTeacherComponent} from "./component/admin/user/teacher/create-teacher/create-teacher.component";
import {
  ShowSubjectAreasComponent
} from "./component/admin/classes/subject-area/show-subject-areas/show-subject-areas.component";
import {
  CreateSubjectAreaComponent
} from "./component/admin/classes/subject-area/create-subject-area/create-subject-area.component";
import {ShowSubjectsComponent} from "./component/admin/classes/subject/show-subjects/show-subjects.component";
import {CreateSubjectComponent} from "./component/admin/classes/subject/create-subject/create-subject.component";
import {
  ShowStudentClassesComponent
} from "./component/admin/classes/student-class/show-student-classes/show-student-classes.component";
import {
  CreateStudentClassComponent
} from "./component/admin/classes/student-class/create-student-class/create-student-class.component";
import {EditAdminComponent} from "./component/admin/user/admin/edit-admin/edit-admin.component";
import {EditStudentComponent} from "./component/admin/user/student/edit-student/edit-student.component";
import {EditTeacherComponent} from "./component/admin/user/teacher/edit-teacher/edit-teacher.component";
import {ChangePasswordComponent} from "./component/auth/change-password/change-password.component";
import {EditSubjectComponent} from "./component/admin/classes/subject/edit-subject/edit-subject.component";
import {
  EditSubjectAreaComponent
} from "./component/admin/classes/subject-area/edit-subject-area/edit-subject-area.component";
import {
  EditStudentClassComponent
} from "./component/admin/classes/student-class/edit-student-class/edit-student-class.component";
import {ShowTapesComponent} from "./component/admin/classes/tape/show-tapes/show-tapes.component";
import {CreateTapeComponent} from "./component/admin/classes/tape/create-tape/create-tape.component";
import {EditTapeComponent} from "./component/admin/classes/tape/edit-tape/edit-tape.component";
import {ShowClassesComponent} from "./component/admin/classes/class/show-classes/show-classes.component";
import {CreateClassComponent} from "./component/admin/classes/class/create-class/create-class.component";
import {EditClassComponent} from "./component/admin/classes/class/edit-class/edit-class.component";
import {ShowLessonsComponent} from "./component/admin/classes/tape/show-lessons/show-lessons.component";
import {HomePageComponent} from "./component/student/home-page/home-page.component";
import {StudentRequired} from "./routing-helper/student-required";
import {MakeChoiceComponent} from "./component/student/choice/make-choice/make-choice.component";
import {ShowChoicesComponent} from "./component/student/choice/show-choices/show-choices.component";
import {PageNotFoundComponent} from "./component/common/page-not-found/page-not-found.component";
import {ShowRulesComponent} from "./component/admin/classes/rule/show-rules/show-rules.component";
import {CreateRuleComponent} from "./component/admin/classes/rule/create-rule/create-rule.component";
import {EditRuleComponent} from "./component/admin/classes/rule/edit-rule/edit-rule.component";

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'changePassword',
    component: ChangePasswordComponent,
    canActivate: mapToCanActivate([LoginRequired])
  },
  {
    path: 'admin',
    canActivate: mapToCanActivate([LoginRequired, AdminRequired]),
    children: [
      {
        path: 'admins',
        component: ShowAdminsComponent
      },
      {
        path: 'admins/edit/:id',
        component: EditAdminComponent
      },
      {
        path: 'admins/create',
        component: CreateAdminComponent
      },
      {
        path: 'students',
        component: ShowStudentsComponent
      },
      {
        path: 'students/create',
        component: CreateStudentComponent
      },
      {
        path: 'students/edit/:id',
        component: EditStudentComponent
      },
      {
        path: 'teachers',
        component: ShowTeachersComponent
      },
      {
        path: 'teachers/edit/:id',
        component: EditTeacherComponent
      },
      {
        path: 'teachers/create',
        component: CreateTeacherComponent
      },
      {
        path: 'subjectAreas',
        component: ShowSubjectAreasComponent
      },
      {
        path: 'subjectAreas/create',
        component: CreateSubjectAreaComponent
      },
      {
        path: 'subjectAreas/edit/:id',
        component: EditSubjectAreaComponent
      },
      {
        path: 'subjects',
        component: ShowSubjectsComponent
      },
      {
        path: 'subjects/create',
        component: CreateSubjectComponent
      },
      {
        path: 'subjects/edit/:id',
        component: EditSubjectComponent
      },
      {
        path: 'studentClasses',
        component: ShowStudentClassesComponent
      },
      {
        path: 'studentClasses/create',
        component: CreateStudentClassComponent
      },
      {
        path: 'studentClasses/edit/:id',
        component: EditStudentClassComponent
      },
      {
        path: 'tapes',
        component: ShowTapesComponent
      },
      {
        path: 'tapes/create',
        component: CreateTapeComponent
      },
      {
        path: 'tapes/edit/:id',
        component: EditTapeComponent
      },
      {
        path: 'classes',
        component: ShowClassesComponent
      },
      {
        path: 'classes/create',
        component: CreateClassComponent
      },
      {
        path: 'classes/edit/:id',
        component: EditClassComponent
      },
      {
        path: 'lessons/:year',
        component: ShowLessonsComponent
      },
      {
        path: 'rules',
        component: ShowRulesComponent
      },
      {
        path: 'rules/create',
        component: CreateRuleComponent
      },
      {
        path: 'rules/edit/:id',
        component: EditRuleComponent
      },
    ]
  },
  {
    path: 'student',
    canActivate: mapToCanActivate([LoginRequired, StudentRequired]),
    children: [
      {
        path: '',
        component: HomePageComponent
      },
      {
        path: 'choice/:choiceNumber',
        pathMatch: 'full',
        component: MakeChoiceComponent
      },
      {
        path: 'choices',
        pathMatch: 'full',
        component: ShowChoicesComponent
      },
    ]
  },
  {
    path: '**',
    pathMatch: 'full',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
