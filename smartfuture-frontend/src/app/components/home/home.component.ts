import { Component, HostListener, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { HeroComponent } from '../hero/hero.component';
import { About } from '../about/about';
import { Features } from '../features/features';
import { Gallery } from '../gallery/gallery';
import { Stats } from '../stats/stats';
import { Footer } from '../footer/footer';
import { AdminLoginModalComponent } from '../auth/admin-login-modal/admin-login-modal.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    HeroComponent,
    About,
    Features,
    Gallery,
    Stats,
    Footer,
    AdminLoginModalComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.scss',
})
export class Home implements OnDestroy {
  showAdminModal = false;

  private keySequence = '';
  private keyTimeout: any;
  private readonly SECRET_WORD = 'admin';

  @HostListener('document:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    // Ignorer si l'utilisateur est dans un input/textarea
    const tag = (event.target as HTMLElement).tagName.toLowerCase();
    if (tag === 'input' || tag === 'textarea' || tag === 'select') return;

    // Ignorer les touches spéciales
    if (event.key.length > 1) {
      this.keySequence = '';
      return;
    }

    this.keySequence += event.key.toLowerCase();

    // Garder seulement les N derniers caractères (longueur du mot secret)
    if (this.keySequence.length > this.SECRET_WORD.length) {
      this.keySequence = this.keySequence.slice(-this.SECRET_WORD.length);
    }

    // Réinitialiser après 2 secondes d'inactivité
    clearTimeout(this.keyTimeout);
    this.keyTimeout = setTimeout(() => {
      this.keySequence = '';
    }, 2000);

    // Vérifier si le mot secret est tapé
    if (this.keySequence === this.SECRET_WORD) {
      this.keySequence = '';
      this.showAdminModal = true;
    }
  }

  closeAdminModal() {
    this.showAdminModal = false;
  }

  ngOnDestroy() {
    clearTimeout(this.keyTimeout);
  }
}
