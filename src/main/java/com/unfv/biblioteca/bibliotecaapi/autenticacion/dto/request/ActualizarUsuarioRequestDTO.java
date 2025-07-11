
package com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarUsuarioRequestDTO {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombres;

    @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
    private String apellidoPaterno;

    @Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
    private String apellidoMaterno;

    @Email(message = "El email debe ser válido")
    private String email;

    @Size(min = 5, max = 15, message = "El teléfono debe tener entre 5 y 15 caracteres")
    private String telefono;

}
