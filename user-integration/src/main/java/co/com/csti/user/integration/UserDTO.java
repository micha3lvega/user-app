package co.com.csti.user.integration;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO implements Serializable {

	private String id;

	@NotBlank(message = "Name is mandatory")
	@Size(min = 2, max = 60, message = "The size of the names must be greater than 2 and less than 60")
	private String name;

	@NotBlank(message = "last names is mandatory")
	@Size(min = 2, max = 60, message = "The size of the lastnames must be greater than 2 and less than 60")
	private String lastName;

	@Size(min = 10, max = 100, message = "the mobile number must be greater than 10 digits")
	@NotBlank(message = "Mobile number is mandatory")
	private String mobileNumber;

	@Size(min = 5, max = 160, message = "The size of the email must be greater than 5")
	@Email(message = "Not a valid email")
	@NotBlank(message = "Email is mandatory")
	private String email;

	@Size(min = 6, max = 124, message = "The size of the password must be greater than 6")
	@NotBlank(message = "Password is mandatory")
	private String password;

	private Date createDate;

	private Date lastUpdate;

	private Boolean active;

}
