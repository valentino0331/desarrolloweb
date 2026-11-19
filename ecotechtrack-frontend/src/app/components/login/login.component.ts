import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  loginForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  sesionExpirada = false;

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
      return;
    }

    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });

    this.route.queryParams.subscribe((params) => {
      if (params['sesionExpirada']) {
        this.sesionExpirada = true;
      }
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.sesionExpirada = false;

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 401) {
          this.errorMessage = 'Usuario o contraseña incorrectos. Verifica tus credenciales.';
        } else if (err.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor Spring Boot (http://localhost:8080). Asegúrate de que el backend esté ejecutándose.';
        } else {
          this.errorMessage = err.error?.mensaje || 'Error al autenticar con el servidor.';
        }
      }
    });
  }

  // Atajos rápidos para agilizar la evaluación académica
  setDemoUser(user: 'admin' | 'operador' | 'usuario'): void {
    if (user === 'admin') {
      this.loginForm.setValue({ username: 'admin', password: 'Admin123!' });
    } else if (user === 'operador') {
      this.loginForm.setValue({ username: 'operador', password: 'Operador123!' });
    } else {
      this.loginForm.setValue({ username: 'usuario', password: 'User123!' });
    }
    this.onSubmit();
  }
}
