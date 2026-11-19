import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { ResiduoService } from '../../../services/residuo.service';
import { AlmacenService } from '../../../services/almacen.service';
import { EmpresaService } from '../../../services/empresa.service';
import { Almacen, CategoriaRAEE, EmpresaGestora, EstadoResiduo } from '../../../models/models';

@Component({
  selector: 'app-residuo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './residuo-form.component.html',
  styleUrls: ['./residuo-form.component.scss']
})
export class ResiduoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private residuoService = inject(ResiduoService);
  private almacenService = inject(AlmacenService);
  private empresaService = inject(EmpresaService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  residuoForm!: FormGroup;
  isEditMode = false;
  residuoId: number | null = null;
  isLoading = false;
  errorMessage = '';

  almacenes: Almacen[] = [];
  empresas: EmpresaGestora[] = [];
  categorias = Object.values(CategoriaRAEE);

  ngOnInit(): void {
    this.initForm();
    this.cargarCatalogos();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.residuoId = Number(idParam);
      this.cargarDatosResiduo(this.residuoId);
    }
  }

  initForm(): void {
    this.residuoForm = this.fb.group({
      codigoIdentificacion: [
        'RAEE-2026-',
        [Validators.required, Validators.minLength(5), Validators.maxLength(50)]
      ],
      descripcion: ['', [Validators.required, Validators.minLength(5)]],
      categoria: ['', Validators.required],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      numeroSerie: ['', Validators.required],
      pesoKg: [null, [Validators.required, Validators.min(0.01)]],
      almacenId: [null, Validators.required],
      empresaGestoraId: [null],
      observaciones: ['']
    });
  }

  cargarCatalogos(): void {
    this.almacenService.getAll().subscribe({
      next: (data) => (this.almacenes = data),
      error: () => {}
    });

    this.empresaService.getAll().subscribe({
      next: (data) => (this.empresas = data),
      error: () => {}
    });
  }

  cargarDatosResiduo(id: number): void {
    this.isLoading = true;
    this.residuoService.getById(id).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.residuoForm.patchValue({
          codigoIdentificacion: res.codigoIdentificacion,
          descripcion: res.descripcion,
          categoria: res.categoria,
          marca: res.marca,
          modelo: res.modelo,
          numeroSerie: res.numeroSerie,
          pesoKg: res.pesoKg,
          almacenId: res.almacenId,
          empresaGestoraId: res.empresaGestoraId,
          observaciones: res.observaciones
        });
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'No se pudo cargar la información del residuo.';
      }
    });
  }

  onSubmit(): void {
    if (this.residuoForm.invalid) {
      this.residuoForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    const payload = this.residuoForm.value;

    if (this.isEditMode && this.residuoId) {
      this.residuoService.update(this.residuoId, payload).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/residuos']);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err.error?.mensaje || 'Error al actualizar el residuo.';
        }
      });
    } else {
      this.residuoService.create(payload).subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/residuos']);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err.error?.mensaje || 'Error al registrar el nuevo residuo.';
        }
      });
    }
  }

  formatearTexto(texto: string): string {
    return texto ? texto.replace(/_/g, ' ') : '';
  }
}
