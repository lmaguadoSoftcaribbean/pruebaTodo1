import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Utils from 'src/app/helpers/Utils';
import { Producto } from 'src/app/models/interfaces.models';
import { AuthService } from 'src/app/services/auth.service';
import { ProductosService } from 'src/app/services/productos.service';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { SweetAlertIcon } from 'sweetalert2';

@Component({
  selector: 'app-productos',
  templateUrl: './productos.component.html',
  styleUrls: ['./productos.component.css']
})
export class ProductosComponent implements OnInit {

  productos!: Array<Producto>;

  @BlockUI() blockUI!: NgBlockUI;

  constructor(private authService: AuthService, private router: Router, private productosService: ProductosService) { }

  ngOnInit(): void {
    this.validarAcceso();
    this.obtenerProductos();
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

  obtenerProductos () {
    this.blockUI.start('Cargando...');
    this.productosService.obtenerProductos().subscribe(res => {
      this.blockUI.stop();
      if(res.code === 1 && res.message == 'Request successfully.'){
        this.productos = res.data;
        this.mostrarAlerta('Productos cargados correctamente.', 'success');
      }
      else {
        this.mostrarAlerta('No se pudo cargar los productos correctamente.', 'error');
      }
    }, error => {
      this.blockUI.stop();
      this.mostrarAlerta('Error al cargar los productos.', 'error');
    });
  }

  mostrarAlerta(texto: string, icono: SweetAlertIcon = 'info') {
    return Utils.mostrarAlerta(texto, icono);
  }

}
