package edu.tamu.cap.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.tamu.cap.model.validation.SchemaValidator;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
//@JsonIgnoreProperties(value={ "namespaces" }, allowGetters=true)
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

    public Schema(String name, String namespace, String abbreviation, List<Property> properties) {
        this();
        setName(name);
        setAbbreviation(abbreviation);
        setProperties(properties);
    }

    public Schema(String name, String namespace, String abbreviation) {
        this();
        setName(name);
        setAbbreviation(abbreviation);
        setProperties(new ArrayList<Property>());
    }

    private String getNamespaceFromProperty(Property property) {
        String uri = property.getUri();
        String namespace = null;
        if (uri != null) {
            // matches # if there, otherwise matches last /
            namespace = uri.split("#|(/)(?:[^/#]+)$")[0];
        }
        return namespace;
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

    public Set<String> getNamespaces() {
        Set<String> namespaces = new HashSet<String>();
        properties.stream().forEach(property -> {
            namespaces.add(getNamespaceFromProperty(property));
        });
        return namespaces;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public void removeProperty(Property property) {
        this.properties.remove(property);
    }

}
