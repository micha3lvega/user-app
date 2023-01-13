package co.com.csti.user.exception.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.com.csti.user.integration.dto.Response;
import co.com.csti.user.integration.exception.UserExistsException;
import co.com.csti.user.integration.exception.UserNotFoundException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler{

	@ExceptionHandler
	public ResponseEntity<Response> userNotFoundExceptionHandler(UserNotFoundException ex) {

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

	@ExceptionHandler(UserExistsException.class)
	public ResponseEntity<Response> userExistsExceptionHandler(UserExistsException ex) {

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

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) ->{

			String fieldName = ((FieldError) error).getField();
			var message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

}