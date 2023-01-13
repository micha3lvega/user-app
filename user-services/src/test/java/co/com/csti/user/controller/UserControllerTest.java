package co.com.csti.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import co.com.csti.user.integration.dto.UserDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Autowired
	private UserController controller;

	private TestRestTemplate restTemplate;

	@LocalServerPort
	int randomServerPort;

	private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

	String url;

	@BeforeEach
	void before() {
		restTemplate = new TestRestTemplate();

		url = "http://localhost:" + randomServerPort + "/api/v1/user";
		log.info("url: {}", url);

	}

	@Test
	@DisplayName("Valida que al enviar un paginado se retornen solo el numero de registros solictados")
	void testPageable() {

		// Generar el tamaño de la pagina al azar
		var rand = new Random();
		var size = rand.nextInt(20);

		var pageable = PageRequest.of(0, size);

		var page = controller.findAll(pageable);

		assertEquals(size, page.getNumberOfElements());

	}

	@Test
	@DisplayName("Valida que al no ingresar un email se genere error")
	void testErrorUserInvalidEmail() {

		// No email
		var user = UserDTO.builder().name("name").lastName("lastname").mobileNumber("3000000000").password("123456")
				.build();

		var exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			controller.insert(user);
		});

		Assertions.assertEquals("insert.user.email: El correo es obligatorio", exception.getMessage());

	}

	@Test
	@DisplayName("Valida que si el formato del correo no es correo sea invalido")
	void testErrorUserInvalidEmailFormat() {

		// No email
		var user = UserDTO.builder().name("name").email("email").lastName("lastname").mobileNumber("3000000000")
				.password("123456").build();

		var exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			controller.insert(user);
		});

		Assertions.assertEquals("insert.user.email: debe ser una dirección de correo electrónico con formato correcto",
				exception.getMessage());

	}

	@Test
	@DisplayName("Valida que al no ingresar un nombre se genere error")
	void testErrorUserInvalidName() {

		// No email
		var user = UserDTO.builder().email("me@example.com").lastName("lastname").mobileNumber("3000000000")
				.password("123456").build();

		var exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			controller.insert(user);
		});

		Assertions.assertEquals("insert.user.name: El nombre es obligatorio", exception.getMessage());

	}

	@Test
	@DisplayName("Valida que al no ingresar un apellido se genere error")
	void testErrorUserInvalidLastName() {

		// No email
		var user = UserDTO.builder().email("me@example.com").name("name").mobileNumber("3000000000").password("123456")
				.build();

		var exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			controller.insert(user);
		});

		Assertions.assertEquals("insert.user.lastName: El apellido es obligatorio", exception.getMessage());

	}

	@Test
	@DisplayName("Valida que un usuario se cree correctamente")
	void tesAddUserSuccess() {

		var randomEmail = System.currentTimeMillis() + "@email.com";

		var user = UserDTO.builder().email(randomEmail).name("name").lastName("lastname").mobileNumber("3000000000")
				.password("123456").build();

		var newUser = controller.insert(user);

		assertNotNull(newUser);
		assertNotNull(newUser.getId());

		// Buscar si se añadio correctamente
		var findUser = controller.findByID(newUser.getId());

		assertNotNull(findUser);
		assertNotNull(findUser.getId());

	}

	@Test
	@DisplayName("Valida que un usuario se actualice correctamente")
	void tesUpdateUserSuccess() {

		var randomEmail = System.currentTimeMillis() + "@email.com";

		var user = UserDTO.builder().email(randomEmail).name("name").lastName("lastname").mobileNumber("3000000000")
				.password("123456").build();

		var newUser = restTemplate.postForObject(url, user, UserDTO.class);

		assertNotNull(newUser);
		assertNotNull(newUser.getId());
		assertEquals(randomEmail, user.getEmail());

		var newname = "newname";
		newUser.setName(newname);

		var headers = new HttpHeaders();
		var requestEntity = new HttpEntity<>(newUser, headers);
		var updateUser = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, UserDTO.class);

		log.info("updateUser: {}", updateUser);

		assertNotNull(updateUser);
		assertNotNull(updateUser.getBody());
		assertNotNull(updateUser.getBody().getId());
		assertEquals(newname, updateUser.getBody().getName());

	}

	@Test
	@DisplayName("Prueba para validar si se inactiva el usuario")
	void testInactive() {

		// Buscar el usuario por el correo
		var findUser = restTemplate.getForObject(url + "/email/me@example.com", UserDTO.class);

		assertNotNull(findUser);
		assertNotNull(findUser.getId());

		// Inactivar el usuario
		var disableUrl = url + "/" + findUser.getId();
		log.info("disableUrl: {}", disableUrl);
		restTemplate.delete(disableUrl);

		// Buscar nuevamente el usuario
		var inactiveUser = restTemplate.getForObject(url + "/" + findUser.getId(), UserDTO.class);

		// Validar que el estado del usuario haya cambiado
		assertNotNull(inactiveUser);
		assertNotNull(inactiveUser.getId());
		assertFalse(inactiveUser.isActive());

	}

	@Test
	@DisplayName("Prueba para validar si se activa el usuario")
	void testActive() {

		// Buscar el usuario por el correo
		var findUser = restTemplate.getForObject(url + "/email/me@example.com", UserDTO.class);

		// activa el usuario
		var activeUrl = url + "/active/" + findUser.getId();
		var activeUser = restTemplate.postForObject(activeUrl, null, UserDTO.class);

		// Validar que el estado del usuario haya cambiado
		assertNotNull(activeUser);
		assertNotNull(activeUser.getId());
		assertTrue(activeUser.isActive());

	}
}
