package co.com.csti.user.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

	@Id
	private String id;

	private String name;

	private String lastName;

	private String mobileNumber;

	private String email;

	private String password;

	@CreatedDate
	private Date createDate;

	@LastModifiedDate
	private Date lastUpdate;

	@Builder.Default
	private boolean active = true;

}
