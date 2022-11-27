import { Component, OnInit } from '@angular/core';
import Utils from 'src/app/helpers/Utils';
import { AuthService } from 'src/app/services/auth.service';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  nombre = '';
  @BlockUI() blockUI!: NgBlockUI;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.validarAcceso();
    let cookie = Utils.obtenerCookie();
    if (cookie) {
      this.nombre = cookie.dsnombre;
    }
  }

  validarAcceso() {
    let token = this.authService.token;
    if (!token) {
      Utils.mostrarAlerta("No se ha iniciado sesión", 'warning');
      this.router.navigate(['/login']);
    }
  }

  cerrarSesion() {
    Utils.postLogout(this.router);
  }

}
