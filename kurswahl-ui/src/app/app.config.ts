import {ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideAnimations} from '@angular/platform-browser/animations';

import {FormGroupDirective} from '@angular/forms';
import {HttpService} from "./service/http.service";
import {LoginRequired} from "./routing-helper/login-required";
import {routes} from "./app.routes";
import {provideHttpClient} from "@angular/common/http";
import {StudentRequired} from "./routing-helper/student-required";
import {TeacherRequired} from "./routing-helper/teacher-required";
import {AdminRequired} from "./routing-helper/admin-required";
import {StorageService} from "./service/storage.service";
import {AuthenticationService} from "./service/authentication.service";
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatButtonModule} from '@angular/material/button';
import {MatTableModule} from '@angular/material/table';
import {MatSortModule} from '@angular/material/sort';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatCardModule} from '@angular/material/card';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideAnimations(),
    provideRouter(routes),
    provideHttpClient(),
    FormGroupDirective,
    HttpService,
    LoginRequired,
    StudentRequired,
    TeacherRequired,
    AdminRequired,
    StorageService,
    AuthenticationService,
    importProvidersFrom(
      MatToolbarModule,
      MatIconModule,
      MatMenuModule,
      MatSidenavModule,
      MatListModule,
      MatButtonModule,
      MatTableModule,
      MatSortModule,
      MatProgressSpinnerModule,
      MatSnackBarModule,
      MatFormFieldModule,
      MatInputModule,
      MatPaginatorModule,
      MatCardModule
    )
  ]
};
