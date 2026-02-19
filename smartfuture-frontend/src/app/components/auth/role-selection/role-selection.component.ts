import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-role-selection',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './role-selection.component.html'
})
export class RoleSelectionComponent {
  selectedRole: string | null = null;

  constructor(private router: Router) {}

  selectRole(role: string) {
    this.selectedRole = role;
  }

  continue() {
    if (this.selectedRole) {
      // Naviguer vers le formulaire d'inscription avec le rôle sélectionné
      this.router.navigate(['/inscription/informations'], {
        queryParams: { role: this.selectedRole }
      });
    }
  }
}
