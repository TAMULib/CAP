package edu.tamu.cap.model.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IRContext implements Serializable {

    private static final long serialVersionUID = -8489156248122941988L;

    private String name;

    private Triple triple;

    private boolean resource;

    private List<Triple> properties;

    private List<Triple> metadata;

    private List<IRContext> children;

    public IRContext() {
        super();
        properties = new ArrayList<Triple>();
        metadata = new ArrayList<Triple>();
        children = new ArrayList<IRContext>();
    }

    public IRContext(Triple triple) {
        this();
        this.triple = triple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Triple getTriple() {
        return triple;
    }

    public void setTriple(Triple triple) {
        this.triple = triple;
    }

    public boolean isResource() {
        return resource;
    }

    public void setResource(boolean resource) {
        this.resource = resource;
    }

    public List<Triple> getProperties() {
        return properties;
    }

    public void setProperties(List<Triple> properties) {
        this.properties = properties;
    }

    public void addProperty(Triple property) {
        properties.add(property);
    }

    public List<Triple> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Triple> metadata) {
        this.metadata = metadata;
    }

    public void addMetadum(Triple metadatum) {
        metadata.add(metadatum);
    }

    public List<IRContext> getChildren() {
        return children;
    }

    public void setChildren(List<IRContext> children) {
        this.children = children;
    }

    public void addChild(IRContext child) {
        children.add(child);
    }

}
