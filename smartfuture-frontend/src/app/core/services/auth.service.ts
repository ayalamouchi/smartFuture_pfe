import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, OtpSentResponse, ApiResponse } from '../models/auth-response.model';
import { User, UserRole } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  // ============ INSCRIPTION ============

  registerLearner(data: any): Observable<ApiResponse<OtpSentResponse>> {
    return this.http.post<ApiResponse<OtpSentResponse>>(
      `${this.apiUrl}/register/learner`,
      data
    );
  }

  registerTrainer(data: any): Observable<ApiResponse<OtpSentResponse>> {
    return this.http.post<ApiResponse<OtpSentResponse>>(
      `${this.apiUrl}/register/trainer`,
      data
    );
  }

  registerCompany(data: any): Observable<ApiResponse<OtpSentResponse>> {
    return this.http.post<ApiResponse<OtpSentResponse>>(
      `${this.apiUrl}/register/company`,
      data
    );
  }

  // ============ VÉRIFICATION OTP ============

  verifyOtp(email: string, code: string): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(
      `${this.apiUrl}/verify-otp`,
      { email, code }
    ).pipe(
      tap(response => {
        if (response.success && response.data) {
          this.handleAuthentication(response.data);
        }
      })
    );
  }
  // src/app/core/services/auth.service.ts (ajouter cette méthode)
verifyEmailToken(token: string): Observable<ApiResponse<AuthResponse>> {
  return this.http.get<ApiResponse<AuthResponse>>(
    `${this.apiUrl}/verify?token=${token}`
  ).pipe(
    tap(response => {
      if (response.success && response.data) {
        this.handleAuthentication(response.data);
      }
    })
  );
}

  // ============ CONNEXION ============

  login(email: string, password: string, role: UserRole): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(
      `${this.apiUrl}/login`,
      { email, password, role }
    ).pipe(
      tap(response => {
        if (response.success && response.data) {
          this.handleAuthentication(response.data);
        }
      })
    );
  }

  // ============ GESTION TOKEN ============

  private handleAuthentication(authResponse: AuthResponse): void {
    // Stocker le token
    localStorage.setItem('token', authResponse.token);

    // Créer l'objet utilisateur
    const user: User = {
      id: 0, // Sera extrait du token JWT si nécessaire
      email: authResponse.email,
      role: authResponse.role,
      firstName: authResponse.firstName,
      lastName: authResponse.lastName,
      emailVerified: authResponse.emailVerified
    };

    // Stocker l'utilisateur
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private loadUserFromStorage(): void {
    const userJson = localStorage.getItem('currentUser');
    if (userJson) {
      const user = JSON.parse(userJson);
      this.currentUserSubject.next(user);
    }
  }

  // ============ DÉCONNEXION ============

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  // ============ HELPERS ============

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
}
