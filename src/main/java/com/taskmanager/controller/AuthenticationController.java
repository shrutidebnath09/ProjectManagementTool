package com.taskmanager.controller;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.taskmanager.authentification.AuthUtil;
import com.taskmanager.authentification.CodecUtil;
import com.taskmanager.dal.EntityManagerBuilder;
import com.taskmanager.dal.UserProfileEntityManager;
import com.taskmanager.models.UserProfile;

/**
 * Controller for authentication
 * 
 */
public class AuthenticationController {

	private static AuthenticationController authController = new AuthenticationController();
	private final UserProfileEntityManager userEntityManager;

	private AuthenticationController() {
		this.userEntityManager = EntityManagerBuilder.getEntityManager()
				.createEntityManager(UserProfileEntityManager.class);
	}

	/**
	 * @return a singleton instance
	 */
	public static AuthenticationController getAuthenticationController() {
		return authController;
	}

	/**
	 * Checks if the user name is already registered
	 * 
	 * @param username
	 * @return
	 */
	public boolean isUsernameRegistered(final String username) {
		return this.userEntityManager.findByUsername(username) != null;
	}

	/**
	 * Checks if the email is already registered
	 * 
	 * @param email
	 * @return
	 */
	public boolean isEmailRegistered(final String email) {
		return this.userEntityManager.findByEmail(email) != null;
	}

	/**
	 * Searches the profile in the db
	 * 
	 * @param {@link UserProfile} userProfile
	 * @return the profile found.
	 */
	public UserProfile findProfileByUsernameAndPassword(
			final UserProfile userProfile) {

		final UserProfile profile = this.userEntityManager
				.findByUsername(userProfile.getUsername());

		if (profile == null) {
			return null;
		}

		final byte[] salt = CodecUtil.hexToByte(profile.getPasswordSalt());
		final byte[] encryptedPassword = AuthUtil.encryptPasswordWithSalt(
				userProfile.getPassword(), salt);

		if (!profile.getPassword().equals(
				CodecUtil.byteToHex(encryptedPassword))) {
			return null;
		}

		final String newKey = CodecUtil.byteToHex(AuthUtil.generateSalt());
		profile.setAuthHashSalt(newKey);

		this.userEntityManager.edit(profile);

		return profile;
	}

	/**
	 * Registers a profile
	 * 
	 * @param {@link UserProfile} userProfile
	 */
	public void registerProfile(final UserProfile userProfile) {

		byte[] salt = AuthUtil.generateSalt();
		byte[] encryptedPassword = AuthUtil.encryptPasswordWithSalt(
				userProfile.getPassword(), salt);

		userProfile.setPasswordSalt(CodecUtil.byteToHex(salt));
		userProfile.setPassword(CodecUtil.byteToHex(encryptedPassword));
		userProfile.setAuthHashSalt(AuthUtil.generateNewClientAuthHashSalt());

		this.userEntityManager.create(userProfile);
	}

	/**
	 * Compares the authentification hash sent by the client with the profile in
	 * the db
	 * 
	 * @param userId
	 * @param clientHash
	 * @return
	 */
	public boolean compareClientAndServerAuthHash(final String userId,
			final String clientHash) {

		if (clientHash == null) {
			return false;
		}

		final UserProfile userProfile = this.userEntityManager.findById(Long
				.valueOf(userId));

		if (userProfile == null) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		final String serverHash = AuthUtil.createAuthHash(userProfile);

		return clientHash.equals(serverHash);
	}

	public void renewClientAuthHashSalt(long userId) {
		if (userId == 0) {
			return;
		}

		final UserProfile profile = this.userEntityManager.findById(userId);
		if (profile == null) {
			return;
		}

		profile.setAuthHashSalt(AuthUtil.generateNewClientAuthHashSalt());
		this.userEntityManager.edit(profile);
	}

}
