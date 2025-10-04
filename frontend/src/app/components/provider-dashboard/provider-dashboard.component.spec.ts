import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, ActivatedRoute } from '@angular/router';
import { ProviderDashboardComponent } from './provider-dashboard.component';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

describe('ProviderDashboardComponent', () => {
  let component: ProviderDashboardComponent;
  let fixture: ComponentFixture<ProviderDashboardComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser', 'logout']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const activatedRouteSpy = jasmine.createSpyObj('ActivatedRoute', ['']);

    await TestBed.configureTestingModule({
      imports: [ProviderDashboardComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: activatedRouteSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProviderDashboardComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with current user on ngOnInit', () => {
    const mockUser = { email: 'provider@test.com', name: 'Provider', role: 'SERVICE_PROVIDER' as const };
    authService.getCurrentUser.and.returnValue(mockUser);

    component.ngOnInit();

    expect(component.currentUser).toEqual(mockUser);
  });

  describe('booking management', () => {
    let testBooking: any;

    beforeEach(() => {
      testBooking = {
        id: 'CW001',
        customerName: 'John Doe',
        status: 'PENDING',
        serviceType: 'Premium Wash',
        price: 25
      };
    });

    it('should accept booking', () => {
      const event = new Event('click');
      spyOn(event, 'stopPropagation');
      const initialPending = component.stats.pendingBookings;
      const initialCurrent = component.stats.currentBookings;

      component.acceptBooking(testBooking, event);

      expect(event.stopPropagation).toHaveBeenCalled();
      expect(testBooking.status).toBe('ACCEPTED');
      expect(component.stats.pendingBookings).toBe(initialPending - 1);
      expect(component.stats.currentBookings).toBe(initialCurrent + 1);
    });

    it('should reject booking', () => {
      const event = new Event('click');
      spyOn(event, 'stopPropagation');
      const initialPending = component.stats.pendingBookings;
      const initialCancelled = component.stats.cancelledBookings;

      component.rejectBooking(testBooking, event);

      expect(event.stopPropagation).toHaveBeenCalled();
      expect(testBooking.status).toBe('CANCELLED');
      expect(component.stats.pendingBookings).toBe(initialPending - 1);
      expect(component.stats.cancelledBookings).toBe(initialCancelled + 1);
    });

    it('should start service', () => {
      const event = new Event('click');
      spyOn(event, 'stopPropagation');

      component.startService(testBooking, event);

      expect(event.stopPropagation).toHaveBeenCalled();
      expect(testBooking.status).toBe('ACTIVE');
    });

    it('should complete service', () => {
      const event = new Event('click');
      spyOn(event, 'stopPropagation');
      const initialCurrent = component.stats.currentBookings;
      const initialCompleted = component.stats.completedBookings;
      const initialEarnings = component.stats.totalEarnings;

      component.completeService(testBooking, event);

      expect(event.stopPropagation).toHaveBeenCalled();
      expect(testBooking.status).toBe('COMPLETED');
      expect(component.stats.currentBookings).toBe(initialCurrent - 1);
      expect(component.stats.completedBookings).toBe(initialCompleted + 1);
      expect(component.stats.totalEarnings).toBe(initialEarnings + testBooking.price);
    });
  });

  describe('filtering', () => {
    beforeEach(() => {
      component.bookings = [
        { 
          id: '1', 
          status: 'PENDING', 
          customerName: 'John',
          phone: '555-0001',
          email: 'john@test.com',
          serviceType: 'Basic Wash',
          date: '2024-01-15',
          time: '10:00 AM',
          location: '123 Test St',
          price: 15
        },
        { 
          id: '2', 
          status: 'ACTIVE', 
          customerName: 'Jane',
          phone: '555-0002',
          email: 'jane@test.com',
          serviceType: 'Premium Wash',
          date: '2024-01-15',
          time: '11:00 AM',
          location: '456 Test Ave',
          price: 25
        },
        { 
          id: '3', 
          status: 'COMPLETED', 
          customerName: 'Bob',
          phone: '555-0003',
          email: 'bob@test.com',
          serviceType: 'Full Detail',
          date: '2024-01-14',
          time: '2:00 PM',
          location: '789 Test Blvd',
          price: 45
        }
      ];
    });

    it('should filter bookings by status', () => {
      component.filterBookings('PENDING');
      expect(component.statusFilter).toBe('PENDING');
    });

    it('should return all bookings when no filter', () => {
      component.statusFilter = '';
      component.searchTerm = '';
      
      expect(component.filteredBookings.length).toBe(3);
    });

    it('should filter bookings by status', () => {
      component.statusFilter = 'PENDING';
      component.searchTerm = '';
      
      expect(component.filteredBookings.length).toBe(1);
      expect(component.filteredBookings[0].status).toBe('PENDING');
    });

    it('should filter bookings by search term', () => {
      component.statusFilter = '';
      component.searchTerm = 'john';
      
      expect(component.filteredBookings.length).toBe(1);
      expect(component.filteredBookings[0].customerName).toBe('John');
    });

    it('should filter by both status and search term', () => {
      component.statusFilter = 'ACTIVE';
      component.searchTerm = 'jane';
      
      expect(component.filteredBookings.length).toBe(1);
      expect(component.filteredBookings[0].customerName).toBe('Jane');
      expect(component.filteredBookings[0].status).toBe('ACTIVE');
    });
  });

  describe('booking details panel', () => {
    it('should open booking details', () => {
      const booking = { id: 'CW001', customerName: 'John' };

      component.openBookingDetails(booking);

      expect(component.selectedBooking).toEqual(booking);
    });

    it('should close booking details', () => {
      component.selectedBooking = { id: 'CW001' };

      component.closeBookingDetails();

      expect(component.selectedBooking).toBeNull();
    });
  });

  describe('notifications', () => {
    it('should toggle notifications panel', () => {
      component.notificationCount = 5;
      
      component.toggleNotifications();

      expect(component.showNotifications).toBe(true);
      expect(component.notificationCount).toBe(0);

      component.toggleNotifications();
      expect(component.showNotifications).toBe(false);
    });

    it('should show toast notification', () => {
      component.showToast('Test message', 'success', 'fas fa-check');

      expect(component.toasts.length).toBe(1);
      expect(component.toasts[0].message).toBe('Test message');
      expect(component.toasts[0].type).toBe('success');
    });

    it('should remove toast notification', () => {
      const toast = { message: 'Test', type: 'info', icon: 'fas fa-info', id: 123 };
      component.toasts = [toast];

      component.removeToast(toast);

      expect(component.toasts.length).toBe(0);
    });
  });

  describe('invoice generation', () => {
    it('should generate invoice for booking', () => {
      const booking = {
        id: 'CW001',
        customerName: 'John Doe',
        phone: '555-1234',
        email: 'john@test.com',
        serviceType: 'Premium Wash',
        date: '2024-01-15',
        time: '10:00',
        location: '123 Test St',
        price: 25
      };
      const event = new Event('click');
      spyOn(event, 'stopPropagation');
      spyOn(window.URL, 'createObjectURL').and.returnValue('blob:url');
      spyOn(window.URL, 'revokeObjectURL');
      const linkSpy = jasmine.createSpyObj('a', ['click']);
      spyOn(document, 'createElement').and.returnValue(linkSpy);

      component.generateInvoice(booking, event);

      expect(event.stopPropagation).toHaveBeenCalled();
      expect(document.createElement).toHaveBeenCalledWith('a');
      expect(linkSpy.download).toBe('Invoice-CW001.txt');
      expect(linkSpy.click).toHaveBeenCalled();
    });
  });

  describe('report generation', () => {
    it('should generate daily report', () => {
      spyOn(window.URL, 'createObjectURL').and.returnValue('blob:url');
      spyOn(window.URL, 'revokeObjectURL');
      const linkSpy = jasmine.createSpyObj('a', ['click']);
      spyOn(document, 'createElement').and.returnValue(linkSpy);

      component.generateReport();

      expect(document.createElement).toHaveBeenCalledWith('a');
      expect(linkSpy.download).toContain('Daily-Report-');
      expect(linkSpy.click).toHaveBeenCalled();
    });
  });

  describe('simulate booking', () => {
    it('should add new test booking', () => {
      const initialBookingsCount = component.bookings.length;
      const initialPendingCount = component.stats.pendingBookings;

      component.simulateBooking();

      expect(component.bookings.length).toBe(initialBookingsCount + 1);
      expect(component.stats.pendingBookings).toBe(initialPendingCount + 1);
      expect(component.bookings[0].status).toBe('PENDING');
    });
  });

  describe('logout', () => {
    it('should logout and navigate to login', () => {
      component.logout();

      expect(authService.logout).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  describe('getSecureRandom', () => {
    it('should generate secure random number between 0 and 1', () => {
      spyOn(crypto, 'getRandomValues').and.callFake((array: any) => {
        array[0] = 0x80000000; // Half of max value
        return array;
      });

      const result = (component as any).getSecureRandom();

      expect(crypto.getRandomValues).toHaveBeenCalled();
      expect(result).toBeCloseTo(0.5, 1);
    });
  });

  describe('simulateRealTimeUpdates', () => {
    it('should call addNewBooking when random condition is met', () => {
      spyOn(component, 'addNewBooking');
      spyOn(component as any, 'getSecureRandom').and.returnValue(0.8);
      let intervalCallback: any;
      spyOn(window, 'setInterval').and.callFake((callback: any) => {
        intervalCallback = callback;
        return 123;
      });
      
      component.simulateRealTimeUpdates();
      if (intervalCallback) intervalCallback();
      
      expect(component.addNewBooking).toHaveBeenCalled();
    });
  });

  describe('showToast timeout', () => {
    it('should remove toast after timeout callback', () => {
      spyOn(component, 'removeToast');
      let timeoutCallback: any;
      spyOn(window, 'setTimeout').and.callFake((callback: any) => {
        timeoutCallback = callback;
        return 123;
      });
      
      component.showToast('Test message', 'info', 'fas fa-info');
      if (timeoutCallback) timeoutCallback();
      
      expect(component.removeToast).toHaveBeenCalled();
    });
  });
});