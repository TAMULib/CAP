package edu.tamu.cap.model.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IRContext implements Serializable {

    private static final long serialVersionUID = -8489156248122941988L;

    private String name;

    private boolean resource;

    private List<Triple> properties;

    private List<Triple> metadata;

    private List<Triple> containers;

    private List<Triple> resources;

    public IRContext() {
        properties = new ArrayList<Triple>();
        metadata = new ArrayList<Triple>();
        containers = new ArrayList<Triple>();
        resources = new ArrayList<Triple>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Triple> getContainers() {
        return containers;
    }

    public void setContainers(List<Triple> containers) {
        this.containers = containers;
    }

    public void addContainer(Triple container) {
        containers.add(container);
    }

    public List<Triple> getResources() {
        return resources;
    }

    public void setResources(List<Triple> resources) {
        this.resources = resources;
    }

    public void addResource(Triple resource) {
        resources.add(resource);
    }

}
