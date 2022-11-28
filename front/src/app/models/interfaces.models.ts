export interface Usuario  {
  username?: string;
  password?: string;
  email?: string;
}

export interface Login {
  username?: string;
  password?: string;
}

export interface Data {
  status?: string;
  code?: number;
  message?: string;
  data?: any;
}

export interface Producto {
  code?: string;
  name?: string;
  unit?: number;
}
