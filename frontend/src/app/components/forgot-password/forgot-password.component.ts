import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email = '';
  emailSent = false;

  constructor(private router: Router, private authService: AuthService) {}

  onResetPassword() {
    this.authService.forgotPassword(this.email).subscribe({
      next: (response) => {
        this.emailSent = true;
        this.email = '';
      },
      error: (error) => {
        alert('Error sending reset email. Please check your email address.');
        this.emailSent = false;
      }
    });
  }
}