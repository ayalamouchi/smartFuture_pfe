import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  isMenuOpen = false;
  isScrolled = false;

  navItems = [
    { id: 'accueil', label: 'Accueil' },
    { id: 'apropos', label: 'À propos' },
    { id: 'catalogue', label: 'Catalogue' },
    { id: 'examens', label: 'Examens Certiport' },
    { id: 'contact', label: 'Contact' }
  ];

  constructor(private router: Router) {}

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isScrolled = window.pageYOffset > 20;
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  scrollToSection(sectionId: string) {
    const element = document.getElementById(sectionId);
    if (element) {
      const offset = 80;
      const elementPosition = element.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.pageYOffset - offset;

      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth'
      });

      this.isMenuOpen = false;
    }
  }

  goToLogin() {
    this.router.navigate(['/connexion']);
    this.isMenuOpen = false;
  }

  goToRegister() {
    this.router.navigate(['/inscription/role']);
    this.isMenuOpen = false;
  }
}
