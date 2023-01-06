package co.com.csti.user.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
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

	private Boolean active;

}
