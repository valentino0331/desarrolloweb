export enum CategoriaRAEE {
  GRANDES_ELECTRODOMESTICOS = 'GRANDES_ELECTRODOMESTICOS',
  PEQUENOS_ELECTRODOMESTICOS = 'PEQUENOS_ELECTRODOMESTICOS',
  EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES = 'EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES',
  APARATOS_ELECTRONICOS_DE_CONSUMO = 'APARATOS_ELECTRONICOS_DE_CONSUMO',
  APARATOS_DE_ALUMBRADO = 'APARATOS_DE_ALUMBRADO',
  HERRAMIENTAS_ELECTRICAS = 'HERRAMIENTAS_ELECTRICAS',
  JUGUETES_Y_EQUIPOS_DEPORTIVOS = 'JUGUETES_Y_EQUIPOS_DEPORTIVOS',
  APARATOS_MEDICOS = 'APARATOS_MEDICOS',
  INSTRUMENTOS_DE_VIGILANCIA = 'INSTRUMENTOS_DE_VIGILANCIA',
  PANELES_FOTOVOLTAICOS = 'PANELES_FOTOVOLTAICOS',
  BATERIAS_Y_ACUMULADORES = 'BATERIAS_Y_ACUMULADORES'
}

export enum EstadoResiduo {
  DECLARADO_BAJA = 'DECLARADO_BAJA',
  ALMACENADO_TEMPORAL = 'ALMACENADO_TEMPORAL',
  SOLICITUD_RECOJO = 'SOLICITUD_RECOJO',
  EN_TRANSITO = 'EN_TRANSITO',
  ENTREGADO_GESTOR = 'ENTREGADO_GESTOR',
  VALORIZADO = 'VALORIZADO',
  DISPOSICION_FINAL = 'DISPOSICION_FINAL'
}

export enum Rol {
  ROLE_ADMIN = 'ROLE_ADMIN',
  ROLE_OPERADOR = 'ROLE_OPERADOR',
  ROLE_USER = 'ROLE_USER'
}

export interface Almacen {
  id?: number;
  codigo: string;
  nombre: string;
  sede: string;
  direccion?: string;
  capacidadKg: number;
  responsable?: string;
}

export interface EmpresaGestora {
  id?: number;
  ruc: string;
  razonSocial: string;
  registroAutorizacion: string;
  email?: string;
  telefono?: string;
  direccion?: string;
}

export interface HistorialMovimiento {
  id: number;
  estadoAnterior?: EstadoResiduo;
  estadoNuevo: EstadoResiduo;
  fechaMovimiento: string;
  responsable: string;
  observaciones?: string;
}

export interface ResiduoElectronico {
  id: number;
  codigoIdentificacion: string;
  descripcion: string;
  categoria: CategoriaRAEE;
  marca: string;
  modelo: string;
  numeroSerie: string;
  pesoKg: number;
  estado: EstadoResiduo;
  almacenId?: number;
  ubicacionAlmacen?: string;
  almacen?: Almacen;
  empresaGestoraId?: number;
  empresaGestora?: string;
  empresaGestoraDetalle?: EmpresaGestora;
  observaciones?: string;
  fechaRegistro: string;
  fechaActualizacion?: string;
}

export interface ResiduoRequest {
  codigoIdentificacion: string;
  descripcion: string;
  categoria: CategoriaRAEE;
  marca: string;
  modelo: string;
  numeroSerie: string;
  pesoKg: number;
  estado?: EstadoResiduo;
  almacenId: number;
  empresaGestoraId?: number | null;
  observaciones?: string;
}

export interface CambioEstadoRequest {
  nuevoEstado: EstadoResiduo;
  responsable: string;
  observaciones?: string;
  almacenId?: number;
  empresaGestoraId?: number | null;
}

export interface EstadisticaEstado {
  estado: EstadoResiduo;
  cantidad: number;
  pesoTotalKg: number;
}

export interface Usuario {
  id: number;
  username: string;
  nombreCompleto: string;
  email: string;
  activo: boolean;
  roles: Rol[];
  fechaCreacion?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  nombreCompleto: string;
  email: string;
  roles?: Rol[];
}

export interface JwtAuthResponse {
  token: string;
  tipoToken: string;
  id: number;
  username: string;
  nombreCompleto: string;
  email: string;
  roles: string[];
}
