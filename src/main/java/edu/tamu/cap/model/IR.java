/* 
 * Project.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.cap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import edu.tamu.cap.model.validation.IRValidator;
import edu.tamu.cap.service.ir.IRType;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

/**
 * 
 * 
 * @author
 *
 */
@Entity
public class IR extends ValidatingBaseEntity {

	@Column
	@Enumerated(EnumType.STRING)
	private IRType type;

	@Column(unique = true)
	private String name;

	@Column
	private String rootUri;
	
	@Transient
	private String contextUri;

	@Column
	private String username;

	@Column
	private String password;

	public IR() {
		setModelValidator(new IRValidator());
	}

	public IR(IRType type, String name, String rootUri) {
		this();
		setType(type);
		setName(name);
		setRootUri(rootUri);
		setContextUri(rootUri);
	}

	public IRType getType() {
		return type;
	}

	public void setType(IRType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRootUri() {
		return rootUri;
	}

	public void setRootUri(String rootUri) {
		this.rootUri = rootUri;
	}

	public String getContextUri() {
		// This is root uri intentionally.
		return rootUri;
	}

	public void setContextUri(String contextUri) {
		this.contextUri = contextUri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
