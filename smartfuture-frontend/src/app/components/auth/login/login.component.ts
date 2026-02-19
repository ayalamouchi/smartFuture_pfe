import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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

  constructor(
    private fb: FormBuilder,
    private router: Router
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

  async onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading = true;

      try {
        const { email, password, rememberMe } = this.loginForm.value;

        // Simuler un appel API
        await this.simulateLogin(email, password, this.selectedRole);

        // Rediriger vers le dashboard
        this.router.navigate(['/dashboard']);
      } catch (error) {
        console.error('Erreur de connexion:', error);
        alert('Email ou mot de passe incorrect');
      } finally {
        this.isLoading = false;
      }
    }
  }

  private simulateLogin(email: string, password: string, role: string): Promise<void> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        // Simuler une authentification réussie
        resolve();
      }, 1500);
    });
  }
}
