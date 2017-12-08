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

import edu.tamu.weaver.data.model.BaseEntity;

/**
 * 
 * 
 * @author
 *
 */
@Entity
public class IR extends BaseEntity {

    @Column(unique = true)
    private String name;
    
    @Column(unique = true)
    private String URI;

    public IR() {}
    
    public IR(String name, String uri) {
    	setName(name);
    	setURI(uri);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

  

}
