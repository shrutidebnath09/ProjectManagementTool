package com.taskmanager.filter;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;

import com.taskmanager.authentification.AuthUtil;
import com.taskmanager.controller.AuthenticationController;

/**
 * Filter to activate the authentication for every request
 * 
 */
public class BasicAuthFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {

		final String headerAuth = requestContext
				.getHeaderString(FilterConstants.AUTH_HEADER);

		final String[] splittedAuth = AuthUtil
				.parseClientAuthHeader(headerAuth);

		if (splittedAuth == null || splittedAuth.length != 2) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		boolean isAuthenticated = AuthenticationController.getAuthenticationController()
				.compareClientAndServerAuthHash(splittedAuth[0],
						splittedAuth[1]);

		if (!isAuthenticated) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		requestContext.setProperty(FilterConstants.USER_ID_CONTEXT,
				splittedAuth[0]);
	}
}
