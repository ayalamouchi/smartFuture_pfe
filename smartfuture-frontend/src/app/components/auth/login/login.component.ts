import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { UserRole } from '../../../core/models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  selectedRole: string = 'apprenant';
  showPassword = false;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });
  }

  selectRole(role: string) {
    if (role !== 'administrateur') {
      this.selectedRole = role;
    }
  }

  onSubmit() {
    if (this.loginForm.valid && !this.isLoading) {
      this.isLoading = true;
      this.errorMessage = '';

      const { email, password } = this.loginForm.value;

      // Convertir le rôle frontend en enum backend
      let role: UserRole;
      switch (this.selectedRole) {
        case 'apprenant':
          role = UserRole.LEARNER;
          break;
        case 'formateur':
          role = UserRole.TRAINER;
          break;
        case 'entreprise':
          role = UserRole.COMPANY;
          break;
        default:
          role = UserRole.LEARNER;
      }

      this.authService.login(email, password, role).subscribe({
        next: (response) => {
          if (response.success && response.data) {
            // Rediriger vers le dashboard approprié selon le rôle
            this.router.navigate(['/dashboard']);
          }
        },
        error: (error) => {
          console.error('Erreur connexion:', error);
          this.errorMessage = error.error?.message || 'Email ou mot de passe incorrect';
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }
}
