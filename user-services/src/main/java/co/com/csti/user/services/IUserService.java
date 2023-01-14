package co.com.csti.user.services;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.com.csti.user.integration.dto.LoginRequest;
import co.com.csti.user.integration.dto.UserDTO;

public interface IUserService {

	/**
	 * Metodo que obtiene un usuario por su id
	 *
	 * @param id
	 * @return el {@link UserDTO} asociado a su id
	 */
	public UserDTO findByID(String id);

	/**
	 * Retorna una {@link Page} con las retriciones de paginacion propocianadas por
	 * el objecto {@link Pageable}
	 *
	 * @param pageable
	 * @return
	 */
	public Page<UserDTO> findAll(Pageable pageable);

	/**
	 * Metodo para obtener los datos de un usuario por el usuario y la contraseña
	 * @param loginRequest
	 * @return
	 */
	public UserDTO login(LoginRequest loginRequest);

	/**
	 * Metodo que obtiene un usuario por su email
	 *
	 * @param email
	 * @return el {@link UserDTO} por su email
	 */
	public UserDTO findByEmail(String email);

	/**
	 * Metodo que guarda un usuario validando que no el email no este repetido
	 *
	 * @param user
	 * @return el {@link UserDTO} almacenado
	 */
	public UserDTO insert(@Valid UserDTO user);

	/**
	 * Metodo que actualiza un usuario validando que no el id exista
	 *
	 * @param user
	 * @return el {@link UserDTO} actualizado
	 */
	public UserDTO update(@Valid UserDTO user);

	/**
	 * Metodo que cambia el estado de un usuario a inactivo
	 *
	 * @param id
	 */
	public void disable(String id);

	/**
	 * Metodo que cambia el estado de un usuario a activo
	 *
	 * @param id
	 */
	public UserDTO active(String id);

}
