package edu.tamu.cap.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.tamu.cap.model.validation.RepositoryViewValidator;
import edu.tamu.cap.service.RepositoryViewType;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
public class RepositoryView extends ValidatingBaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private RepositoryViewType type;

    @Column(unique = true)
    private String name;

    @Column
    private String rootUri;

    @Column
    private String username;

    @Column
    private String password;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Schema> schemas;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> curators;

    public RepositoryView() {
        setModelValidator(new RepositoryViewValidator());
        setSchemas(new ArrayList<Schema>());
        setCurators(new ArrayList<User>());
    }

    public RepositoryView(RepositoryViewType type, String name, String rootUri) {
        this();
        setType(type);
        setName(name);
        setRootUri(rootUri);
    }

    public RepositoryView(RepositoryViewType type, String name, String rootUri, List<Schema> schemas) {
        this(type, name, rootUri);
        setSchemas(schemas);
    }

    public RepositoryViewType getType() {
        return type;
    }

    public void setType(RepositoryViewType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootUri() {
        return rootUri;
    }

    public void setRootUri(String rootUri) {
        this.rootUri = rootUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<Schema> schema) {
        this.schemas = schema;
    }

    public List<User> getCurators() {
        return curators;
    }

    public void setCurators(List<User> curators) {
        this.curators = curators;
    }

    public List<String> getMetadataPrefixes() {
        List<String> metadataPrefixes = new ArrayList<String>();
        schemas.forEach(schema -> {
            metadataPrefixes.addAll(schema.getNamespaces());
        });
        return metadataPrefixes;
    }

}
