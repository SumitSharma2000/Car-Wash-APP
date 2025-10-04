import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ForgotPasswordComponent } from './forgot-password.component';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

describe('ForgotPasswordComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['forgotPassword']);

    await TestBed.configureTestingModule({
      imports: [ForgotPasswordComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty email and emailSent false', () => {
    expect(component.email).toBe('');
    expect(component.emailSent).toBe(false);
  });

  describe('onResetPassword', () => {
    it('should send reset email successfully', () => {
      const mockResponse = { message: 'Reset email sent' };
      authService.forgotPassword.and.returnValue(of(mockResponse));
      component.email = 'test@test.com';

      component.onResetPassword();

      expect(authService.forgotPassword).toHaveBeenCalledWith('test@test.com');
      expect(component.emailSent).toBe(true);
      expect(component.email).toBe(''); // Email should be cleared after success
    });

    it('should handle reset email error', () => {
      spyOn(window, 'alert');
      authService.forgotPassword.and.returnValue(throwError(() => new Error('Email not found')));
      component.email = 'invalid@test.com';

      component.onResetPassword();

      expect(window.alert).toHaveBeenCalledWith('Error sending reset email. Please check your email address.');
      expect(component.emailSent).toBe(false);
    });
  });

  describe('template', () => {
    it('should render forgot password form', () => {
      fixture.detectChanges();
      const compiled = fixture.nativeElement;

      expect(compiled.querySelector('h1').textContent).toContain('CarWash Pro');
      expect(compiled.querySelector('input[type="email"]')).toBeTruthy();
      expect(compiled.querySelector('button[type="submit"]')).toBeTruthy();
    });

    it('should show success message when email is sent', () => {
      component.emailSent = true;
      fixture.detectChanges();
      const compiled = fixture.nativeElement;

      expect(compiled.querySelector('.success-message')).toBeTruthy();
    });

    it('should disable submit button when email is empty', async () => {
      component.email = '';
      fixture.detectChanges();
      
      await fixture.whenStable();
      fixture.detectChanges();
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(true);
    });

    it('should enable submit button when email is valid', () => {
      component.email = 'test@test.com';
      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(false);
    });
  });
});