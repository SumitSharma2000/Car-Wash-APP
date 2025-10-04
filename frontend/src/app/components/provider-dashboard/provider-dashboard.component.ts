import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';

@Component({
  selector: 'app-provider-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './provider-dashboard.component.html',
  styleUrls: ['./provider-dashboard.component.css']
})
export class ProviderDashboardComponent implements OnInit {
  currentUser: User | null = null;
  searchTerm = '';
  statusFilter = '';
  selectedBooking: any = null;
  showNotifications = false;
  showPayments = false;
  notificationCount = 3;

  stats = {
    currentBookings: 8,
    pendingBookings: 12,
    completedBookings: 156,
    totalEarnings: 4250,
    cancelledBookings: 5,
    todayBookings: 8,
    todayRevenue: 320
  };

  bookings = [
    {
      id: 'CW001',
      customerName: 'John Smith',
      phone: '+1 (555) 123-4567',
      email: 'john@email.com',
      serviceType: 'Premium Wash',
      status: 'PENDING',
      date: '2024-01-15',
      time: '10:00 AM',
      location: '123 Main St, Downtown',
      price: 25
    },
    {
      id: 'CW002',
      customerName: 'Sarah Johnson',
      phone: '+1 (555) 234-5678',
      email: 'sarah@email.com',
      serviceType: 'Full Detail',
      status: 'ACTIVE',
      date: '2024-01-15',
      time: '11:30 AM',
      location: '456 Oak Ave, Midtown',
      price: 45
    },
    {
      id: 'CW003',
      customerName: 'Mike Davis',
      phone: '+1 (555) 345-6789',
      email: 'mike@email.com',
      serviceType: 'Basic Wash',
      status: 'COMPLETED',
      date: '2024-01-14',
      time: '2:00 PM',
      location: '789 Pine St, Uptown',
      price: 15
    },
    {
      id: 'CW004',
      customerName: 'Emily Wilson',
      phone: '+1 (555) 456-7890',
      email: 'emily@email.com',
      serviceType: 'Premium Wash',
      status: 'ACCEPTED',
      date: '2024-01-15',
      time: '3:00 PM',
      location: '321 Elm St, Westside',
      price: 25
    }
  ];

  notifications = [
    {
      icon: 'fas fa-bell',
      message: 'New booking received from John Smith',
      time: '2 minutes ago'
    },
    {
      icon: 'fas fa-dollar-sign',
      message: 'Payment confirmed for booking #CW003',
      time: '15 minutes ago'
    },
    {
      icon: 'fas fa-star',
      message: 'New 5-star review received',
      time: '1 hour ago'
    }
  ];

  toasts: any[] = [];

  chartData = [
    { label: 'Pending', value: 12, color: '#ffa500' },
    { label: 'Active', value: 8, color: '#00d4ff' },
    { label: 'Completed', value: 156, color: '#00ff88' },
    { label: 'Cancelled', value: 5, color: '#ff4757' }
  ];

  revenueData = [
    { day: 'Mon', amount: 280 },
    { day: 'Tue', amount: 320 },
    { day: 'Wed', amount: 180 },
    { day: 'Thu', amount: 420 },
    { day: 'Fri', amount: 380 },
    { day: 'Sat', amount: 520 },
    { day: 'Sun', amount: 450 }
  ];

  maxRevenue = Math.max(...this.revenueData.map(d => d.amount));

  constructor(private readonly authService: AuthService, private readonly router: Router) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.simulateRealTimeUpdates();
  }

  private getSecureRandom(): number {
    const array = new Uint32Array(1);
    crypto.getRandomValues(array);
    return array[0] / (0xffffffff + 1);
  }

  get filteredBookings() {
    return this.bookings.filter(booking => {
      const matchesSearch = !this.searchTerm || 
        booking.customerName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        booking.id.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesStatus = !this.statusFilter || booking.status === this.statusFilter;
      
      return matchesSearch && matchesStatus;
    });
  }

  simulateRealTimeUpdates() {
    // Simulate new bookings every 30 seconds
    setInterval(() => {
      if (this.getSecureRandom() > 0.7) {
        this.addNewBooking();
      }
    }, 30000);
  }

  addNewBooking() {
    const customers = ['Alex Brown', 'Lisa Garcia', 'Tom Wilson', 'Anna Lee'];
    const services = ['Basic Wash', 'Premium Wash', 'Full Detail'];
    const locations = ['Downtown Plaza', 'Mall Parking', 'Office Complex', 'Residential Area'];
    
    const newBooking = {
      id: 'CW' + String(Date.now()).slice(-3),
      customerName: customers[Math.floor(this.getSecureRandom() * customers.length)],
      phone: '+1 (555) ' + Math.floor(this.getSecureRandom() * 900 + 100) + '-' + Math.floor(this.getSecureRandom() * 9000 + 1000),
      email: 'customer@email.com',
      serviceType: services[Math.floor(this.getSecureRandom() * services.length)],
      status: 'PENDING',
      date: new Date().toISOString().split('T')[0],
      time: new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}),
      location: locations[Math.floor(this.getSecureRandom() * locations.length)],
      price: [15, 25, 45][Math.floor(this.getSecureRandom() * 3)]
    };

    this.bookings.unshift(newBooking);
    this.stats.pendingBookings++;
    this.showToast('New booking received from ' + newBooking.customerName, 'info', 'fas fa-bell');
    this.notificationCount++;
  }

  simulateBooking() {
    this.addNewBooking();
    this.showToast('Test booking created successfully', 'success', 'fas fa-check');
  }

  filterBookings(status: string) {
    this.statusFilter = status;
  }

  openBookingDetails(booking: any) {
    this.selectedBooking = booking;
  }

  closeBookingDetails() {
    this.selectedBooking = null;
  }

  acceptBooking(booking: any, event: Event) {
    event.stopPropagation();
    booking.status = 'ACCEPTED';
    this.stats.pendingBookings--;
    this.stats.currentBookings++;
    this.showToast(`Booking ${booking.id} accepted`, 'success', 'fas fa-check');
  }

  rejectBooking(booking: any, event: Event) {
    event.stopPropagation();
    booking.status = 'CANCELLED';
    this.stats.pendingBookings--;
    this.stats.cancelledBookings++;
    this.showToast(`Booking ${booking.id} cancelled`, 'error', 'fas fa-times');
  }

  startService(booking: any, event: Event) {
    event.stopPropagation();
    booking.status = 'ACTIVE';
    this.showToast(`Service started for ${booking.customerName}`, 'info', 'fas fa-play');
  }

  completeService(booking: any, event: Event) {
    event.stopPropagation();
    booking.status = 'COMPLETED';
    this.stats.currentBookings--;
    this.stats.completedBookings++;
    this.stats.totalEarnings += booking.price;
    this.showToast(`Service completed for ${booking.customerName}`, 'success', 'fas fa-check-circle');
  }

  generateInvoice(booking: any, event: Event) {
    event.stopPropagation();
    
    const invoiceContent = `
CARWASH PRO - INVOICE
=====================

Invoice #: INV-${booking.id}
Date: ${new Date().toLocaleDateString()}

BILL TO:
${booking.customerName}
${booking.phone}
${booking.email}

SERVICE DETAILS:
Service: ${booking.serviceType}
Date: ${booking.date}
Time: ${booking.time}
Location: ${booking.location}

AMOUNT:
Service Charge: $${booking.price}
Tax (10%): $${(booking.price * 0.1).toFixed(2)}
Total: $${(booking.price * 1.1).toFixed(2)}

PAYMENT STATUS: PAID

Thank you for choosing CarWash Pro!

Contact: +1 (555) 123-4567
Email: billing@carwashpro.com
Website: www.carwashpro.com
    `;

    const blob = new Blob([invoiceContent], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `Invoice-${booking.id}.txt`;
    link.click();
    window.URL.revokeObjectURL(url);

    this.showToast(`Invoice generated for ${booking.customerName}`, 'success', 'fas fa-file-invoice');
  }

  generateReport() {
    const reportContent = `
CARWASH PRO - DAILY REPORT
==========================

Date: ${new Date().toLocaleDateString()}

SUMMARY:
- Total Bookings: ${this.bookings.length}
- Pending: ${this.stats.pendingBookings}
- Active: ${this.stats.currentBookings}
- Completed: ${this.stats.completedBookings}
- Cancelled: ${this.stats.cancelledBookings}
- Total Revenue: $${this.stats.totalEarnings}

BOOKINGS DETAIL:
${this.bookings.map(b => `${b.id} - ${b.customerName} - ${b.serviceType} - $${b.price} - ${b.status}`).join('\n')}

Generated on: ${new Date().toLocaleString()}
    `;

    const blob = new Blob([reportContent], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `Daily-Report-${new Date().toISOString().split('T')[0]}.txt`;
    link.click();
    window.URL.revokeObjectURL(url);

    this.showToast('Daily report generated', 'success', 'fas fa-chart-line');
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications) {
      this.notificationCount = 0;
    }
  }

  toggleUserMenu() {
    // Implement user menu toggle
  }

  showToast(message: string, type: string, icon: string) {
    const toast = { message, type, icon, id: Date.now() };
    this.toasts.push(toast);
    
    setTimeout(() => {
      this.removeToast(toast);
    }, 5000);
  }

  removeToast(toast: any) {
    const index = this.toasts.indexOf(toast);
    if (index > -1) {
      this.toasts.splice(index, 1);
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}