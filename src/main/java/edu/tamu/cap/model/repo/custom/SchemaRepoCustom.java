package edu.tamu.cap.model.repo.custom;

import java.util.List;

import org.apache.jena.atlas.web.HttpException;

import edu.tamu.cap.exceptions.OntModelReadException;
import edu.tamu.cap.model.Property;

public interface SchemaRepoCustom {
	
	public List<Property> findPropertiesByNamespace(String namespace) throws OntModelReadException;

}
