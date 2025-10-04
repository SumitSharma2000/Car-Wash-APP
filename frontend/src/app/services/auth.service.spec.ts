import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService, AuthResponse } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  describe('login', () => {
    it('should login user successfully', () => {
      const mockResponse: AuthResponse = {
        token: 'test-token',
        email: 'test@test.com',
        name: 'Test User',
        role: 'CUSTOMER'
      };

      service.login('test@test.com', 'password').subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(localStorage.getItem('token')).toBe('test-token');
        expect(localStorage.getItem('user')).toBeTruthy();
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ email: 'test@test.com', password: 'password' });
      req.flush(mockResponse);
    });

    it('should handle login error', () => {
      service.login('invalid@test.com', 'wrong').subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });
  });

  describe('signup', () => {
    it('should signup user successfully', () => {
      const userData = {
        name: 'New User',
        email: 'new@test.com',
        password: 'password',
        role: 'CUSTOMER'
      };

      const mockResponse: AuthResponse = {
        token: 'new-token',
        email: 'new@test.com',
        name: 'New User',
        role: 'CUSTOMER'
      };

      service.signup(userData).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/signup');
      expect(req.request.method).toBe('POST');
      req.flush(mockResponse);
    });
  });

  describe('isAuthenticated', () => {
    it('should return true when token exists', () => {
      localStorage.setItem('token', 'test-token');
      expect(service.isAuthenticated()).toBe(true);
    });

    it('should return false when no token', () => {
      expect(service.isAuthenticated()).toBe(false);
    });
  });

  describe('logout', () => {
    it('should clear localStorage and currentUser', () => {
      localStorage.setItem('token', 'test-token');
      localStorage.setItem('user', JSON.stringify({ name: 'Test' }));

      service.logout();

      expect(localStorage.getItem('token')).toBeNull();
      expect(localStorage.getItem('user')).toBeNull();
      expect(service.getCurrentUser()).toBeNull();
    });
  });

  describe('forgotPassword', () => {
    it('should send forgot password request', () => {
      service.forgotPassword('test@test.com').subscribe();

      const req = httpMock.expectOne('http://localhost:8080/api/auth/forgot-password');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ email: 'test@test.com' });
      req.flush({ message: 'Email sent' });
    });
  });

  describe('resetPassword', () => {
    it('should reset password with token', () => {
      service.resetPassword('reset-token', 'newpassword').subscribe();

      const req = httpMock.expectOne('http://localhost:8080/api/auth/reset-password');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ token: 'reset-token', newPassword: 'newpassword' });
      req.flush({ message: 'Password reset successful' });
    });
  });
});