import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Partner {
  name: string;
  logo: string;
  website: string;
}

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './about.html',
  styleUrl: './about.scss',
})
export class About {
  partners: Partner[] = [
    {
      name: 'Certiport',
      logo: 'assets/images/partners/certiport.png',
      website: 'https://www.certiport.com'
    },
    {
      name: 'Microsoft',
      logo: 'assets/images/partners/microsoft.png',
      website: 'https://www.microsoft.com'
    },
    {
      name: 'Pearson VUE',
      logo: 'assets/images/partners/pearson-vue.png',
      website: 'https://home.pearsonvue.com'
    },
    {
      name: 'Python Institute',
      logo: 'assets/images/partners/python-institute.png',
      website: 'https://pythoninstitute.org'
    },
    {
      name: 'Skillable',
      logo: 'assets/images/partners/skillable.png',
      website: 'https://www.skillable.com'
    }
  ];

  onImageError(event: Event, partner: Partner) {
    const img = event.target as HTMLImageElement;
    console.error('❌ Erreur chargement:', partner.name, img.src);

    // NE PAS utiliser placeholder externe - laisser vide ou utiliser une icône SVG
    img.style.display = 'none';
  }
}
