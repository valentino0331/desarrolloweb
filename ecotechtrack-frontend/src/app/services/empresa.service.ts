import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmpresaGestora } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private readonly apiUrl = 'http://localhost:8080/api/empresas-gestoras';

  constructor(private http: HttpClient) {}

  getAll(): Observable<EmpresaGestora[]> {
    return this.http.get<EmpresaGestora[]>(this.apiUrl);
  }

  getById(id: number): Observable<EmpresaGestora> {
    return this.http.get<EmpresaGestora>(`${this.apiUrl}/${id}`);
  }

  create(empresa: EmpresaGestora): Observable<EmpresaGestora> {
    return this.http.post<EmpresaGestora>(this.apiUrl, empresa);
  }
}
