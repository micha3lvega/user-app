package co.com.csti.user.integration.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

	private String id;

	private String name;

	private String lastName;

	private String mobileNumber;

	private String email;

	private String password;

	private Date createDate;

	private Date lastUpdate;

	private Boolean active;

}
