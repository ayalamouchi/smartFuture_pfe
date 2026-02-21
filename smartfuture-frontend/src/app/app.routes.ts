// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { Home } from './components/home/home.component';

// Auth components
import { RoleSelectionComponent } from './components/auth/role-selection/role-selection.component';
import { InscriptionFormComponent } from './components/auth/inscription-form/inscription-form.component';
import { CheckEmailComponent } from './components/auth/check-email/check-email.component';
import { EmailVerificationComponent } from './components/auth/email-verification/email-verification.component';
import { ConfirmationSuccessComponent } from './components/auth/confirmation-success/confirmation-success.component';
import { LoginComponent } from './components/auth/login/login.component';

export const routes: Routes = [
  // Page d'accueil
  { path: '', component: Home },

  // Parcours d'inscription
  { path: 'inscription/role', component: RoleSelectionComponent },
  { path: 'inscription/informations', component: InscriptionFormComponent },
  { path: 'inscription/check-email', component: CheckEmailComponent },
  { path: 'inscription/verify', component: EmailVerificationComponent },
  { path: 'inscription/confirmation', component: ConfirmationSuccessComponent },

  // Connexion
  { path: 'connexion', component: LoginComponent },

  // Redirection par défaut
  { path: '**', redirectTo: '' }
];
