package com.taskmanager.helper;

import javax.ws.rs.container.ContainerRequestContext;

import com.taskmanager.filter.FilterConstants;

/**
 * Helper class for Requests
 * 
 */
public class RequestHelper {

	public static long getUserIdFromContext(
			final ContainerRequestContext requestContext) {
		final Object ownerId = requestContext
				.getProperty(FilterConstants.USER_ID_CONTEXT);
		if (ownerId == null) {
			return 0;
		}
		return Long.valueOf(ownerId.toString());
	}

}
