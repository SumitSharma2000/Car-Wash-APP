import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty email and password', () => {
    expect(component.email).toBe('');
    expect(component.password).toBe('');
  });

  describe('onLogin', () => {
    it('should navigate to customer dashboard for customer role', () => {
      const mockResponse = {
        token: 'test-token',
        email: 'customer@test.com',
        name: 'Customer',
        role: 'CUSTOMER' as const
      };

      authService.login.and.returnValue(of(mockResponse));
      component.email = 'customer@test.com';
      component.password = 'password';

      component.onLogin();

      expect(authService.login).toHaveBeenCalledWith('customer@test.com', 'password');
      expect(router.navigate).toHaveBeenCalledWith(['/customer-dashboard']);
    });

    it('should navigate to provider dashboard for service provider role', () => {
      const mockResponse = {
        token: 'test-token',
        email: 'provider@test.com',
        name: 'Provider',
        role: 'SERVICE_PROVIDER' as const
      };

      authService.login.and.returnValue(of(mockResponse));
      component.email = 'provider@test.com';
      component.password = 'password';

      component.onLogin();

      expect(router.navigate).toHaveBeenCalledWith(['/provider-dashboard']);
    });

    it('should show alert on login failure', () => {
      spyOn(window, 'alert');
      authService.login.and.returnValue(throwError(() => new Error('Login failed')));

      component.onLogin();

      expect(window.alert).toHaveBeenCalledWith('Login failed. Please check your credentials.');
    });
  });

  describe('onForgotPassword', () => {
    it('should navigate to forgot password page', () => {
      const event = new Event('click');
      spyOn(event, 'preventDefault');

      component.onForgotPassword(event);

      expect(event.preventDefault).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/forgot-password']);
    });
  });

  describe('template', () => {
    it('should render login form', () => {
      fixture.detectChanges();
      const compiled = fixture.nativeElement;

      expect(compiled.querySelector('h1').textContent).toContain('CarWash Pro');
      expect(compiled.querySelector('input[type="email"]')).toBeTruthy();
      expect(compiled.querySelector('input[type="password"]')).toBeTruthy();
      expect(compiled.querySelector('button[type="submit"]')).toBeTruthy();
    });

    it('should disable submit button when form is invalid', async () => {
      component.email = '';
      component.password = '';
      fixture.detectChanges();
      
      // Wait for form validation to process
      await fixture.whenStable();
      fixture.detectChanges();
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(true);
    });

    it('should enable submit button when form is valid', () => {
      component.email = 'test@test.com';
      component.password = 'password';
      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(false);
    });
  });
});