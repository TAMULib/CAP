package edu.tamu.cap.model.response;

import java.io.Serializable;
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

	public List<Triple> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<Triple> metadata) {
		this.metadata = metadata;
	}

	public List<Triple> getContainers() {
		return containers;
	}

	public void setContainers(List<Triple> containers) {
		this.containers = containers;
	}

	public List<Triple> getResources() {
		return resources;
	}

	public void setResources(List<Triple> resources) {
		this.resources = resources;
	};

}
