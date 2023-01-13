package co.com.csti.user.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
		var userByEmail = repository.findByEmail(email);

		if (userByEmail == null) {
			throw new UserNotFoundException("No se encontro el usuario con el email: " + email);
		}

		return mapper.map(userByEmail, UserDTO.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see co.com.csti.user.services#insert(co.com.csti.user.integration.dto.User)
	 */
	@Override
	public UserDTO insert(UserDTO user) {

		// Buscar si ya existe el email
		var userByEmail = repository.findByEmail(user.getEmail());

		if (userByEmail != null) {
			throw new UserExistsException("Ya existe un usuario con el email: " + user.getEmail());
		}

		// Codificar la contraseña
		user.setPassword(encoder.encode(user.getPassword() + user.getEmail()));

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
	public void active(String id) {

		var userForDelete = repository.findById(id).orElseThrow(UserNotFoundException::new);
		userForDelete.setActive(true);
		repository.save(userForDelete);

	}

	/**
	 * Convierte una pagina de entidades a sus respectivos dtos
	 * @param users
	 * @return
	 */
	private Page<UserDTO> toPageObjectDto(Page<User> users) {
		return users.map(user -> mapper.map(user, UserDTO.class));
	}

}
