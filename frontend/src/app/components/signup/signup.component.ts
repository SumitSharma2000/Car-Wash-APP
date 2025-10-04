import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  userData = {
    name: '',
    email: '',
    password: '',
    role: '',
    phone: '',
    address: ''
  };
  
  // Track terms acceptance for form validation
  termsAccepted = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSignup() {
    this.authService.signup(this.userData).subscribe({
      next: (response) => {
        alert('Account created successfully! Please login.');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        alert('Signup failed. Please try again.');
      }
    });
  }
}