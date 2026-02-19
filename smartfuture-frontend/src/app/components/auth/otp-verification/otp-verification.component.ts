import { Component, OnInit, OnDestroy, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

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
  remainingTime: number = 300; // 5 minutes en secondes
  canResend: boolean = false;
  submitted: boolean = false;
  isVerifying: boolean = false;
  errorMessage: string = '';

  private timerInterval: any;

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Récupérer l'email depuis les query params
    this.route.queryParams.subscribe(params => {
      this.userEmail = params['email'] || 'example@email.com';
    });

    // Démarrer le timer
    this.startTimer();

    // Focus sur le premier input
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

    // Ne garder que les chiffres
    if (value && !/^\d$/.test(value)) {
      this.otpCode[index] = '';
      return;
    }

    // Passer au champ suivant si un chiffre est entré
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

    // Retour arrière : passer au champ précédent
    if (event.key === 'Backspace' && !input.value && index > 0) {
      const prevInput = input.previousElementSibling as HTMLInputElement;
      if (prevInput) {
        prevInput.focus();
        prevInput.select();
      }
    }

    // Flèche gauche : aller au champ précédent
    if (event.key === 'ArrowLeft' && index > 0) {
      event.preventDefault();
      const prevInput = input.previousElementSibling as HTMLInputElement;
      if (prevInput) {
        prevInput.focus();
        prevInput.select();
      }
    }

    // Flèche droite : aller au champ suivant
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

    // Focus sur le dernier champ rempli
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

  async verify() {
    this.submitted = true;

    if (!this.isOtpComplete()) {
      this.errorMessage = 'Veuillez entrer le code complet';
      return;
    }

    this.isVerifying = true;
    this.errorMessage = '';

    try {
      const code = this.otpCode.join('');

      // Simuler un appel API
      await this.simulateApiCall(code);

      // Si succès, naviguer vers la page de confirmation
      this.router.navigate(['/inscription/confirmation']);
    } catch (error) {
      this.errorMessage = 'Code invalide ou expiré. Veuillez réessayer.';
      this.otpCode = ['', '', '', '', '', ''];
      setTimeout(() => {
        const firstInput = document.querySelector('input[type="text"]') as HTMLInputElement;
        if (firstInput) {
          firstInput.focus();
        }
      }, 100);
    } finally {
      this.isVerifying = false;
    }
  }

  private simulateApiCall(code: string): Promise<void> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        // Simuler une vérification
        if (code === '123456') { // Code de test
          resolve();
        } else {
          reject(new Error('Invalid code'));
        }
      }, 1500);
    });
  }

  async resendCode() {
    if (!this.canResend) return;

    try {
      // Simuler l'envoi d'un nouveau code
      await new Promise(resolve => setTimeout(resolve, 1000));

      // Réinitialiser le timer
      this.remainingTime = 300;
      this.canResend = false;
      this.otpCode = ['', '', '', '', '', ''];
      this.errorMessage = '';

      if (this.timerInterval) {
        clearInterval(this.timerInterval);
      }
      this.startTimer();

      // Afficher un message de succès (pourrait être un toast)
      console.log('Code renvoyé avec succès');
    } catch (error) {
      this.errorMessage = 'Erreur lors de l\'envoi du code. Veuillez réessayer.';
    }
  }

  goBack() {
    this.router.navigate(['/inscription/informations']);
  }
}
