import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmpresaService } from '../../../services/empresa.service';
import { AuthService } from '../../../services/auth.service';
import { EmpresaGestora } from '../../../models/models';

@Component({
  selector: 'app-empresas-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './empresas-list.component.html',
  styleUrls: ['./empresas-list.component.scss']
})
export class EmpresasListComponent implements OnInit {
  private empresaService = inject(EmpresaService);
  private fb = inject(FormBuilder);
  authService = inject(AuthService);

  empresas: EmpresaGestora[] = [];
  isLoading = true;
  mostrarModal = false;
  empresaForm!: FormGroup;
  successMessage = '';
  errorMessage = '';

  ngOnInit(): void {
    this.initForm();
    this.cargarEmpresas();
  }

  initForm(): void {
    this.empresaForm = this.fb.group({
      ruc: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
      razonSocial: ['', [Validators.required, Validators.minLength(3)]],
      registroAutorizacion: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.email]],
      telefono: [''],
      direccion: ['']
    });
  }

  cargarEmpresas(): void {
    this.isLoading = true;
    this.empresaService.getAll().subscribe({
      next: (data) => {
        this.empresas = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Error al cargar empresas gestoras desde la API.';
      }
    });
  }

  guardarEmpresa(): void {
    if (this.empresaForm.invalid) {
      this.empresaForm.markAllAsTouched();
      return;
    }

    this.empresaService.create(this.empresaForm.value).subscribe({
      next: () => {
        this.mostrarModal = false;
        this.empresaForm.reset();
        this.successMessage = 'Empresa Gestora autorizada por MINAM registrada con éxito.';
        this.cargarEmpresas();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        alert(err.error?.mensaje || 'Error al guardar empresa operadora.');
      }
    });
  }
}
