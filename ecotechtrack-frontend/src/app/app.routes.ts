import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ResiduosListComponent } from './components/residuos/residuos-list/residuos-list.component';
import { ResiduoFormComponent } from './components/residuos/residuo-form/residuo-form.component';
import { ResiduoDetailComponent } from './components/residuos/residuo-detail/residuo-detail.component';
import { AlmacenesListComponent } from './components/almacenes/almacenes-list/almacenes-list.component';
import { EmpresasListComponent } from './components/empresas/empresas-list/empresas-list.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'residuos', component: ResiduosListComponent, canActivate: [authGuard] },
  { path: 'residuos/nuevo', component: ResiduoFormComponent, canActivate: [authGuard] },
  { path: 'residuos/:id', component: ResiduoDetailComponent, canActivate: [authGuard] },
  { path: 'residuos/:id/editar', component: ResiduoFormComponent, canActivate: [authGuard] },
  { path: 'almacenes', component: AlmacenesListComponent, canActivate: [authGuard] },
  { path: 'empresas', component: EmpresasListComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' }
];
