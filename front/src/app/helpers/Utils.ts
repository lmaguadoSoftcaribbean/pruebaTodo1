import Swal, { SweetAlertIcon } from "sweetalert2";
import { Data } from "../models/interfaces.models";
import { AuthService } from "../services/auth.service";


class Utils {
  static postLogin (res:Data, authService:AuthService, router: any) {
    if(res.code === 1) {
      authService.token = res.data.token;
      this.guardarCookie(res.data);
      Swal.fire({
        position: 'top',
        icon: 'success',
        title: `Bienvenido ${res.data.username}`,
        showConfirmButton: false,
        timer: 1500
      })
      router.navigate(['/dashboard']);
    }
    else {
      this.mostrarAlerta('Clave o Usuario no valido.', 'warning');
    }
  }

  static postLogout (router: any) {
    this.eliminarCookie();
    this.mostrarAlerta('Se ha cerrado la sesion.', 'success');
    router.navigate(['/']);
  }

  static mostrarAlerta(texto: string, icono: SweetAlertIcon = 'info') {
    return Swal.fire({
      title: texto,
      icon: icono,
      customClass: {
        popup: 'tamanio_response'
      },
      target: 'body',

      showClass: {
        popup: 'animated fadeInDown faster'
      },
      hideClass: {
        popup: 'animated fadeOutUp faster'
      },
    });
  }


  static guardarCookie(usuario: any) {
    let obj = {
        token: usuario.token,
        nombre: usuario.username,
    }

    sessionStorage.setItem( 'token' ,JSON.stringify(obj));
  }

  static obtenerCookie() {
    let cookie = this.consultarCookie('token');
    if (cookie) {
      return JSON.parse(cookie);
    }
    return null;
  }

  static consultarCookie(c_name: string) {
    return sessionStorage.getItem(c_name);
  }

  static eliminarCookie() {
    sessionStorage.removeItem('token');
  }
}

export default Utils;
