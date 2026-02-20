import { Component, OnInit, OnDestroy, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-otp-verification',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './otp-verification.component.html'
})
export class OtpVerificationComponent implements OnInit, OnDestroy {
  @ViewChildren('otpInput') otpInputs!: QueryList<ElementRef>;

  otpCode: string[] = ['', '', '', '', '', ''];
  userEmail: string = '';
  remainingTime: number = 300;
  canResend: boolean = false;
  submitted: boolean = false;
  isVerifying: boolean = false;
  errorMessage: string = '';

  private timerInterval: any;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.userEmail = params['email'] || sessionStorage.getItem('otpEmail') || '';
    });

    this.startTimer();

    setTimeout(() => {
      const firstInput = document.querySelector('input[type="text"]') as HTMLInputElement;
      if (firstInput) {
        firstInput.focus();
      }
    }, 100);
  }

  ngOnDestroy() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  startTimer() {
    this.timerInterval = setInterval(() => {
      if (this.remainingTime > 0) {
        this.remainingTime--;
      } else {
        this.canResend = true;
        clearInterval(this.timerInterval);
      }
    }, 1000);
  }

  formatTime(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }

  onOtpInput(event: any, index: number) {
    const input = event.target;
    const value = input.value;

    if (value && !/^\d$/.test(value)) {
      this.otpCode[index] = '';
      return;
    }

    if (value && index < 5) {
      const nextInput = input.nextElementSibling as HTMLInputElement;
      if (nextInput) {
        nextInput.focus();
      }
    }

    this.errorMessage = '';
  }

  onKeyDown(event: KeyboardEvent, index: number) {
    const input = event.target as HTMLInputElement;

    if (event.key === 'Backspace' && !input.value && index > 0) {
      const prevInput = input.previousElementSibling as HTMLInputElement;
      if (prevInput) {
        prevInput.focus();
        prevInput.select();
      }
    }

    if (event.key === 'ArrowLeft' && index > 0) {
      event.preventDefault();
      const prevInput = input.previousElementSibling as HTMLInputElement;
      if (prevInput) {
        prevInput.focus();
        prevInput.select();
      }
    }

    if (event.key === 'ArrowRight' && index < 5) {
      event.preventDefault();
      const nextInput = input.nextElementSibling as HTMLInputElement;
      if (nextInput) {
        nextInput.focus();
        nextInput.select();
      }
    }
  }

  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedData = event.clipboardData?.getData('text') || '';
    const digits = pastedData.replace(/\D/g, '').slice(0, 6);

    digits.split('').forEach((digit, index) => {
      if (index < 6) {
        this.otpCode[index] = digit;
      }
    });

    const lastFilledIndex = digits.length - 1;
    if (lastFilledIndex >= 0 && lastFilledIndex < 6) {
      setTimeout(() => {
        const inputs = document.querySelectorAll('input[type="text"]');
        const targetInput = inputs[Math.min(lastFilledIndex, 5)] as HTMLInputElement;
        if (targetInput) {
          targetInput.focus();
        }
      }, 0);
    }
  }

  isOtpComplete(): boolean {
    return this.otpCode.every(code => code !== '');
  }

  verify() {
    this.submitted = true;

    if (!this.isOtpComplete()) {
      this.errorMessage = 'Veuillez entrer le code complet';
      return;
    }

    this.isVerifying = true;
    this.errorMessage = '';

    const code = this.otpCode.join('');

    this.authService.verifyOtp(this.userEmail, code).subscribe({
      next: (response) => {
        if (response.success) {
          sessionStorage.removeItem('otpEmail');
          this.router.navigate(['/inscription/confirmation']);
        }
      },
      error: (error) => {
        console.error('Erreur vérification OTP:', error);
        this.errorMessage = error.error?.message || 'Code invalide ou expiré';
        this.otpCode = ['', '', '', '', '', ''];
        this.isVerifying = false;

        setTimeout(() => {
          const firstInput = document.querySelector('input[type="text"]') as HTMLInputElement;
          if (firstInput) {
            firstInput.focus();
          }
        }, 100);
      },
      complete: () => {
        this.isVerifying = false;
      }
    });
  }

  resendCode() {
    if (!this.canResend) return;

    // TODO: Implémenter le renvoi de code OTP
    console.log('Renvoi du code OTP');

    this.remainingTime = 300;
    this.canResend = false;
    this.otpCode = ['', '', '', '', '', ''];
    this.errorMessage = '';

    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
    this.startTimer();
  }

  goBack() {
    this.router.navigate(['/inscription/informations']);
  }
}
