import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hero',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hero.component.html',
  
})
export class HeroComponent {
  scrollToTest() {
    const element = document.getElementById('test-gratuit');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }
}
