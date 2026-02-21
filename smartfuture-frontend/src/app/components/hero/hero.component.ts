import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-hero',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hero.component.html',
})
export class HeroComponent {
  constructor(private router: Router) {}

  scrollToTest() {
    const element = document.getElementById('test-gratuit');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  goToRegister() {
    this.router.navigate(['/inscription/role']);
  }
}
