import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { UploadService } from '../../../core/services/upload.service';

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

  // Upload states
  cvFile: File | null = null;
  cvCNFCPPFile: File | null = null;
  photoFile: File | null = null;
  cvFileUrl: string = '';
  cvCNFCPPFileUrl: string = '';
  photoFileUrl: string = '';
  uploadProgress: { [key: string]: number } = {};
  isUploading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService,
    private uploadService: UploadService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.selectedRole = params['role'] || 'apprenant';
      this.initForm();
    });

    this.setupPasswordStrengthCheck();
  }

  initForm() {
    // Champs communs à tous les rôles
    const baseFields = {
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
    };

    // Champs spécifiques selon le rôle
    switch (this.selectedRole) {
      case 'apprenant':
        this.inscriptionForm = this.fb.group({
          ...baseFields,
          company: [''], // Optionnel
          fonction: [''], // Job/Fonction - NOUVEAU
        }, { validators: this.passwordMatchValidator });
        break;

      case 'formateur':
        this.inscriptionForm = this.fb.group({
          ...baseFields,
          bio: ['', [Validators.maxLength(1000)]], // Optionnel
        }, { validators: this.passwordMatchValidator });
        break;

      case 'entreprise':
        this.inscriptionForm = this.fb.group({
          ...baseFields,
          companyName: ['', Validators.required], // Nom entreprise - REQUIS
          address: ['', Validators.required], // Adresse - REQUIS
        }, { validators: this.passwordMatchValidator });
        break;

      default:
        this.inscriptionForm = this.fb.group(baseFields, { validators: this.passwordMatchValidator });
    }
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

  // ============ GESTION DES FICHIERS ============

  onFileSelected(event: Event, type: 'cv' | 'cv-cnfcpp' | 'photo') {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];

    // Validation
    const validation = this.uploadService.validateFile(file, type === 'photo' ? 'photo' : 'cv');
    if (!validation.valid) {
      alert(validation.error);
      input.value = '';
      return;
    }

    // Stocker le fichier
    switch (type) {
      case 'cv':
        this.cvFile = file;
        break;
      case 'cv-cnfcpp':
        this.cvCNFCPPFile = file;
        break;
      case 'photo':
        this.photoFile = file;
        // Prévisualisation de la photo
        this.previewPhoto(file);
        break;
    }
  }

  previewPhoto(file: File) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.photoFileUrl = e.target.result;
    };
    reader.readAsDataURL(file);
  }

  removeFile(type: 'cv' | 'cv-cnfcpp' | 'photo') {
    switch (type) {
      case 'cv':
        this.cvFile = null;
        this.cvFileUrl = '';
        break;
      case 'cv-cnfcpp':
        this.cvCNFCPPFile = null;
        this.cvCNFCPPFileUrl = '';
        break;
      case 'photo':
        this.photoFile = null;
        this.photoFileUrl = '';
        break;
    }
  }

  // ============ SOUMISSION DU FORMULAIRE ============

  async onSubmit() {
    if (this.inscriptionForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      this.isUploading = true;
      this.errorMessage = '';

      try {
        // 1. Upload des fichiers selon le rôle
        if (this.selectedRole === 'formateur') {
          // Upload CV et CV CNFCPP pour formateur
          if (this.cvFile) {
            const cvResponse = await this.uploadFile(this.cvFile, 'cv');
            this.cvFileUrl = cvResponse.fileUrl;
          }
          if (this.cvCNFCPPFile) {
            const cvCNFCPPResponse = await this.uploadFile(this.cvCNFCPPFile, 'cv-cnfcpp');
            this.cvCNFCPPFileUrl = cvCNFCPPResponse.fileUrl;
          }
        } else if (this.selectedRole === 'apprenant') {
          // Upload photo pour apprenant
          if (this.photoFile) {
            const photoResponse = await this.uploadFile(this.photoFile, 'photo');
            this.photoFileUrl = photoResponse.fileUrl;
          }
        }

        this.isUploading = false;

        // 2. Préparer les données selon le rôle
        const formData = this.prepareFormData();

        // 3. Appeler l'API d'inscription
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
              sessionStorage.setItem('otpEmail', formData.email);
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

      } catch (error) {
        console.error('Erreur upload:', error);
        this.errorMessage = 'Erreur lors de l\'upload des fichiers';
        this.isSubmitting = false;
        this.isUploading = false;
      }
    } else {
      Object.keys(this.inscriptionForm.controls).forEach(key => {
        this.inscriptionForm.get(key)?.markAsTouched();
      });
    }
  }

  private prepareFormData(): any {
    const baseData = {
      prenom: this.inscriptionForm.value.prenom,
      nom: this.inscriptionForm.value.nom,
      email: this.inscriptionForm.value.email,
      telephone: this.inscriptionForm.value.telephone,
      ville: this.inscriptionForm.value.ville,
      motDePasse: this.inscriptionForm.value.motDePasse,
      accepteConditions: this.inscriptionForm.value.accepteConditions,
      accepteConfidentialite: this.inscriptionForm.value.accepteConfidentialite,
      accepteNewsletter: this.inscriptionForm.value.accepteNewsletter
    };

    switch (this.selectedRole) {
      case 'apprenant':
        return {
          ...baseData,
          company: this.inscriptionForm.value.company,
          fonction: this.inscriptionForm.value.fonction,
          profilePictureUrl: this.photoFileUrl
        };

      case 'formateur':
        return {
          ...baseData,
          bio: this.inscriptionForm.value.bio,
          cvFileUrl: this.cvFileUrl,
          cvCNFCPPFileUrl: this.cvCNFCPPFileUrl
        };

      case 'entreprise':
        return {
          ...baseData,
          companyName: this.inscriptionForm.value.companyName,
          contactName: `${this.inscriptionForm.value.prenom} ${this.inscriptionForm.value.nom}`,
          address: this.inscriptionForm.value.address
        };

      default:
        return baseData;
    }
  }

  private uploadFile(file: File, type: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.uploadService.uploadFile(file, type as any).subscribe({
        next: (response) => resolve(response),
        error: (error) => reject(error)
      });
    });
  }

  goBack() {
    this.router.navigate(['/inscription/role']);
  }
}
