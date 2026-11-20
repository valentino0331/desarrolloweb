import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ResiduoService } from '../../../services/residuo.service';
import { AuthService } from '../../../services/auth.service';
import { EstadoResiduo, HistorialMovimiento, ResiduoElectronico } from '../../../models/models';

@Component({
  selector: 'app-residuo-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './residuo-detail.component.html',
  styleUrls: ['./residuo-detail.component.scss']
})
export class ResiduoDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private residuoService = inject(ResiduoService);
  authService = inject(AuthService);

  residuo: ResiduoElectronico | null = null;
  historial: HistorialMovimiento[] = [];
  isLoading = true;
  errorMessage = '';

  etapas = [
    { key: EstadoResiduo.DECLARADO_BAJA, label: 'Baja Registrada', icon: 'bi-clipboard-x' },
    { key: EstadoResiduo.ALMACENADO_TEMPORAL, label: 'En Custodia', icon: 'bi-archive' },
    { key: EstadoResiduo.SOLICITUD_RECOJO, label: 'Solicitud Recojo', icon: 'bi-bell' },
    { key: EstadoResiduo.EN_TRANSITO, label: 'En Tránsito', icon: 'bi-truck' },
    { key: EstadoResiduo.ENTREGADO_GESTOR, label: 'En Planta Gestor', icon: 'bi-building-check' },
    { key: EstadoResiduo.VALORIZADO, label: 'Valorizado / Final', icon: 'bi-recycle' }
  ];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.cargarDetalle(id);
    }
  }

  cargarDetalle(id: number): void {
    this.isLoading = true;
    this.residuoService.getById(id).subscribe({
      next: (res) => {
        this.residuo = res;
        this.cargarHistorial(id);
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'No se encontró el residuo electrónico con el ID indicado.';
      }
    });
  }

  cargarHistorial(id: number): void {
    this.residuoService.getHistorial(id).subscribe({
      next: (hist) => {
        this.historial = hist;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  getEtapaIndex(estado: EstadoResiduo): number {
    const orden = [
      EstadoResiduo.DECLARADO_BAJA,
      EstadoResiduo.ALMACENADO_TEMPORAL,
      EstadoResiduo.SOLICITUD_RECOJO,
      EstadoResiduo.EN_TRANSITO,
      EstadoResiduo.ENTREGADO_GESTOR,
      EstadoResiduo.VALORIZADO,
      EstadoResiduo.DISPOSICION_FINAL
    ];
    return orden.indexOf(estado);
  }

  getEtapaStatus(index: number): 'completed' | 'active' | 'pending' {
    if (!this.residuo) return 'pending';
    const currentIndex = this.getEtapaIndex(this.residuo.estado);
    if (index < currentIndex) return 'completed';
    if (index === currentIndex) return 'active';
    return 'pending';
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

  formatearTexto(texto?: string): string {
    return texto ? texto.replace(/_/g, ' ') : '';
  }
}
