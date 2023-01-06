package co.com.csti.user.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.com.csti.user.integration.dto.Response;
import co.com.csti.user.integration.exception.UserNotFoundException;

@ControllerAdvice
public class UserExistsExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Response> exceptionHandler(UserNotFoundException ex) {

		var response = new Response();

		response.setDate(new Date());

		// Agregar error
		if ((ex.getCause() != null) && (ex.getCause().getLocalizedMessage() != null)) {
			response.setMessage(ex.getCause().getLocalizedMessage());
		} else if ((ex.getMessage() != null) && !ex.getMessage().isBlank()) {
			response.setMessage(ex.getMessage());
		} else {
			response.setMessage("Error en el request");
		}

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

	}

}
