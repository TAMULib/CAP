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
    private String uri;
    
    @Column
    private String username;
    
    @Column
    private String password;

    public IR() {
    	setModelValidator(new IRValidator());
    }
    
    public IR(IRType type, String name, String uri) {
    	this();
    	setType(type);
    	setName(name);
    	setUri(uri);
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
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
