package co.com.csti.user.integration.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable{

	@Email
	private String email;

	@NotEmpty(message = "La contraseña es obligatoria")
	@Size(min = 2, max = 60, message = "El tamaño de la contraseña debe tener entre 6 y 60 carcateres")
	private String password;

}
