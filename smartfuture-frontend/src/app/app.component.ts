import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Home } from './components/home/home.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Home],
  template: `<router-outlet></router-outlet>`,

})
export class AppComponent {
  title = 'SmartFuture - Formations Certifiantes';
}
