import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SignupComponent } from './signup.component';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

describe('SignupComponent', () => {
  let component: SignupComponent;
  let fixture: ComponentFixture<SignupComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['signup']);

    await TestBed.configureTestingModule({
      imports: [SignupComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SignupComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty userData and terms not accepted', () => {
    expect(component.userData).toEqual({
      name: '',
      email: '',
      password: '',
      role: '',
      phone: '',
      address: ''
    });
    expect(component.termsAccepted).toBe(false);
  });

  describe('onSignup', () => {
    beforeEach(() => {
      component.userData = {
        name: 'Test User',
        email: 'test@test.com',
        password: 'password',
        role: 'CUSTOMER',
        phone: '555-1234',
        address: '123 Test St'
      };
    });

    it('should signup successfully and navigate to login', () => {
      const mockResponse = {
        token: 'test-token',
        email: 'test@test.com',
        name: 'Test User',
        role: 'CUSTOMER' as const
      };

      authService.signup.and.returnValue(of(mockResponse));
      spyOn(window, 'alert');

      component.onSignup();

      expect(authService.signup).toHaveBeenCalledWith(component.userData);
      expect(window.alert).toHaveBeenCalledWith('Account created successfully! Please login.');
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should show alert on signup failure', () => {
      spyOn(window, 'alert');
      authService.signup.and.returnValue(throwError(() => new Error('Signup failed')));

      component.onSignup();

      expect(window.alert).toHaveBeenCalledWith('Signup failed. Please try again.');
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('template', () => {
    it('should render signup form', () => {
      fixture.detectChanges();
      const compiled = fixture.nativeElement;

      expect(compiled.querySelector('h1').textContent).toContain('CarWash Pro');
      expect(compiled.querySelector('input[name="name"]')).toBeTruthy();
      expect(compiled.querySelector('input[name="email"]')).toBeTruthy();
      expect(compiled.querySelector('input[name="password"]')).toBeTruthy();
      expect(compiled.querySelector('select[name="role"]')).toBeTruthy();
      expect(compiled.querySelector('button[type="submit"]')).toBeTruthy();
    });

    it('should disable submit button when form is invalid', async () => {
      component.userData = {
        name: '',
        email: '',
        password: '',
        role: '',
        phone: '',
        address: ''
      };
      fixture.detectChanges();
      
      // Wait for form validation to process
      await fixture.whenStable();
      fixture.detectChanges();
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(true);
    });

    it('should enable submit button when required fields are filled and terms accepted', () => {
      component.userData = {
        name: 'Test User',
        email: 'test@test.com',
        password: 'password',
        role: 'CUSTOMER',
        phone: '',
        address: ''
      };
      component.termsAccepted = true; // Terms must be accepted
      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(false);
    });

    it('should disable submit button when terms are not accepted', async () => {
      component.userData = {
        name: 'Test User',
        email: 'test@test.com',
        password: 'password',
        role: 'CUSTOMER',
        phone: '',
        address: ''
      };
      component.termsAccepted = false; // Terms not accepted
      fixture.detectChanges();
      
      // Wait for form validation to process
      await fixture.whenStable();
      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBe(true);
    });

    it('should have role options', () => {
      fixture.detectChanges();
      const roleSelect = fixture.nativeElement.querySelector('select[name="role"]');
      const options = roleSelect.querySelectorAll('option');

      expect(options.length).toBe(3); // Including default option
      expect(options[1].value).toBe('CUSTOMER');
      expect(options[2].value).toBe('SERVICE_PROVIDER');
    });
  });
});