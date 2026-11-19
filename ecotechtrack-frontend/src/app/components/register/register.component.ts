import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Rol } from '../../models/models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  registerForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  rolesDisponibles = [
    { value: Rol.ROLE_USER, label: 'Auditor / Usuario Consultor' },
    { value: Rol.ROLE_OPERADOR, label: 'Operador Logístico RAEE' },
    { value: Rol.ROLE_ADMIN, label: 'Administrador del Sistema' }
  ];

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      nombreCompleto: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rolSeleccionado: [Rol.ROLE_USER, Validators.required]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formVal = this.registerForm.value;
    const registerPayload = {
      username: formVal.username,
      nombreCompleto: formVal.nombreCompleto,
      email: formVal.email,
      password: formVal.password,
      roles: [formVal.rolSeleccionado]
    };

    this.authService.register(registerPayload).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = '¡Usuario registrado exitosamente! Redirigiendo al login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.mensaje || 'Error al registrar el usuario en el servidor.';
      }
    });
  }
}
