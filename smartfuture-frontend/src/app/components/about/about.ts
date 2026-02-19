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
      name: 'Microsoft',
      logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/512px-Microsoft_logo.svg.png',
      website: 'https://www.microsoft.com'
    },
    {
      name: 'Certiport',
      logo: 'https://logos-world.net/wp-content/uploads/2022/02/Certiport-Logo.png',
      website: 'https://www.certiport.com'
    },
    {
      name: 'Pearson VUE',
      logo: 'https://mma.prnewswire.com/media/683230/Pearson_VUE_Logo.jpg',
      website: 'https://home.pearsonvue.com'
    },
    {
      name: 'Python Institute',
      logo: 'https://pythoninstitute.org/assets/627eea95c6b5250e69c4c66e/627eea95c6b5250e69c4c675_python-institute-logo.png',
      website: 'https://pythoninstitute.org'
    },
    {
      name: 'Skillable',
      logo: 'https://images.crunchbase.com/image/upload/c_pad,h_256,w_256,f_auto,q_auto:eco,dpr_1/v1488591002/vhkcmfnzwbkaqxqla7te.png',
      website: 'https://www.skillable.com'
    }
  ];

  onImageError(event: Event) {
    const img = event.target as HTMLImageElement;
    // Fallback vers un placeholder générique
    img.src = 'https://via.placeholder.com/200x80/3B82F6/FFFFFF?text=Partner+Logo';
  }
}
