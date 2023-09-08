package com.taskmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

/**
 * A class for user profile entities
 * 
 */
@Entity
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@NotNull
	@FormParam("username")
	@Column(unique = true)
	private String username;

	@NotNull
	@FormParam("password")
	private String password;

	@NotNull
	@FormParam("email")
	@Column(unique = true)
	private String email;

	private String passwordSalt;
	
	private String authHashSalt;

	public UserProfile() {}

	/**
	 * 
	 * @return
	 */
	public long getId() {
		return this.userId;
	}

	/**
	 * 
	 * @param value
	 */
	public void setId(long value) {
		this.userId = value;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * 
	 * @param value
	 */
	public void setUsername(final String value) {
		this.username = value;
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param value
	 */
	public void setPassword(final String value) {
		this.password = value;
	}

	/**
	 * 
	 * @return
	 */
	public String getPasswordSalt() {
		return this.passwordSalt;
	}

	/**
	 * 
	 * @param value
	 */
	public void setPasswordSalt(final String value) {
		this.passwordSalt = value;
	}

	/**
	 * 
	 * @return
	 */
	public String getAuthHashSalt() {
		return this.authHashSalt;
	}

	/**
	 * 
	 * @param value
	 */
	public void setAuthHashSalt(final String value) {
		this.authHashSalt = value;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * 
	 * @param value
	 */
	public void setEmail(final String value) {
		this.email = value;
	}

}
