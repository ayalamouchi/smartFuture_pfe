import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-inscription-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './inscription-form.component.html'
})
export class InscriptionFormComponent implements OnInit {
  inscriptionForm!: FormGroup;
  selectedRole: string = '';
  showPassword = false;
  passwordStrength = 'Faible';
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.selectedRole = params['role'] || 'apprenant';
    });

    this.initForm();
    this.setupPasswordStrengthCheck();
  }

  initForm() {
    this.inscriptionForm = this.fb.group({
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      nom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern(/^[0-9]{8,15}$/)]],
      ville: ['', Validators.required],
      company: [''], // Optionnel pour apprenant
      motDePasse: ['', [Validators.required, Validators.minLength(8)]],
      confirmMotDePasse: ['', Validators.required],
      accepteConditions: [false, Validators.requiredTrue],
      accepteConfidentialite: [false, Validators.requiredTrue],
      accepteNewsletter: [false]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('motDePasse');
    const confirmPassword = control.get('confirmMotDePasse');

    if (!password || !confirmPassword) {
      return null;
    }

    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }

  setupPasswordStrengthCheck() {
    this.inscriptionForm.get('motDePasse')?.valueChanges.subscribe(password => {
      this.passwordStrength = this.calculatePasswordStrength(password);
    });
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
    if (this.inscriptionForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      this.errorMessage = '';

      const formData = this.inscriptionForm.value;

      let registerObservable;

      switch (this.selectedRole) {
        case 'apprenant':
          registerObservable = this.authService.registerLearner(formData);
          break;
        case 'formateur':
          registerObservable = this.authService.registerTrainer(formData);
          break;
        case 'entreprise':
          registerObservable = this.authService.registerCompany(formData);
          break;
        default:
          this.errorMessage = 'Rôle non valide';
          this.isSubmitting = false;
          return;
      }

      registerObservable.subscribe({
        next: (response) => {
          if (response.success && response.data) {
            // Stocker l'email pour la page de vérification OTP
            sessionStorage.setItem('otpEmail', formData.email);

            // Rediriger vers la page de vérification OTP
            this.router.navigate(['/inscription/verification'], {
              queryParams: { email: formData.email }
            });
          }
        },
        error: (error) => {
          console.error('Erreur inscription:', error);
          this.errorMessage = error.error?.message || 'Une erreur est survenue lors de l\'inscription';
          this.isSubmitting = false;
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });
    } else {
      Object.keys(this.inscriptionForm.controls).forEach(key => {
        this.inscriptionForm.get(key)?.markAsTouched();
      });
    }
  }

  goBack() {
    this.router.navigate(['/inscription/role']);
  }
}
