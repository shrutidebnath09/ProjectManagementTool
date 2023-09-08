package com.taskmanager.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.taskmanager.authentification.AuthUtil;
import com.taskmanager.controller.AuthenticationController;
import com.taskmanager.helper.RequestHelper;
import com.taskmanager.models.UserProfile;
import com.taskmanager.response.ResponseResult;

/**
 * API for Authentication
 * 
 */
@Path("/auth")
public class Authentification {

	private static final String USERNAME_ALREADY_REGISTERED_MESSAGE = "This username is is already registered.";
	private static final String EMAIL_ALREADY_REGISTERED_MESSAGE = "This email is is already registered.";
	private static final String PROFILE_COULD_NOT_BE_CREATED_MESSAGE = "The profile could not be saved.";
	private static final String FAILED_LOGIN_MESSAGE = "Username and password ist not valid.";
	private static final String LOGIN_NOT_POSSIBLE_MESSAGE = "Login not possible - please try later again.";

	/**
	 * Register an account
	 * 
	 * @param userProfile
	 * @return {@link ResponseResult} with validation messages
	 */
	@Path("signup")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String signUp(UserProfile userProfile) {

		final Gson gson = new Gson();
		final ResponseResult responseResult = new ResponseResult();
		final AuthenticationController authController = AuthenticationController.getAuthenticationController();

		if (authController.isUsernameRegistered(userProfile
				.getUsername())) {
			responseResult.addErrorMessage("username",
					USERNAME_ALREADY_REGISTERED_MESSAGE);
		}

		if (authController.isEmailRegistered(userProfile.getEmail())) {
			responseResult.addErrorMessage("email",
					EMAIL_ALREADY_REGISTERED_MESSAGE);
		}

		if (!responseResult.hasErrorFlag()) {

			authController.registerProfile(userProfile);

			final String clientAuthHash = AuthUtil
					.createClientAuthHash(userProfile);

			if (clientAuthHash == null) {
				responseResult.addErrorMessage("registration",
						PROFILE_COULD_NOT_BE_CREATED_MESSAGE);
			} else {
				responseResult.addMessage("authentification", clientAuthHash);
			}
		}

		return gson.toJson(responseResult);
	}

	@Path("signin")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String signIn(UserProfile userProfile) {
		final Gson gson = new Gson();
		final ResponseResult responseResult = new ResponseResult();

		final UserProfile profile = AuthenticationController.getAuthenticationController()
				.findProfileByUsernameAndPassword(userProfile);

		if (profile == null) {
			responseResult.addErrorMessage("login", FAILED_LOGIN_MESSAGE);
			return gson.toJson(responseResult);
		}

		final String clientAuthHash = AuthUtil.createClientAuthHash(profile);

		if (clientAuthHash == null) {
			responseResult.addErrorMessage("security",
					LOGIN_NOT_POSSIBLE_MESSAGE);
		} else {
			responseResult.addMessage("authentification", clientAuthHash);
		}

		return gson.toJson(responseResult);
	}

	@Path("signout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String signOut(@Context ContainerRequestContext requestContext) {
		final Gson gson = new Gson();
		final ResponseResult responseResult = new ResponseResult();

		final AuthenticationController authController = AuthenticationController.getAuthenticationController();

		long userId = RequestHelper.getUserIdFromContext(requestContext);
		authController.renewClientAuthHashSalt(userId);

		return gson.toJson(responseResult);
	}

}
