import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

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

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Récupérer le rôle depuis les query params
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
    if (this.inscriptionForm.valid) {
      const formData = {
        ...this.inscriptionForm.value,
        role: this.selectedRole
      };

      // Stocker les données temporairement (ou envoyer à l'API)
      sessionStorage.setItem('inscriptionData', JSON.stringify(formData));

      // Naviguer vers la page de vérification OTP
      this.router.navigate(['/inscription/verification'], {
        queryParams: { email: this.inscriptionForm.value.email }
      });
    } else {
      // Marquer tous les champs comme touchés pour afficher les erreurs
      Object.keys(this.inscriptionForm.controls).forEach(key => {
        this.inscriptionForm.get(key)?.markAsTouched();
      });
    }
  }

  goBack() {
    this.router.navigate(['/inscription/role']);
  }
}
