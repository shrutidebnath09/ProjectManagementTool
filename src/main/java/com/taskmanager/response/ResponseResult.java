package com.taskmanager.response;

import java.util.HashMap;
import java.util.Map;

/**
 * The class defines the general response format
 * 
 */
public class ResponseResult {

	private ResponseStatus status;
	private Map<String, String> messages;
	private boolean errorFlag;
	private Map<String, String> errorMessages;

	public enum ResponseStatus {
		NOT_SUCCESSFUL, SUCCESSFUL;
	};

	public ResponseResult() {
		this.messages = new HashMap<String, String>();
		this.errorMessages = new HashMap<String, String>();
		setStatus(ResponseStatus.SUCCESSFUL);
	}

	public String getStatus() {
		return status.name();
	}

	public void setStatus(final ResponseStatus status) {
		this.status = status;
	}

	public void addMessage(final String key, final String message) {
		if (key == null || message == null) {
			return;
		}
		this.messages.put(key, message);
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessage(final Map<String, String> messages) {
		this.messages = messages;
	}

	public boolean hasErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean hasErrors) {
		this.errorFlag = hasErrors;
		if (this.errorFlag == true) {
			setStatus(ResponseStatus.NOT_SUCCESSFUL);
		} else {
			setStatus(ResponseStatus.SUCCESSFUL);
		}
	}

	public void addErrorMessage(final String key, final String message) {
		if (key == null || message == null) {
			return;
		}
		this.errorMessages.put(key, message);
		setErrorFlag(true);
	}

	public Map<String, String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(final Map<String, String> messages) {
		this.errorMessages = messages;
	}
}
