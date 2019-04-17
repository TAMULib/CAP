package edu.tamu.cap.model.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import edu.tamu.cap.model.repositoryviewcontext.TransactionDetails;
import edu.tamu.cap.service.FedoraService;

public class RepositoryViewContext implements Serializable {

    private static final long serialVersionUID = -8489156248122941988L;

    private String name;

    private Triple triple;

    private Triple parent;

    private Map<String, List<Triple>> properties;

    private Map<String, List<Triple>> metadata;

    private List<RepositoryViewContext> children;

    private List<Version> versions;

    private String version;

    private TransactionDetails transactionDetails;

    private Map<String, Boolean> features;

	public RepositoryViewContext() {
        super();
        properties = new HashMap<String, List<Triple>>();
        metadata = new HashMap<String, List<Triple>>();
        versions = new ArrayList<Version>();
        children = new ArrayList<RepositoryViewContext>();
        features = new HashMap<String, Boolean>();
    }

    public RepositoryViewContext(Triple triple) {
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

    public Triple getParent() {
        return parent;
    }

    public void setParent(Triple parent) {
        this.parent = parent;
    }

    public boolean getHasParent() {
        return Optional.ofNullable(parent).isPresent();
    }

    public boolean isResource() {
        return triple != null ? triple.getObject().equals(FedoraService.FEDORA_BINRAY_PREDICATE) : false;
    }

    public Map<String, List<Triple>> getProperties() {
        return properties;
    }

    public List<Triple> getPropertiesAll() {
        List<Triple> triples = new ArrayList<Triple>();

        for(Entry<String, List<Triple>> set: properties.entrySet()){
            List<Triple> values = set.getValue();
            for (int i = 0; i < values.size(); i++) {
                triples.add(values.get(i));
            }
        }

        return triples;
    }

    public void setProperties(Map<String, List<Triple>> properties) {
        this.properties = properties;
    }

    public void addProperty(Triple property) {
        String key = property.getPredicate();
        if (properties.containsKey(key)) {
            properties.get(key).add(property);
        } else {
            List<Triple> list = new ArrayList<Triple>();
            list.add(property);
            properties.put(key, list);
        }
    }

    public Map<String, List<Triple>> getMetadata() {
        return metadata;
    }

    public List<Triple> getMetadataAll() {
        List<Triple> triples = new ArrayList<Triple>();

        for(Entry<String, List<Triple>> set: metadata.entrySet()){
            List<Triple> values = set.getValue();
            for (int i = 0; i < values.size(); i++) {
                triples.add(values.get(i));
            }
        }

        return triples;
    }

    public void setMetadata(Map<String, List<Triple>> metadata) {
        this.metadata = metadata;
    }

    public void addMetadum(Triple metadatum) {
        String key = metadatum.getPredicate();
        if (metadata.containsKey(key)) {
            metadata.get(key).add(metadatum);
        } else {
            List<Triple> list = new ArrayList<Triple>();
            list.add(metadatum);
            metadata.put(key, list);
        }
    }

    public List<RepositoryViewContext> getChildren() {
        return children;
    }

    public void setChildren(List<RepositoryViewContext> children) {
        this.children = children;
    }

    public void addChild(RepositoryViewContext child) {
        children.add(child);
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public void addVersion(Version version) {
        this.versions.add(version);
    }

    public void removeVersion(Version version) {
        this.versions.remove(version);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public boolean getIsVersion() {
        return Optional.ofNullable(version).isPresent();
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Map<String, Boolean> addFeature(String featureName, boolean supported) {
        this.features.put(featureName, supported);
        return features;
    }

    public Map<String, Boolean> removeFeature(String featureName) {
        this.features.remove(featureName);
        return features;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }

}
