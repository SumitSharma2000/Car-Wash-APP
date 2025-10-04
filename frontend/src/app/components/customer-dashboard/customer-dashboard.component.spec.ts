import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, ActivatedRoute } from '@angular/router';
import { CustomerDashboardComponent } from './customer-dashboard.component';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

describe('CustomerDashboardComponent', () => {
  let component: CustomerDashboardComponent;
  let fixture: ComponentFixture<CustomerDashboardComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser', 'logout']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const activatedRouteSpy = jasmine.createSpyObj('ActivatedRoute', ['']);

    await TestBed.configureTestingModule({
      imports: [CustomerDashboardComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: activatedRouteSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerDashboardComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with current user on ngOnInit', () => {
    const mockUser = { email: 'test@test.com', name: 'Test User', role: 'CUSTOMER' as const };
    authService.getCurrentUser.and.returnValue(mockUser);

    component.ngOnInit();

    expect(component.currentUser).toEqual(mockUser);
    expect(component.minDate).toBeTruthy();
    expect(component.maxDate).toBeTruthy();
  });

  describe('booking functionality', () => {
    beforeEach(() => {
      component.ngOnInit();
    });

    it('should open booking form', () => {
      const service = { name: 'Basic Wash', price: 15 };

      component.openBookingForm(service);

      expect(component.showBookingForm).toBe(true);
      expect(component.selectedService).toEqual(service);
    });

    it('should close booking form and reset data', () => {
      component.showBookingForm = true;
      component.selectedService = { name: 'Test' };
      component.bookingData = { date: '2024-01-01', time: '10:00', address: 'Test', phone: '123' };

      component.closeBookingForm();

      expect(component.showBookingForm).toBe(false);
      expect(component.selectedService).toBeNull();
      expect(component.bookingData).toEqual({ date: '', time: '', address: '', phone: '' });
    });

    it('should submit booking successfully', () => {
      component.selectedService = { name: 'Premium Wash', price: 25 };
      component.bookingData = {
        date: '2024-01-15',
        time: '10:00',
        address: '123 Test St',
        phone: '555-1234'
      };
      const initialBookingsCount = component.bookings.length;

      component.submitBooking();

      expect(component.bookings.length).toBe(initialBookingsCount + 1);
      expect(component.bookings[0].serviceType).toBe('Premium Wash');
      expect(component.bookings[0].status).toBe('PENDING');
      expect(component.showSuccessModal).toBe(true);
      expect(component.showBookingForm).toBe(false);
    });
  });

  describe('calendar functionality', () => {
    beforeEach(() => {
      component.ngOnInit();
    });

    it('should generate calendar correctly', () => {
      component.generateCalendar();

      expect(component.currentMonth).toBeTruthy();
      expect(component.currentYear).toBeGreaterThan(2020);
      expect(component.calendarDays.length).toBe(42); // 6 weeks * 7 days
    });

    it('should navigate to previous month', () => {
      const initialMonth = component.calendarDate.getMonth();
      
      component.previousMonth();

      expect(component.calendarDate.getMonth()).toBe(initialMonth - 1);
    });

    it('should navigate to next month', () => {
      const initialMonth = component.calendarDate.getMonth();
      
      component.nextMonth();

      expect(component.calendarDate.getMonth()).toBe(initialMonth + 1);
    });

    it('should select date', () => {
      const day = {
        date: 15,
        fullDate: '2024-01-15',
        currentMonth: true,
        disabled: false,
        selected: false
      };

      component.selectDate(day);

      expect(day.selected).toBe(true);
      expect(component.bookingData.date).toBe('2024-01-15');
    });

    it('should not select disabled date', () => {
      const day = {
        date: 15,
        fullDate: '2024-01-15',
        currentMonth: true,
        disabled: true,
        selected: false
      };

      component.selectDate(day);

      expect(day.selected).toBe(false);
      expect(component.bookingData.date).toBe('');
    });
  });

  describe('time slot functionality', () => {
    it('should select time slot', () => {
      component.selectTimeSlot('10:00');

      expect(component.bookingData.time).toBe('10:00');
    });

    it('should have 24 time slots', () => {
      expect(component.timeSlots.length).toBe(24);
      expect(component.timeSlots).toContain('08:00');
      expect(component.timeSlots).toContain('23:00');
    });
  });

  describe('FAQ functionality', () => {
    it('should toggle FAQ item', () => {
      const initialState = component.faqs[0].open;

      component.toggleFaq(0);

      expect(component.faqs[0].open).toBe(!initialState);
    });
  });

  describe('logout', () => {
    it('should logout and navigate to login', () => {
      component.logout();

      expect(authService.logout).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('download receipt', () => {
    it('should download receipt for booking', () => {
      const booking = {
        id: 'CW001',
        serviceType: 'Basic Wash',
        date: '2024-01-15',
        time: '10:00',
        location: '123 Test St',
        phone: '555-1234',
        price: 15
      };

      spyOn(window.URL, 'createObjectURL').and.returnValue('blob:url');
      spyOn(window.URL, 'revokeObjectURL');
      const linkSpy = jasmine.createSpyObj('a', ['click']);
      spyOn(document, 'createElement').and.returnValue(linkSpy);

      component.downloadReceipt(booking);

      expect(document.createElement).toHaveBeenCalledWith('a');
      expect(linkSpy.download).toBe('CarWash-Receipt-CW001.txt');
      expect(linkSpy.click).toHaveBeenCalled();
    });
  });
});