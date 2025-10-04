import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface User {
  email: string;
  name: string;
  role: 'CUSTOMER' | 'SERVICE_PROVIDER';
}

export interface AuthResponse {
  token: string;
  email: string;
  name: string;
  role: 'CUSTOMER' | 'SERVICE_PROVIDER';
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { email, password })
      .pipe(tap(response => this.setSession(response)));
  }

  signup(userData: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/signup`, userData)
      .pipe(tap(response => this.setSession(response)));
  }

  private setSession(authResult: AuthResponse): void {
    localStorage.setItem('token', authResult.token);
    localStorage.setItem('user', JSON.stringify({
      email: authResult.email,
      name: authResult.name,
      role: authResult.role
    }));
    this.currentUserSubject.next({
      email: authResult.email,
      name: authResult.name,
      role: authResult.role
    });
  }

  private loadUserFromStorage(): void {
    const user = localStorage.getItem('user');
    if (user) {
      this.currentUserSubject.next(JSON.parse(user));
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
  
  forgotPassword(email: string) {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }
  
  resetPassword(token: string, newPassword: string) {
    return this.http.post(`${this.apiUrl}/reset-password`, { token, newPassword });
  }
}