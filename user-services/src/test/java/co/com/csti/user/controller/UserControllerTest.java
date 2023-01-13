package co.com.csti.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;

import co.com.csti.user.integration.dto.UserDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Autowired
	private UserController controller;

	@Test
	@DisplayName("Valida que al enviar un paginado se retornen solo el numero de registros solictados")
	void testPageable() {

		var pageable = PageRequest.of(0, 10);

		var findAll = controller.findAll(pageable);

		assertEquals(10, findAll.getNumberOfElements());

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

	}

	@Test
	@DisplayName("Valida que un usuario se actualice correctamente")
	void tesUpdateUserSuccess() {

		var randomEmail = System.currentTimeMillis() + "@email.com";

		var user = UserDTO.builder().email(randomEmail).name("name").lastName("lastname").mobileNumber("3000000000")
				.password("123456").build();

		var newUser = controller.insert(user);

		assertNotNull(newUser);
		assertNotNull(newUser.getId());
		assertEquals(randomEmail, user.getEmail());

		var newname = "newname";
		newUser.setName(newname);

		var updateUser = controller.update(newUser);

		assertNotNull(updateUser);
		assertNotNull(updateUser.getId());
		assertEquals(newname, updateUser.getName());

	}

}
