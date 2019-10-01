package edu.tamu.cap.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import edu.tamu.cap.model.validation.SchemaValidator;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
public class Schema extends ValidatingBaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String abbreviation;

    @Column(nullable = false)
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
        return properties.stream().map(this::getNamespaceFromProperty).collect(Collectors.toSet());
    }

    private String getNamespaceFromProperty(Property property) {
        String uri = property.getUri();
        // matches # if there, otherwise matches last /
        return uri.split("#|(/)(?:[^/#]+)$")[0];
    }

}
