import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Partner {
  name: string;
  logo: string;
  website: string;
  fallbackColor?: string;
  imageError?: boolean;
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
      website: 'https://www.certiport.com',
      fallbackColor: 'from-blue-500 to-blue-600',
      imageError: false
    },
    {
      name: 'Microsoft',
      logo: 'assets/images/partners/microsoft.png',
      website: 'https://www.microsoft.com',
      fallbackColor: 'from-green-500 to-green-600',
      imageError: false
    },
    {
      name: 'Pearson VUE',
      logo: 'assets/images/partners/pearson-vue.png',
      website: 'https://home.pearsonvue.com',
      fallbackColor: 'from-red-500 to-red-600',
      imageError: false
    },
    {
      name: 'Python Institute',
      logo: 'assets/images/partners/python-institute.png',
      website: 'https://pythoninstitute.org',
      fallbackColor: 'from-yellow-500 to-yellow-600',
      imageError: false
    },
    {
      name: 'Skillable',
      logo: 'assets/images/partners/skillable.png',
      website: 'https://www.skillable.com',
      fallbackColor: 'from-purple-500 to-purple-600',
      imageError: false
    }
  ];

  onImageError(event: Event, partner: Partner) {
    const img = event.target as HTMLImageElement;
    console.warn(`⚠️  Image non trouvée pour ${partner.name}: ${img.src}`);

    // Marquer l'image comme en erreur pour afficher le fallback
    partner.imageError = true;
  }
}
