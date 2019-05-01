package edu.tamu.cap.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import edu.tamu.cap.model.validation.SchemaValidator;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
public class Schema extends ValidatingBaseEntity {

    @Column
    private String name;

    @Column
    private String abbreviation;

    @Column
    private String namespace;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Property> properties;

    public Schema() {
        setModelValidator(new SchemaValidator());
        setProperties(new ArrayList<Property>());
    }

    public Schema(String name, String namespace, String abbreviation) {
        this();
        setName(name);
        setNamespace(namespace);
        setAbbreviation(abbreviation);
    }

    public Schema(String name, String namespace, String abbreviation, List<Property> properties) {
        this(name, namespace, abbreviation);
        setProperties(properties);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public Set<String> getNamespaces() {
        Set<String> namespaces = new HashSet<String>();
        properties.stream().forEach(property -> {
            namespaces.add(getNamespaceFromProperty(property));
        });
        return namespaces;
    }

    private String getNamespaceFromProperty(Property property) {
        String uri = property.getUri();
        String namespace = null;
        System.out.println("\n\n\nuri: " + uri + "\n\n\n");
//        if (uri != null) {
            // matches # if there, otherwise matches last /
            namespace = uri.split("#|(/)(?:[^/#]+)$")[0];
//        }
        return namespace;
    }

}
