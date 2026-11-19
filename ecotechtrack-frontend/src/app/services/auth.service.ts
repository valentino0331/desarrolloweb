import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { JwtAuthResponse, LoginRequest, RegisterRequest, Usuario } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'ecotechtrack_jwt';
  private readonly USER_KEY = 'ecotechtrack_user';

  // Signals para reactividad moderna en Angular 18
  currentUserSignal = signal<JwtAuthResponse | null>(this.getStoredUser());

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<JwtAuthResponse> {
    return this.http.post<JwtAuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((res) => {
        this.saveSession(res);
      })
    );
  }

  register(userData: RegisterRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/register`, userData);
  }

  getProfile(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/me`);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSignal.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  hasRole(role: string): boolean {
    const user = this.currentUserSignal();
    return user ? user.roles.includes(role) : false;
  }

  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }

  isOperador(): boolean {
    return this.hasRole('ROLE_OPERADOR') || this.isAdmin();
  }

  private saveSession(authResponse: JwtAuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, authResponse.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(authResponse));
    this.currentUserSignal.set(authResponse);
  }

  private getStoredUser(): JwtAuthResponse | null {
    const raw = localStorage.getItem(this.USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw);
    } catch {
      return null;
    }
  }
}
