package co.com.csti.user.integration.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class UserDTO implements Serializable {

	private String id;

	@NotEmpty(message = "El nombre es obligatorio")
	@Size(min = 2, max = 60, message = "El tamaño del nombre debe ser entre 2 y 60 caracteres")
	private String name;

	@NotEmpty(message = "El apellido es obligatorio")
	@Size(min = 2, max = 60, message = "El tamaño del apellido debe ser entre 2 y 60 caracteres")
	private String lastName;

	@NotEmpty(message = "El numero de telefono es obligatorio")
	@Size(min = 10, max =  10, message = "El telefono debe ser de 10 digitos")
	private String mobileNumber;

	@Email
	private String email;

	@NotEmpty(message = "La contraseña es obligatoria")
	@Size(min = 2, max = 60, message = "El tamaño de la contraseña debe tener entre 6 y 60 carcateres")
	private String password;

	private Date createDate;

	private Date lastUpdate;

	private List<RolE> roles;

	@Builder.Default
	private boolean active = true;

}
