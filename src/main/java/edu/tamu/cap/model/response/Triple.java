package edu.tamu.cap.model.response;

import java.io.Serializable;

public class Triple implements Serializable {

	private static final long serialVersionUID = -8857131728443388752L;

	public String subject;

	public String predicate;

	public String object;

	public Triple() {

	};

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

}
