package co.com.csti.user.integration.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Response implements Serializable {

	private Date date;
	private String message;

}
