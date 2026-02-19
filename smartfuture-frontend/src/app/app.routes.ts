import { Routes } from '@angular/router';
import { Home } from './components/home/home.component';

// Auth components
import { RoleSelectionComponent } from './components/auth/role-selection/role-selection.component';
import { InscriptionFormComponent } from './components/auth/inscription-form/inscription-form.component';
import { OtpVerificationComponent } from './components/auth/otp-verification/otp-verification.component';
import { ConfirmationSuccessComponent } from './components/auth/confirmation-success/confirmation-success.component';
import { LoginComponent } from './components/auth/login/login.component';

export const routes: Routes = [
  // Page d'accueil
  { path: '', component: Home },

  // Parcours d'inscription
  { path: 'inscription/role', component: RoleSelectionComponent },
  { path: 'inscription/informations', component: InscriptionFormComponent },
  { path: 'inscription/verification', component: OtpVerificationComponent },
  { path: 'inscription/confirmation', component: ConfirmationSuccessComponent },

  // Connexion
  { path: 'connexion', component: LoginComponent },

  // Redirection par défaut
  { path: '**', redirectTo: '' }
];
