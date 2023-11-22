import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./component/auth/login/login.component";
import {ShowAdminsComponent} from "./component/admin/user/show-admins/show-admins.component";
import {LoginRequired} from "./login-required";
import {AdminRequired} from "./admin-required";
import {CreateAdminComponent} from "./component/admin/user/create-admin/create-admin.component";
import {ShowStudentsComponent} from "./component/admin/user/show-students/show-students.component";
import {CreateStudentComponent} from "./component/admin/user/create-student/create-student.component";
import {ShowTeachersComponent} from "./component/admin/user/show-teachers/show-teachers.component";
import {CreateTeacherComponent} from "./component/admin/user/create-teacher/create-teacher.component";
import {ShowSubjectAreasComponent} from "./component/admin/classes/show-subject-areas/show-subject-areas.component";
import {CreateSubjectAreaComponent} from "./component/admin/classes/create-subject-area/create-subject-area.component";
import {ShowSubjectsComponent} from "./component/admin/classes/show-subjects/show-subjects.component";
import {CreateSubjectComponent} from "./component/admin/classes/create-subject/create-subject.component";
import {
  ShowStudentClassesComponent
} from "./component/admin/classes/show-student-classes/show-student-classes.component";
import {
  CreateStudentClassComponent
} from "./component/admin/classes/create-student-class/create-student-class.component";
import {EditAdminComponent} from "./component/admin/user/edit-admin/edit-admin.component";
import {EditStudentComponent} from "./component/admin/user/edit-student/edit-student.component";
import {EditTeacherComponent} from "./component/admin/user/edit-teacher/edit-teacher.component";
import {ChangePasswordComponent} from "./component/auth/change-password/change-password.component";
import {EditSubjectComponent} from "./component/admin/classes/edit-subject/edit-subject.component";
import {EditSubjectAreaComponent} from "./component/admin/classes/edit-subject-area/edit-subject-area.component";

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
    path: 'admin/admins',
    component: ShowAdminsComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/admins/edit/:id',
    component: EditAdminComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/admins/create',
    component: CreateAdminComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/students',
    component: ShowStudentsComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/students/create',
    component: CreateStudentComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/students/edit/:id',
    component: EditStudentComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/teachers',
    component: ShowTeachersComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/teachers/edit/:id',
    component: EditTeacherComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/teachers/create',
    component: CreateTeacherComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/subjectAreas',
    component: ShowSubjectAreasComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/subjectAreas/create',
    component: CreateSubjectAreaComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/subjectAreas/edit/:id',
    component: EditSubjectAreaComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/subjects',
    component: ShowSubjectsComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/subjects/create',
    component: CreateSubjectComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/subjects/edit/:id',
    component: EditSubjectComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/studentClasses',
    component: ShowStudentClassesComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/studentClasses/create',
    component: CreateStudentClassComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
