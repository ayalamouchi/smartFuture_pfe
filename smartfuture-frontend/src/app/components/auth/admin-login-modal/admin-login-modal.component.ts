import { Component, EventEmitter, Output, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-login-modal.component.html'
})
export class AdminLoginModalComponent implements OnInit, OnDestroy {
  @Output() close = new EventEmitter<void>();

  adminForm: FormGroup;
  isLoading = false;
  showPassword = false;
  errorMessage = '';
  private keySequence = '';
  private keyTimeout: any;

  constructor(private fb: FormBuilder, private router: Router) {
    this.adminForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
    // Bloquer le scroll du body
    document.body.style.overflow = 'hidden';
  }

  ngOnDestroy() {
    document.body.style.overflow = '';
  }

  onSubmit() {
    if (this.adminForm.valid && !this.isLoading) {
      this.isLoading = true;
      this.errorMessage = '';

      const { email, password } = this.adminForm.value;

      // TODO: Appeler l'API admin login
      // this.authService.loginAdmin(email, password).subscribe(...)
      console.log('Admin login:', email);

      // Simulation (à remplacer par l'appel API)
      setTimeout(() => {
        this.isLoading = false;
        // this.router.navigate(['/admin/dashboard']);
        this.errorMessage = 'API admin non configurée.';
      }, 1000);
    } else {
      Object.keys(this.adminForm.controls).forEach(key =>
        this.adminForm.get(key)?.markAsTouched()
      );
    }
  }

  closeModal() {
    this.close.emit();
  }

  onOverlayClick(event: MouseEvent) {
    if ((event.target as HTMLElement).classList.contains('modal-overlay')) {
      this.closeModal();
    }
  }
}
