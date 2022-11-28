import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Data } from '../models/interfaces.models';
import Utils from 'src/app/helpers/Utils';

@Injectable({
  providedIn: 'root'
})
export class ProductosService {

  urlBase = '';
  token: any;

  constructor(public httpClient: HttpClient) {
    this.urlBase = environment.baseUrl;
    this.token = Utils.obtenerCookie();
  }

  obtenerProductos () {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.token.token}`
      })
    };
    return this.httpClient.get<Data>(`${this.urlBase}/api/product`, httpOptions)
  }

  obtenerProducto (code: string) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.token.token}`
      })
    };
    const req = code;
    return this.httpClient.get<Data>(`${this.urlBase}/api/product/${req}`, httpOptions)
  }

}
