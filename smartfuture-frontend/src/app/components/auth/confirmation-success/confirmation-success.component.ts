import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation-success',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirmation-success.component.html'
})
export class ConfirmationSuccessComponent implements OnInit {
  userRole: string = 'Apprenant';

  constructor(private router: Router) {}

  ngOnInit() {
    // Récupérer les données d'inscription depuis sessionStorage
    const inscriptionData = sessionStorage.getItem('inscriptionData');
    if (inscriptionData) {
      const data = JSON.parse(inscriptionData);
      this.userRole = this.getRoleLabel(data.role);

      // Nettoyer le sessionStorage après utilisation
      sessionStorage.removeItem('inscriptionData');
    }
  }

  getRoleLabel(role: string): string {
    const roleLabels: { [key: string]: string } = {
      'apprenant': 'Apprenant',
      'formateur': 'Formateur',
      'entreprise': 'Entreprise'
    };
    return roleLabels[role] || 'Apprenant';
  }

  goToDashboard() {
    // Rediriger vers le tableau de bord ou la page de connexion
    this.router.navigate(['/connexion']);
  }
}
