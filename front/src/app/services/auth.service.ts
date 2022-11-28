import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Data, Login, Usuario } from '../models/interfaces.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  urlBase = '';
  token = '';

  constructor(public httpClient: HttpClient) {
    this.urlBase = environment.baseUrl;
  }

  register (usuario: Usuario) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    const req = usuario;
    return this.httpClient.post<Data>(`${this.urlBase}/api/auth/register`, req, httpOptions)
  }

  login (credenciales: Login) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    const req = credenciales;
    return this.httpClient.post<Data>(`${this.urlBase}/api/auth/login`, req, httpOptions)
  }

}
