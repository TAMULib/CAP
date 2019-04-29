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

    @ElementCollection
    private List<Property> properties;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> namespaces;

    public Schema() {
        setModelValidator(new SchemaValidator());
        setProperties(new ArrayList<Property>());
        this.namespaces = new HashSet<String>();
    }

    public Schema(String name, String namespace, String abbreviation, List<Property> properties) {
        this();
        setName(name);
        setNamespace(namespace);
        setAbbreviation(abbreviation);
        setProperties(properties);
    }

    public Schema(String name, String namespace, String abbreviation) {
        this();
        setName(name);
        setNamespace(namespace);
        setAbbreviation(abbreviation);
        setProperties(new ArrayList<Property>());
    }

    private void addNamespace(String namespace) {
        namespaces.add(namespace);
    }

    private String getNamespaceFromProperty(Property property) {
        String uri = property.getUri();
        return uri.split("#")[0];
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

    public void setNamespace(String prefix) {
        this.namespace = prefix;
    }
    
    public Set<String> getNamespaces() {
        return namespaces;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        properties.stream().forEach(property -> {
            addNamespace(getNamespaceFromProperty(property));
        });
        this.properties = properties;
    }

    public void addProperty(Property property) {
        addNamespace(getNamespaceFromProperty(property));
        this.properties.add(property);
    }

    public void removeProperty(Property property) {
        this.properties.remove(property);
    }

}
