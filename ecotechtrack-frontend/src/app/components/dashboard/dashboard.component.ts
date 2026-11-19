import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ResiduoService } from '../../services/residuo.service';
import { AuthService } from '../../services/auth.service';
import { EstadisticaEstado, EstadoResiduo, ResiduoElectronico } from '../../models/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private residuoService = inject(ResiduoService);
  authService = inject(AuthService);

  estadisticas: EstadisticaEstado[] = [];
  residuosRecientes: ResiduoElectronico[] = [];
  isLoading = true;
  errorMessage = '';

  // Totales calculados
  totalResiduos = 0;
  totalPesoKg = 0;
  enAlmacen = 0;
  enTransitoOGestor = 0;

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Cargar estadísticas agregadas por JPQL
    this.residuoService.obtenerEstadisticas().subscribe({
      next: (stats) => {
        this.estadisticas = stats;
        this.totalResiduos = stats.reduce((acc, curr) => acc + curr.cantidad, 0);
        this.totalPesoKg = Math.round(stats.reduce((acc, curr) => acc + curr.pesoTotalKg, 0) * 100) / 100;

        const almStat = stats.find((s) => s.estado === EstadoResiduo.ALMACENADO_TEMPORAL);
        this.enAlmacen = almStat ? almStat.cantidad : 0;

        const transStat = stats.find((s) => s.estado === EstadoResiduo.EN_TRANSITO);
        const gestStat = stats.find((s) => s.estado === EstadoResiduo.ENTREGADO_GESTOR);
        this.enTransitoOGestor = (transStat ? transStat.cantidad : 0) + (gestStat ? gestStat.cantidad : 0);
      },
      error: (err) => {
        console.error('Error al cargar estadísticas JPQL:', err);
      }
    });

    // Cargar residuos para listado reciente
    this.residuoService.getAll().subscribe({
      next: (residuos) => {
        this.residuosRecientes = residuos.slice(0, 5);
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'No se pudieron cargar los datos del backend. Verifica que la API esté corriendo.';
      }
    });
  }

  getEstadoClass(estado: EstadoResiduo): string {
    switch (estado) {
      case EstadoResiduo.DECLARADO_BAJA: return 'badge-baja';
      case EstadoResiduo.ALMACENADO_TEMPORAL: return 'badge-almacen';
      case EstadoResiduo.SOLICITUD_RECOJO: return 'badge-recojo';
      case EstadoResiduo.EN_TRANSITO: return 'badge-transito';
      case EstadoResiduo.ENTREGADO_GESTOR: return 'badge-entregado';
      case EstadoResiduo.VALORIZADO: return 'badge-valorizado';
      case EstadoResiduo.DISPOSICION_FINAL: return 'badge-disposicion';
      default: return '';
    }
  }

  formatearEstado(estado: EstadoResiduo): string {
    return estado ? estado.replace(/_/g, ' ') : '';
  }
}
