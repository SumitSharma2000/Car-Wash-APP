import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { CustomerDashboardComponent } from './components/customer-dashboard/customer-dashboard.component';
import { ProviderDashboardComponent } from './components/provider-dashboard/provider-dashboard.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'customer-dashboard', component: CustomerDashboardComponent, canActivate: [AuthGuard] },
  { path: 'provider-dashboard', component: ProviderDashboardComponent, canActivate: [AuthGuard] }
];