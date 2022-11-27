import { Component, OnInit } from '@angular/core';
import { Login } from 'src/app/models/interfaces.models';
import { SweetAlertIcon } from 'sweetalert2';
import Utils from '../../helpers/Utils';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usuario: Login = {
    username: '',
    password: ''
  }

  @BlockUI() blockUI!: NgBlockUI;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }


  logear() {
    if(this.validarDatosLogeo(this.usuario)) {
      const usuario = this.usuario;
      this.blockUI.start('Cargando...');
      this.authService.login(usuario).subscribe(res => {
        this.blockUI.stop();
        Utils.postLogin(res, this.authService, this.router);
      }, error => {
        this.blockUI.stop();
        this.mostrarAlerta('No se pudo iniciar sesión.', 'error');
      });
    }
  }

  validarDatosLogeo(usuario: Login) {
    if (usuario.username!.length <= 0) {
      this.mostrarAlerta('Por favor ingrese un usuario', 'warning');
      return false;
    }

    if (usuario.password!.length <= 0) {
      this.mostrarAlerta('Por favor ingrese una contraseña', 'warning');
      return false;
    }
    return true;
  }

  mostrarAlerta(texto: string, icono: SweetAlertIcon = 'info') {
    return Utils.mostrarAlerta(texto, icono);
  }

}
