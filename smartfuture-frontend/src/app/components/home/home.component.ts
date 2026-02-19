import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { HeroComponent } from '../hero/hero.component';
import { About } from '../about/about';
import { Features } from '../features/features';
import { Gallery } from '../gallery/gallery';
import { Stats } from '../stats/stats';
import { Footer } from '../footer/footer';

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
    Footer
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.scss',
})
export class Home {

}
