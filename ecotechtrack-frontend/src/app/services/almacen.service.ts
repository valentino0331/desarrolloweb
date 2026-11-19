import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Almacen } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AlmacenService {
  private readonly apiUrl = 'http://localhost:8080/api/almacenes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Almacen[]> {
    return this.http.get<Almacen[]>(this.apiUrl);
  }

  getById(id: number): Observable<Almacen> {
    return this.http.get<Almacen>(`${this.apiUrl}/${id}`);
  }

  create(almacen: Almacen): Observable<Almacen> {
    return this.http.post<Almacen>(this.apiUrl, almacen);
  }
}
