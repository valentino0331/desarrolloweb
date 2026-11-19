import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CambioEstadoRequest,
  CategoriaRAEE,
  EstadisticaEstado,
  EstadoResiduo,
  HistorialMovimiento,
  ResiduoElectronico,
  ResiduoRequest
} from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ResiduoService {
  private readonly apiUrl = 'http://localhost:8080/api/residuos';

  constructor(private http: HttpClient) {}

  getAll(estado?: EstadoResiduo, categoria?: CategoriaRAEE): Observable<ResiduoElectronico[]> {
    let params = new HttpParams();
    if (estado) params = params.set('estado', estado);
    if (categoria) params = params.set('categoria', categoria);
    return this.http.get<ResiduoElectronico[]>(this.apiUrl, { params });
  }

  getById(id: number): Observable<ResiduoElectronico> {
    return this.http.get<ResiduoElectronico>(`${this.apiUrl}/${id}`);
  }

  getByCodigo(codigo: string): Observable<ResiduoElectronico> {
    return this.http.get<ResiduoElectronico>(`${this.apiUrl}/codigo/${codigo}`);
  }

  getHistorial(id: number): Observable<HistorialMovimiento[]> {
    return this.http.get<HistorialMovimiento[]>(`${this.apiUrl}/${id}/historial`);
  }

  create(residuo: ResiduoRequest): Observable<ResiduoElectronico> {
    return this.http.post<ResiduoElectronico>(this.apiUrl, residuo);
  }

  update(id: number, residuo: ResiduoRequest): Observable<ResiduoElectronico> {
    return this.http.put<ResiduoElectronico>(`${this.apiUrl}/${id}`, residuo);
  }

  cambiarEstado(id: number, cambio: CambioEstadoRequest): Observable<ResiduoElectronico> {
    return this.http.patch<ResiduoElectronico>(`${this.apiUrl}/${id}/estado`, cambio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // =========================================================================
  // CONSUMO DE ENDPOINTS DE CONSULTAS PERSONALIZADAS JPQL (APF2 / APF3)
  // =========================================================================

  buscarPorFechas(inicio: string, fin: string): Observable<ResiduoElectronico[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<ResiduoElectronico[]>(`${this.apiUrl}/jpql/fechas`, { params });
  }

  busquedaAvanzada(estado?: EstadoResiduo | '', categoria?: CategoriaRAEE | '', termino?: string): Observable<ResiduoElectronico[]> {
    let params = new HttpParams();
    if (estado) params = params.set('estado', estado);
    if (categoria) params = params.set('categoria', categoria);
    if (termino && termino.trim()) params = params.set('termino', termino.trim());
    return this.http.get<ResiduoElectronico[]>(`${this.apiUrl}/jpql/busqueda-avanzada`, { params });
  }

  obtenerEstadisticas(): Observable<EstadisticaEstado[]> {
    return this.http.get<EstadisticaEstado[]>(`${this.apiUrl}/jpql/estadisticas`);
  }

  buscarPesados(almacenId: number, pesoMinimo: number = 10.0): Observable<ResiduoElectronico[]> {
    const params = new HttpParams().set('pesoMinimo', pesoMinimo.toString());
    return this.http.get<ResiduoElectronico[]>(`${this.apiUrl}/jpql/almacen/${almacenId}/pesados`, { params });
  }
}
