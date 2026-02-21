// src/app/components/auth/reset-password/reset-password.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  token: string = '';
  isLoading = false;
  isValidating = true;
  tokenValid = false;
  successMessage = '';
  errorMessage = '';
  showPassword = false;
  showConfirmPassword = false;
  passwordStrength = 'Faible';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.resetPasswordForm = this.fb.group({
      newPassword: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/)
      ]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    // Récupérer le token depuis l'URL
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];

      if (!this.token) {
        this.isValidating = false;
        this.tokenValid = false;
        this.errorMessage = 'Token manquant';
        return;
      }

      // Valider le token
      this.validateToken();
    });

    // Surveiller le changement du mot de passe pour la force
    this.resetPasswordForm.get('newPassword')?.valueChanges.subscribe(password => {
      this.passwordStrength = this.calculatePasswordStrength(password);
    });
  }

  validateToken() {
    this.authService.validatePasswordResetToken(this.token).subscribe({
      next: (response) => {
        this.isValidating = false;
        this.tokenValid = response.success;
      },
      error: (error) => {
        this.isValidating = false;
        this.tokenValid = false;
        this.errorMessage = error.message || 'Le lien de réinitialisation est invalide ou expiré';
      }
    });
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('newPassword');
    const confirmPassword = control.get('confirmPassword');

    if (!password || !confirmPassword) {
      return null;
    }

    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }

  calculatePasswordStrength(password: string): string {
    if (!password) return 'Faible';

    let strength = 0;
    if (password.length >= 8) strength++;
    if (password.length >= 12) strength++;
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^a-zA-Z0-9]/.test(password)) strength++;

    if (strength <= 2) return 'Faible';
    if (strength <= 3) return 'Moyenne';
    return 'Forte';
  }

  onSubmit() {
    if (this.resetPasswordForm.valid && !isLoading) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const { newPassword, confirmPassword } = this.resetPasswordForm.value;

      this.authService.resetPassword(this.token, newPassword, confirmPassword).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success) {
            this.successMessage = response.message;

            // Rediriger vers la connexion après 3 secondes
            setTimeout(() => {
              this.router.navigate(['/connexion'], {
                queryParams: { passwordReset: 'true' }
              });
            }, 3000);
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.message || 'Une erreur est survenue lors de la réinitialisation';
        }
      });
    } else {
      Object.keys(this.resetPasswordForm.controls).forEach(key => {
        this.resetPasswordForm.get(key)?.markAsTouched();
      });
    }
  }

  togglePasswordVisibility(field: 'password' | 'confirmPassword') {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }

  goToLogin() {
    this.router.navigate(['/connexion']);
  }

  requestNewLink() {
    this.router.navigate(['/mot-de-passe-oublie']);
  }
}
