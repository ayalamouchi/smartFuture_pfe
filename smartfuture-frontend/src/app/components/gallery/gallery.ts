import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-gallery',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gallery.html',
  styleUrl: './gallery.scss',
})
export class Gallery {
  testimonials = [
    {
      name: 'Ahmed Ben Salem',
      role: 'Développeur Full Stack',
      company: 'Tech Solutions',
      image: 'https://ui-avatars.com/api/?name=Ahmed+Ben+Salem&background=E74C3C&color=fff&size=128',
      text: 'Excellente formation Microsoft Azure. Les formateurs sont très compétents et la plateforme est intuitive.',
      rating: 5
    },
    {
      name: 'Fatma Mansouri',
      role: 'Chef de Projet',
      company: 'Orange Tunisie',
      image: 'https://ui-avatars.com/api/?name=Fatma+Mansouri&background=3498DB&color=fff&size=128',
      text: 'La certification PMP obtenue grâce à SmartFuture a boosté ma carrière. Je recommande vivement !',
      rating: 5
    },
    {
      name: 'Mohamed Trabelsi',
      role: 'Ingénieur DevOps',
      company: 'STB Bank',
      image: 'https://ui-avatars.com/api/?name=Mohamed+Trabelsi&background=2C3E50&color=fff&size=128',
      text: 'Formation DevOps complète avec cas pratiques. L\'accompagnement IA est un vrai plus.',
      rating: 5
    }
  ];
}
