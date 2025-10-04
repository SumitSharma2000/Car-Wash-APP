import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-dashboard.component.html',
  styleUrls: ['./customer-dashboard.component.css']
})
export class CustomerDashboardComponent implements OnInit {
  currentUser: User | null = null;
  showBookingForm = false;
  showSuccessModal = false;
  selectedService: any = null;
  lastBookingId = '';
  
  bookingData = {
    date: '',
    time: '',
    address: '',
    phone: ''
  };
  
  minDate = '';
  maxDate = '';
  currentMonth = '';
  currentYear = 0;
  calendarDate = new Date();
  calendarDays: any[] = [];
  dayHeaders = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'];
  
  timeSlots = [
    '08:00', '09:00', '10:00', '11:00', '12:00', '13:00',
    '14:00', '15:00', '16:00', '17:00', '18:00', '19:00',
    '20:00', '21:00', '22:00', '23:00', '00:00', '01:00',
    '02:00', '03:00', '04:00', '05:00', '06:00', '07:00'
  ];

  services = [
    {
      name: 'Basic Wash',
      description: 'Exterior wash with soap and rinse',
      price: 15,
      icon: 'fas fa-car'
    },
    {
      name: 'Premium Wash',
      description: 'Exterior + Interior cleaning',
      price: 25,
      icon: 'fas fa-star'
    },
    {
      name: 'Full Detail',
      description: 'Complete detailing service',
      price: 45,
      icon: 'fas fa-gem'
    }
  ];

  bookings = [
    {
      id: 'CW001',
      serviceType: 'Premium Wash',
      status: 'PENDING',
      date: '2024-01-15',
      time: '10:00 AM',
      location: '123 Main St',
      phone: '+1 (555) 123-4567',
      price: 25
    },
    {
      id: 'CW002',
      serviceType: 'Basic Wash',
      status: 'COMPLETED',
      date: '2024-01-10',
      time: '2:00 PM',
      location: '456 Oak Ave',
      phone: '+1 (555) 123-4567',
      price: 15
    }
  ];

  faqs = [
    {
      question: 'How long does a car wash take?',
      answer: 'Basic wash takes 30 minutes, Premium wash takes 45 minutes, and Full Detail takes 2 hours.',
      open: false
    },
    {
      question: 'Do you provide mobile service?',
      answer: 'Yes, we come to your location for all our services.',
      open: false
    },
    {
      question: 'What payment methods do you accept?',
      answer: 'We accept cash, credit cards, and digital payments.',
      open: false
    },
    {
      question: 'Is my car insured during the service?',
      answer: 'Yes, all vehicles are fully insured during our service.',
      open: false
    }
  ];

  testimonials = [
    {
      name: 'John Smith',
      comment: 'Excellent service! My car looks brand new.',
      rating: 5
    },
    {
      name: 'Sarah Johnson',
      comment: 'Professional and reliable. Highly recommended!',
      rating: 5
    },
    {
      name: 'Mike Davis',
      comment: 'Great value for money. Will use again.',
      rating: 4
    }
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.setDateLimits();
    this.generateCalendar();
  }
  
  setDateLimits() {
    const today = new Date();
    const maxBookingDate = new Date();
    maxBookingDate.setDate(today.getDate() + 30);
    
    this.minDate = today.toISOString().split('T')[0];
    this.maxDate = maxBookingDate.toISOString().split('T')[0];
  }
  
  generateCalendar() {
    this.currentMonth = this.monthNames[this.calendarDate.getMonth()];
    this.currentYear = this.calendarDate.getFullYear();
    
    const firstDay = new Date(this.calendarDate.getFullYear(), this.calendarDate.getMonth(), 1);
    const lastDay = new Date(this.calendarDate.getFullYear(), this.calendarDate.getMonth() + 1, 0);
    const startDate = new Date(firstDay);
    startDate.setDate(startDate.getDate() - firstDay.getDay());
    
    this.calendarDays = [];
    const today = new Date();
    const maxDate = new Date();
    maxDate.setDate(today.getDate() + 30);
    
    for (let i = 0; i < 42; i++) {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);
      
      const isCurrentMonth = date.getMonth() === this.calendarDate.getMonth();
      const isDisabled = date < today || date > maxDate;
      const dateStr = date.toISOString().split('T')[0];
      
      this.calendarDays.push({
        date: date.getDate(),
        fullDate: dateStr,
        currentMonth: isCurrentMonth,
        disabled: isDisabled,
        selected: this.bookingData.date === dateStr
      });
    }
  }
  
  previousMonth() {
    this.calendarDate.setMonth(this.calendarDate.getMonth() - 1);
    this.generateCalendar();
  }
  
  nextMonth() {
    this.calendarDate.setMonth(this.calendarDate.getMonth() + 1);
    this.generateCalendar();
  }
  
  selectDate(day: any) {
    if (day.disabled || !day.currentMonth) return;
    
    this.calendarDays.forEach(d => d.selected = false);
    day.selected = true;
    this.bookingData.date = day.fullDate;
  }
  
  selectTimeSlot(slot: string) {
    this.bookingData.time = slot;
  }



  openBookingForm(service: any) {
    this.selectedService = service;
    this.showBookingForm = true;
  }
  
  closeBookingForm() {
    this.showBookingForm = false;
    this.selectedService = null;
    this.bookingData = { date: '', time: '', address: '', phone: '' };
  }
  
  submitBooking() {
    const newBooking = {
      id: 'CW' + String(Date.now()).slice(-3),
      serviceType: this.selectedService.name,
      status: 'PENDING',
      date: this.bookingData.date,
      time: this.bookingData.time,
      location: this.bookingData.address,
      phone: this.bookingData.phone,
      price: this.selectedService.price
    };
    
    this.bookings.unshift(newBooking);
    this.lastBookingId = newBooking.id;
    this.closeBookingForm();
    this.showSuccessModal = true;
  }
  
  closeSuccessModal() {
    this.showSuccessModal = false;
    this.scrollTo('status');
  }
  
  downloadReceipt(booking: any) {
    const receiptContent = `
      CARWASH PRO - RECEIPT
      =====================
      
      Booking ID: ${booking.id}
      Service: ${booking.serviceType}
      Date: ${booking.date}
      Time: ${booking.time}
      Location: ${booking.location}
      Phone: ${booking.phone}
      Amount: $${booking.price}
      Status: ${booking.status}
      
      Thank you for choosing CarWash Pro!
      
      Contact: +1 (555) 123-4567
      Email: info@carwashpro.com
    `;
    
    const blob = new Blob([receiptContent], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `CarWash-Receipt-${booking.id}.txt`;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  toggleFaq(index: number) {
    this.faqs[index].open = !this.faqs[index].open;
  }

  getStars(rating: number): number[] {
    return Array(rating).fill(0);
  }

  scrollTo(elementId: string) {
    const element = document.getElementById(elementId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}