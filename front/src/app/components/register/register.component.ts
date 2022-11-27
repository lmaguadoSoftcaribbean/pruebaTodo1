import { Component, OnInit } from '@angular/core';
import { Usuario } from 'src/app/models/interfaces.models';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { SweetAlertIcon } from 'sweetalert2';
import Utils from 'src/app/helpers/Utils';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  usuario: Usuario = {
    username: '',
    password: '',
    email: ''
  }

  @BlockUI() blockUI!: NgBlockUI;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  registro() {
    if(this.validarDatosRegistro(this.usuario)) {
      const usuario = this.usuario;
      this.blockUI.start('Cargando...');
      this.authService.register(usuario).subscribe(res => {
        this.blockUI.stop();
        if(res.code === 0) {
          this.mostrarAlerta("Usuario no disponible.","warning");
        }
        if(res.code === 1 && res.message == 'User created successfully.'){
          this.mostrarAlerta("Usuario creado satisfactoriamente.","success");
          this.router.navigate(['/login']);
        }
      },error => {
        this.blockUI.stop();
        this.mostrarAlerta('No se pudo registrar el usuario.', 'error');
      });
    }
  }

  validarDatosRegistro(usuario: Usuario) {
    if (usuario.username!.length <= 0) {
      this.mostrarAlerta('Por favor ingrese un usuario', 'warning');
      return false;
    }

    if (usuario.password!.length <= 0) {
      this.mostrarAlerta('Por favor ingrese una contraseña', 'warning');
      return false;
    }

    if (usuario.email!.length <= 0) {
      this.mostrarAlerta('Por favor ingrese un correo electronico', 'warning');
      return false;
    }
    return true;
  }

  mostrarAlerta(texto: string, icono: SweetAlertIcon = 'info') {
    return Utils.mostrarAlerta(texto, icono);
  }

}
