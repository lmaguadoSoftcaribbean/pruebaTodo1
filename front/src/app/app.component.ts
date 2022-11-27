import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import Utils from './helpers/Utils';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'front';

  constructor(private router: Router, public location: Location, public authService: AuthService){
  }

  ngOnInit() {
    let obj = Utils.obtenerCookie();
    if(obj) {
      this.authService.token = obj.token;
      if(this.location.path() === '/login' || this.location.path() === '/') {
        this.router.navigate(['/dashboard']);
      }
    }
  }

}
