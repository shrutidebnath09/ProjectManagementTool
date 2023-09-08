package com.taskmanager.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@FormParam("name")
	private String name;

	@FormParam("description")
	private String description;

	@FormParam("content")
	private String content;

	@DefaultValue("0")
	@FormParam("owner")
	private long owner;

	public Task() {}

	public long getId() {
		return this.id;
	}

	public void setId(long value) {
		this.id = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(final String value) {
		this.content = value;
	}

	public long getOwner() {
		return this.owner;
	}

	public void setOwner(long value) {
		this.owner = value;
	}
}
