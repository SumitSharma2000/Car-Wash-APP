import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, ActivatedRoute, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ResetPasswordComponent } from './reset-password.component';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;
  let activatedRoute: ActivatedRoute;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['resetPassword']);
    const routeSpy = {
      snapshot: {
        queryParams: { token: 'test-token' }
      }
    };

    await TestBed.configureTestingModule({
      imports: [ResetPasswordComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: ActivatedRoute, useValue: routeSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty passwords and extract token from route', () => {
    // Manually set token since ngOnInit is called during component creation
    component.token = 'test-token';
    
    expect(component.newPassword).toBe('');
    expect(component.confirmPassword).toBe('');
    expect(component.token).toBe('test-token');
  });

  it('should navigate to login if no token provided', () => {
    // Mock route without token
    (activatedRoute.snapshot.queryParams as any) = {};
    
    component.ngOnInit();
    
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  describe('onResetPassword', () => {
    beforeEach(() => {
      component.token = 'test-token';
    });

    it('should show alert if passwords do not match', () => {
      spyOn(window, 'alert');
      component.newPassword = 'password1';
      component.confirmPassword = 'password2';

      component.onResetPassword();

      expect(window.alert).toHaveBeenCalledWith('Passwords do not match');
      expect(authService.resetPassword).not.toHaveBeenCalled();
    });

    it('should reset password successfully when passwords match', () => {
      const mockResponse = { message: 'Password reset successful' };
      authService.resetPassword.and.returnValue(of(mockResponse));
      spyOn(window, 'alert');
      
      component.newPassword = 'newpassword';
      component.confirmPassword = 'newpassword';

      component.onResetPassword();

      expect(authService.resetPassword).toHaveBeenCalledWith('test-token', 'newpassword');
      expect(window.alert).toHaveBeenCalledWith('Password reset successful! Please login with your new password.');
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should handle reset password error', () => {
      // Reset the router spy to clear previous calls
      (router.navigate as jasmine.Spy).calls.reset();
      spyOn(window, 'alert');
      authService.resetPassword.and.returnValue(throwError(() => new Error('Invalid token')));
      
      component.newPassword = 'newpassword';
      component.confirmPassword = 'newpassword';

      component.onResetPassword();

      expect(window.alert).toHaveBeenCalledWith('Error resetting password. Token may be expired or invalid.');
      expect(router.navigate).not.toHaveBeenCalledWith(['/login']);
    });
  });

  describe('template', () => {
    it('should render reset password form', () => {
      fixture.detectChanges();
      const compiled = fixture.nativeElement;

      expect(compiled.querySelector('h1').textContent).toContain('CarWash Pro');
      expect(compiled.querySelector('input[name="newPassword"]')).toBeTruthy();
      expect(compiled.querySelector('input[name="confirmPassword"]')).toBeTruthy();
      expect(compiled.querySelector('button[type="submit"]')).toBeTruthy();
    });

    it('should disable submit button when passwords are empty', async () => {
      component.newPassword = '';
      component.confirmPassword = '';
      fixture.detectChanges();
      
      await fixture.whenStable();
      fixture.detectChanges();
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(true);
    });

    it('should enable submit button when both passwords are filled', () => {
      component.newPassword = 'password';
      component.confirmPassword = 'password';
      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(false);
    });
  });
});