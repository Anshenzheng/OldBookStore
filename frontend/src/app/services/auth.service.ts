import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoginRequest, SignUpRequest, JwtResponse, User } from '../models/user.model';

const API_URL = 'http://localhost:8080/api/auth/';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<JwtResponse | null>;
  public currentUser: Observable<JwtResponse | null>;

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<JwtResponse | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): JwtResponse | null {
    return this.currentUserSubject.value;
  }

  login(loginRequest: LoginRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(API_URL + 'login', loginRequest)
      .pipe(
        map(response => {
          if (response && response.accessToken) {
            localStorage.setItem('currentUser', JSON.stringify(response));
            this.currentUserSubject.next(response);
          }
          return response;
        })
      );
  }

  register(signUpRequest: SignUpRequest): Observable<any> {
    return this.http.post(API_URL + 'register', signUpRequest);
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    const currentUser = this.currentUserValue;
    return currentUser ? currentUser.accessToken : null;
  }

  isLoggedIn(): boolean {
    return this.currentUserValue !== null;
  }

  isAdmin(): boolean {
    const currentUser = this.currentUserValue;
    return currentUser ? currentUser.role === 'ADMIN' : false;
  }

  isStudent(): boolean {
    const currentUser = this.currentUserValue;
    return currentUser ? currentUser.role === 'STUDENT' : false;
  }

  getCurrentUserId(): number | null {
    const currentUser = this.currentUserValue;
    return currentUser ? currentUser.userId : null;
  }
}
