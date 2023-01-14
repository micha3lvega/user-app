package co.com.csti.user.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import co.com.csti.user.integration.dto.LoginRequest;
import co.com.csti.user.integration.dto.UserDTO;
import co.com.csti.user.integration.exception.UserExistsException;
import co.com.csti.user.integration.exception.UserNotFoundException;
import co.com.csti.user.model.entity.User;
import co.com.csti.user.repository.UserRepository;
import co.com.csti.user.services.IUserService;

@Service
@Validated
public class UserServices implements IUserService {

	private UserRepository repository;

	private ModelMapper mapper;

	private PasswordEncoder encoder;

	/**
	 * Default constructor
	 *
	 * @param repository
	 * @param mapper
	 * @param encoder
	 */
	public UserServices(UserRepository repository, ModelMapper mapper, PasswordEncoder encoder) {
		this.repository = repository;
		this.mapper = mapper;
		this.encoder = encoder;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#findAll(Pageable)
	 */
	@Override
	public Page<UserDTO> findAll(Pageable pageable) {
		var findAllUser = repository.findAll(pageable);
		return toPageObjectDto(findAllUser);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#findByID(String)
	 */
	@Override
	public UserDTO findByID(String id) {
		var user = repository.findById(id).orElseThrow(UserNotFoundException::new);
		return mapper.map(user, UserDTO.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#findByEmail(String)
	 */
	@Override
	public UserDTO findByEmail(String email) {

		// Buscar si ya existe el email
		var userByEmail = repository.findByEmail(email.toLowerCase());

		if (userByEmail == null) {
			throw new UserNotFoundException("No se encontro el usuario con el email: " + email);
		}

		return mapper.map(userByEmail, UserDTO.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#login(co.com.csti.user.integration.dto.
	 * LoginRequest)
	 *
	 */
	@Override
	public UserDTO login(LoginRequest loginRequest) {

		// Buscar el usuario por el email
		var userByEmail = repository.findByEmail(loginRequest.getEmail().toLowerCase());

		if (userByEmail == null) {
			throw new UserNotFoundException("Usuario o contraseña incorrectos");
		}

		// validar la contraseña
		var matches = encoder.matches(loginRequest.getPassword(), userByEmail.getPassword());

		if (matches) {
			return mapper.map(userByEmail, UserDTO.class);
		}

		throw new UserNotFoundException("Usuario o contraseña incorrectos");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#insert(co.com.csti.user.integration.dto.User)
	 */
	@Override
	public UserDTO insert(UserDTO user) {

		// Buscar si ya existe el email
		var userByEmail = repository.findByEmail(user.getEmail().toLowerCase());

		if (userByEmail != null) {
			throw new UserExistsException("Ya existe un usuario con el email: " + user.getEmail());
		}

		// Codificar la contraseña
		var rawPassword = encoder.encode(user.getPassword());
		user.setPassword(rawPassword);

		// volver el correo en minusculas
		user.setEmail(user.getEmail().toLowerCase());

		// Guardar el correo
		var newUser = repository.insert(mapper.map(user, User.class));
		return mapper.map(newUser, UserDTO.class);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#update(co.com.csti.user.integration.dto.User)
	 */
	@Override
	public UserDTO update(UserDTO user) {

		// Buscar si el usuario existe
		var userByID = findByID(user.getId());

		// No permitir que se actualice el correo
		user.setEmail(userByID.getEmail());

		// No permitir que se actualice la contraseña
		user.setPassword(user.getPassword());

		var newUser = repository.save(mapper.map(user, User.class));
		return mapper.map(newUser, UserDTO.class);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#disable(String)
	 */
	@Override
	public void disable(String id) {

		var userForDelete = repository.findById(id).orElseThrow(UserNotFoundException::new);
		userForDelete.setActive(false);
		repository.save(userForDelete);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#active(String)
	 */
	@Override
	public UserDTO active(String id) {

		var userForDelete = repository.findById(id).orElseThrow(UserNotFoundException::new);
		userForDelete.setActive(true);
		return mapper.map(repository.save(userForDelete), UserDTO.class);

	}

	/**
	 * Convierte una pagina de entidades a sus respectivos dtos
	 *
	 * @param users
	 * @return
	 */
	private Page<UserDTO> toPageObjectDto(Page<User> users) {
		return users.map(user -> mapper.map(user, UserDTO.class));
	}

}
