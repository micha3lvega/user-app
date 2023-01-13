package co.com.csti.user.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.csti.user.integration.dto.LoginRequest;
import co.com.csti.user.integration.dto.UserDTO;
import co.com.csti.user.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/user")
public class UserController implements IUserService {

	private IUserService iUserService;

	public UserController(IUserService iUserService) {
		this.iUserService = iUserService;
	}

	@Override
	@GetMapping
	@Operation(summary = "Obtiene todos los usuarios que cumplen con la paginacion")
	public Page<UserDTO> findAll(Pageable pageable) {
		return iUserService.findAll(pageable);
	}

	@Override
	@GetMapping("/{id}")
	@Operation(summary = "Obtiene un usuario por su ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
			@ApiResponse(responseCode = "400", description = "Request invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content) })
	public UserDTO findByID(@PathVariable("id") String id) {
		return iUserService.findByID(id);
	}

	@Override
	@Operation(summary = "Obtiene un usuario por su email")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
			@ApiResponse(responseCode = "400", description = "Request invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content) })
	@GetMapping("/email/{email}")
	public UserDTO findByEmail(@PathVariable("email") String email) {
		return iUserService.findByEmail(email);
	}

	@Override
	@PostMapping
	@Operation(summary = "Crea un nuevo usuario")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario creado con exito"),
			@ApiResponse(responseCode = "400", description = "Request invalido", content = @Content),
			@ApiResponse(responseCode = "409", description = "Ya existe un usuario con el email enviado", content = @Content) })
	public UserDTO insert(@RequestBody @Valid UserDTO user) {
		return iUserService.insert(user);
	}

	@Override
	@PutMapping
	@Operation(summary = "Actualiza la informacion de un usuario", description = "No actualiza el correo de un usuario")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario actualizado con exito"),
			@ApiResponse(responseCode = "400", description = "Request invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "No existe un usuario con el id enviado", content = @Content) })
	public UserDTO update(@Valid @RequestBody UserDTO user) {
		return iUserService.update(user);
	}

	@Override
	@DeleteMapping("/{id}")
	@Operation(summary = "Inactiva el usuario con el id recibido")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario actualizado con exito"),
			@ApiResponse(responseCode = "404", description = "No existe un usuario con el id enviado", content = @Content) })
	public void disable(@PathVariable("id") String id) {
		iUserService.disable(id);
	}

	@Override
	@PostMapping("/active/{id}")
	@Operation(summary = "Activa el usuario con el id recibido")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario actualizado con exito"),
			@ApiResponse(responseCode = "400", description = "Request invalido", content = @Content),
			@ApiResponse(responseCode = "404", description = "No existe un usuario con el id enviado", content = @Content) })
	public void active(@PathVariable("id") String id) {
		iUserService.active(id);
	}

	@Override
	@PostMapping("/login")
	@Operation(summary = "Obtiene un usuario por su usuario y contraseña")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario y contraseña incorrectos"),
			@ApiResponse(responseCode = "400", description = "Request invalido", content = @Content),
			@ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectas", content = @Content) })
	public UserDTO login(@RequestBody @Valid LoginRequest loginRequest) {
		return iUserService.login(loginRequest);
	}

}
