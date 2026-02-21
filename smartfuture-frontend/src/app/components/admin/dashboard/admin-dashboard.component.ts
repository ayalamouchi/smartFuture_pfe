import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="min-h-screen bg-slate-900 flex items-center justify-center">
      <div class="bg-slate-800 rounded-2xl p-10 shadow-2xl text-center max-w-md w-full">
        <div class="w-16 h-16 bg-red-500/20 rounded-xl flex items-center justify-center mx-auto mb-6">
          <svg class="w-8 h-8 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/>
          </svg>
        </div>
        <h1 class="text-2xl font-bold text-white mb-2">Panneau Administrateur</h1>
        <p class="text-slate-400 mb-8">{{ adminEmail }}</p>
        <button
          (click)="logout()"
          class="px-6 py-3 bg-red-600 hover:bg-red-700 text-white font-semibold rounded-lg transition-all">
          Déconnexion
        </button>
      </div>
    </div>
  `
})
export class AdminDashboardComponent implements OnInit {
  adminEmail = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    this.adminEmail = user?.email || '';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
