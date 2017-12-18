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

import edu.tamu.cap.model.validation.IRValidator;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

/**
 * 
 * 
 * @author
 *
 */
@Entity
public class IR extends ValidatingBaseEntity {

    @Column(unique = true)
    private String name;
    
    @Column
    private String uri;

    public IR() {
    	setModelValidator(new IRValidator());
    }
    
    public IR(String name, String uri) {
    	this();
    	setName(name);
    	setUri(uri);
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

  

}
