import { Component, EventEmitter, Output, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { UserRole } from '../../../core/models/user.model';

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

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.adminForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
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

      this.authService.login(email, password, UserRole.ADMIN).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success && response.data) {
            localStorage.setItem('isAdmin', 'true');
            this.closeModal();
            this.router.navigate(['/admin/dashboard']);
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.error?.message || 'Identifiants incorrects';
        }
      });
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
