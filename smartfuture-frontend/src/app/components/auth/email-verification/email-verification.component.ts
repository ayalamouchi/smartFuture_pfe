// src/app/components/auth/email-verification/email-verification.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-email-verification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './email-verification.component.html'
})
export class EmailVerificationComponent implements OnInit {
  isVerifying = true;
  verificationSuccess = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const token = this.route.snapshot.queryParams['token'];

    if (!token) {
      this.isVerifying = false;
      this.errorMessage = 'Token de vérification manquant';
      return;
    }

    this.verifyEmail(token);
  }

  verifyEmail(token: string) {
    this.authService.verifyEmailToken(token).subscribe({
      next: (response) => {
        if (response.success) {
          this.isVerifying = false;
          this.verificationSuccess = true;

          // Rediriger après 3 secondes
          setTimeout(() => {
            this.router.navigate(['/inscription/confirmation']);
          }, 3000);
        }
      },
      error: (error) => {
        console.error('Erreur vérification:', error);
        this.isVerifying = false;
        this.verificationSuccess = false;
        this.errorMessage = error.error?.message || 'Le lien de vérification est invalide ou expiré';
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/connexion']);
  }
}
