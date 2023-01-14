package co.com.csti.user.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import co.com.csti.user.integration.dto.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements Serializable {

	@Id
	private String id;

	private String name;

	private String lastName;

	private String mobileNumber;

	private String email;

	private String password;

	@Builder.Default
	private Set<ERole> roles = new HashSet<>();

	@CreatedDate
	private Date createDate;

	@LastModifiedDate
	private Date lastUpdate;

	@Builder.Default
	private boolean active = true;

}
