import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const authSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    guard = TestBed.inject(AuthGuard);
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  describe('canActivate', () => {
    it('should return true when user is authenticated', () => {
      // Arrange: User is authenticated
      authService.isAuthenticated.and.returnValue(true);

      // Act: Check if route can be activated
      const result = guard.canActivate();

      // Assert: Should allow access
      expect(result).toBe(true);
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('should return false and navigate to login when user is not authenticated', () => {
      // Arrange: User is not authenticated
      authService.isAuthenticated.and.returnValue(false);

      // Act: Check if route can be activated
      const result = guard.canActivate();

      // Assert: Should deny access and redirect to login
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should call authService.isAuthenticated exactly once', () => {
      // Arrange: Set up authentication state
      authService.isAuthenticated.and.returnValue(true);

      // Act: Check if route can be activated
      guard.canActivate();

      // Assert: Should check authentication status once
      expect(authService.isAuthenticated).toHaveBeenCalledTimes(1);
    });
  });
});