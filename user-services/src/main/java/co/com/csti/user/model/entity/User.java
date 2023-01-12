package co.com.csti.user.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements Serializable {

	@Id
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

	@CreatedDate
	private Date createDate;

	@LastModifiedDate
	private Date lastUpdate;

	private Boolean active;

}
