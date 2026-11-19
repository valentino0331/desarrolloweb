import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

/**
 * Interceptor HTTP que inyecta automáticamente la cabecera 'Authorization: Bearer <token>'
 * en todas las peticiones salientes dirigidas a la API Spring Boot (Requisito APF3).
 * Captura además respuestas 401 Unauthorized para redirigir al login.
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        console.warn('Sesión caducada o no autorizada. Redirigiendo a /login.');
        authService.logout();
        router.navigate(['/login'], { queryParams: { sesionExpirada: 'true' } });
      }
      return throwError(() => error);
    })
  );
};
