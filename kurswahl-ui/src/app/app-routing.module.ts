import { NgModule } from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./component/auth/login/login.component";
import {ShowAdminsComponent} from "./component/admin/user/show-admins/show-admins.component";
import {LoginRequired} from "./login-required";
import {AdminRequired} from "./admin-required";
import {CreateAdminComponent} from "./component/admin/user/create-admin/create-admin.component";

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'admin/admins',
    component: ShowAdminsComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  },
  {
    path: 'admin/admins/erstellen',
    component: CreateAdminComponent,
    canActivate: mapToCanActivate([LoginRequired, AdminRequired])
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
