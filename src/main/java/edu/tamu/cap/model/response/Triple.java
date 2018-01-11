package edu.tamu.cap.model.response;

import java.io.Serializable;

public class Triple implements Serializable {

    private static final long serialVersionUID = -8857131728443388752L;

    private String subject;

    private String predicate;

    private String object;

    public Triple() {
        super();
    };

    public Triple(String subject, String predicate, String object) {
        this();
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
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

    public static Triple of(String subject, String predicate, String object) {
        return new Triple(subject, predicate, object);
    }

}
