// src/app/components/auth/check-email/check-email.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-check-email',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './check-email.component.html'
})
export class CheckEmailComponent implements OnInit {
  userEmail: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.userEmail = params['email'] || sessionStorage.getItem('registrationEmail') || '';
    });
  }

  goToLogin() {
    this.router.navigate(['/connexion']);
  }
}
