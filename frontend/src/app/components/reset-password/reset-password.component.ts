import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  newPassword = '';
  confirmPassword = '';
  token = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.token = this.route.snapshot.queryParams['token'] || '';
    if (!this.token) {
      this.router.navigate(['/login']);
    }
  }

  onResetPassword() {
    if (this.newPassword !== this.confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    this.authService.resetPassword(this.token, this.newPassword).subscribe({
      next: (response) => {
        alert('Password reset successful! Please login with your new password.');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        alert('Error resetting password. Token may be expired or invalid.');
      }
    });
  }
}