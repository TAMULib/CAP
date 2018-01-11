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

import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

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

	@Column
	private String username;

	@Column
	private String password;
	
	@OneToMany(fetch = EAGER)
    private List<Schema> schemas;

	public IR() {
		setModelValidator(new IRValidator());
	}

	public IR(IRType type, String name, String rootUri, List<Schema> schemas) {
		this();
		setType(type);
		setName(name);
		setRootUri(rootUri);
		setSchemas(schemas!=null?schemas:new ArrayList<Schema>());
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

    public List<Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<Schema> schema) {
        this.schemas = schema;
    }

}
