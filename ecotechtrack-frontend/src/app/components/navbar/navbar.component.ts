import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  authService = inject(AuthService);
  router = inject(Router);

  getHighestRole(roles?: string[]): { label: string; class: string } {
    if (!roles || roles.length === 0) return { label: 'Usuario', class: 'role-user' };
    if (roles.includes('ROLE_ADMIN')) return { label: 'Administrador', class: 'role-admin' };
    if (roles.includes('ROLE_OPERADOR')) return { label: 'Operador RAEE', class: 'role-operador' };
    return { label: 'Usuario', class: 'role-user' };
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
