import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ResiduoService } from '../../../services/residuo.service';
import { AuthService } from '../../../services/auth.service';
import { AlmacenService } from '../../../services/almacen.service';
import { EmpresaService } from '../../../services/empresa.service';
import {
  Almacen,
  CategoriaRAEE,
  EmpresaGestora,
  EstadoResiduo,
  ResiduoElectronico
} from '../../../models/models';

@Component({
  selector: 'app-residuos-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule],
  templateUrl: './residuos-list.component.html',
  styleUrls: ['./residuos-list.component.scss']
})
export class ResiduosListComponent implements OnInit {
  private residuoService = inject(ResiduoService);
  private almacenService = inject(AlmacenService);
  private empresaService = inject(EmpresaService);
  private fb = inject(FormBuilder);
  authService = inject(AuthService);

  residuos: ResiduoElectronico[] = [];
  almacenes: Almacen[] = [];
  empresas: EmpresaGestora[] = [];
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  // Filtros
  filtroEstado: EstadoResiduo | '' = '';
  filtroCategoria: CategoriaRAEE | '' = '';
  filtroTermino = '';
  filtroFechaInicio = '';
  filtroFechaFin = '';
  mostrarFiltroFechas = false;

  // Enums para desplegables
  estados = Object.values(EstadoResiduo);
  categorias = Object.values(CategoriaRAEE);

  // Modal de cambio de estado
  mostrarModalEstado = false;
  residuoSeleccionado: ResiduoElectronico | null = null;
  estadoForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
    this.cargarDatos();
    this.cargarCatalogos();
  }

  initForm(): void {
    this.estadoForm = this.fb.group({
      nuevoEstado: ['', Validators.required],
      responsable: [this.authService.currentUserSignal()?.nombreCompleto || '', Validators.required],
      observaciones: ['', Validators.required],
      almacenId: [null],
      empresaGestoraId: [null]
    });
  }

  cargarDatos(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Uso de consulta JPQL personalizada multicriterio
    this.residuoService.busquedaAvanzada(
      this.filtroEstado,
      this.filtroCategoria,
      this.filtroTermino
    ).subscribe({
      next: (data) => {
        this.residuos = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar los residuos desde la API.';
      }
    });
  }

  cargarCatalogos(): void {
    this.almacenService.getAll().subscribe({
      next: (alms) => (this.almacenes = alms),
      error: () => {}
    });

    this.empresaService.getAll().subscribe({
      next: (emps) => (this.empresas = emps),
      error: () => {}
    });
  }

  aplicarFiltroFechas(): void {
    if (!this.filtroFechaInicio || !this.filtroFechaFin) {
      this.errorMessage = 'Debe seleccionar tanto la fecha de inicio como la de fin para el filtro JPQL.';
      return;
    }

    this.isLoading = true;
    const inicioIso = `${this.filtroFechaInicio}T00:00:00`;
    const finIso = `${this.filtroFechaFin}T23:59:59`;

    this.residuoService.buscarPorFechas(inicioIso, finIso).subscribe({
      next: (data) => {
        this.residuos = data;
        this.isLoading = false;
        this.successMessage = `Se encontraron ${data.length} residuos en el rango seleccionado (JPQL).`;
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Error al ejecutar la consulta JPQL por rango de fechas.';
      }
    });
  }

  limpiarFiltros(): void {
    this.filtroEstado = '';
    this.filtroCategoria = '';
    this.filtroTermino = '';
    this.filtroFechaInicio = '';
    this.filtroFechaFin = '';
    this.cargarDatos();
  }

  abrirModalEstado(residuo: ResiduoElectronico): void {
    this.residuoSeleccionado = residuo;
    this.estadoForm.patchValue({
      nuevoEstado: '',
      responsable: this.authService.currentUserSignal()?.nombreCompleto || 'Operador RAEE',
      observaciones: '',
      almacenId: residuo.almacenId || null,
      empresaGestoraId: residuo.empresaGestoraId || null
    });
    this.mostrarModalEstado = true;
  }

  cerrarModalEstado(): void {
    this.mostrarModalEstado = false;
    this.residuoSeleccionado = null;
  }

  guardarCambioEstado(): void {
    if (this.estadoForm.invalid || !this.residuoSeleccionado) {
      this.estadoForm.markAllAsTouched();
      return;
    }

    const payload = this.estadoForm.value;
    this.residuoService.cambiarEstado(this.residuoSeleccionado.id, payload).subscribe({
      next: () => {
        this.cerrarModalEstado();
        this.successMessage = '¡Estado y trazabilidad actualizados exitosamente en la base de datos!';
        this.cargarDatos();
        setTimeout(() => (this.successMessage = ''), 3500);
      },
      error: (err) => {
        alert(err.error?.mensaje || 'Error al actualizar el estado del residuo.');
      }
    });
  }

  eliminarResiduo(id: number, codigo: string): void {
    if (!this.authService.isAdmin()) {
      alert('Acceso Denegado: Solo los usuarios con rol ADMIN pueden eliminar residuos.');
      return;
    }

    if (confirm(`¿Estás seguro de eliminar el residuo ${codigo}? Esta operación requiere rol ADMIN en el backend.`)) {
      this.residuoService.delete(id).subscribe({
        next: () => {
          this.successMessage = `Residuo ${codigo} eliminado exitosamente.`;
          this.cargarDatos();
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error: (err) => {
          this.errorMessage = err.error?.mensaje || 'Error al eliminar el residuo.';
        }
      });
    }
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

  formatearTexto(texto: string): string {
    return texto ? texto.replace(/_/g, ' ') : '';
  }
}
