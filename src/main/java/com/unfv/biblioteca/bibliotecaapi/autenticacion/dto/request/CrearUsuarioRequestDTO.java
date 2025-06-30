package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearUsuarioRequestDTO {

    @NotBlank(message = "El código universitario no puede estar vacío")
    @Size(max = 20, message = "El código universitario no puede tener más de 20 caracteres")
    private String codigoUniversitario;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido paterno no puede estar vacío")
    @Size(max = 50, message = "El apellido paterno no puede tener más de 50 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno no puede estar vacío")
    @Size(max = 50, message = "El apellido materno no puede tener más de 50 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String telefono;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "El ID del tipo de usuario no puede ser nulo")
    @Positive(message = "El ID del tipo de usuario debe ser un número positivo")
    private Long tipoUsuarioId;
}
