import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        if (response.role === 'CUSTOMER') {
          this.router.navigate(['/customer-dashboard']);
        } else {
          this.router.navigate(['/provider-dashboard']);
        }
      },
      error: (error) => {
        alert('Login failed. Please check your credentials.');
      }
    });
  }

  onForgotPassword(event: Event) {
    event.preventDefault();
    this.router.navigate(['/forgot-password']);
  }
}