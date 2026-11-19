import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AlmacenService } from '../../../services/almacen.service';
import { AuthService } from '../../../services/auth.service';
import { Almacen } from '../../../models/models';

@Component({
  selector: 'app-almacenes-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './almacenes-list.component.html',
  styleUrls: ['./almacenes-list.component.scss']
})
export class AlmacenesListComponent implements OnInit {
  private almacenService = inject(AlmacenService);
  private fb = inject(FormBuilder);
  authService = inject(AuthService);

  almacenes: Almacen[] = [];
  isLoading = true;
  mostrarModal = false;
  almacenForm!: FormGroup;
  successMessage = '';
  errorMessage = '';

  ngOnInit(): void {
    this.initForm();
    this.cargarAlmacenes();
  }

  initForm(): void {
    this.almacenForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.minLength(3)]],
      nombre: ['', Validators.required],
      sede: ['', Validators.required],
      direccion: [''],
      capacidadKg: [null, [Validators.required, Validators.min(10)]],
      responsable: ['', Validators.required]
    });
  }

  cargarAlmacenes(): void {
    this.isLoading = true;
    this.almacenService.getAll().subscribe({
      next: (data) => {
        this.almacenes = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar almacenes desde la API.';
      }
    });
  }

  guardarAlmacen(): void {
    if (this.almacenForm.invalid) {
      this.almacenForm.markAllAsTouched();
      return;
    }

    this.almacenService.create(this.almacenForm.value).subscribe({
      next: () => {
        this.mostrarModal = false;
        this.almacenForm.reset();
        this.successMessage = 'Almacén registrado exitosamente en Spring Boot.';
        this.cargarAlmacenes();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        alert(err.error?.mensaje || 'Error al guardar almacén.');
      }
    });
  }
}
