package edu.tamu.cap.model.response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;

import edu.tamu.cap.exceptions.InvalidTripleException;

public class Triple implements Serializable {

    private final static long serialVersionUID = -8857131728443388752L;

    private String subject;

    private String predicate;

    private String object;

    public Triple() {
        super();
    };

    public Triple(String subject, String predicate, String object) {
        this();
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    };

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public static Triple of(String subject, String predicate, String object) {
        return new Triple(subject, predicate, object);
    }

    public static Triple of(Map<String, String> tripleMap) {
        return new Triple(tripleMap.get("subject"), tripleMap.get("predicate"), tripleMap.get("object"));
    }

    public static Triple of(org.apache.jena.graph.Triple asTriple) {
        return Triple.of(asTriple.getSubject().toString(true), asTriple.getPredicate().toString(true), asTriple.getObject().toString(true));
    }

    /**
     * Convert an Apache Jena Triple into a Cap Triple.
     *
     * @param jenaTriple
     *   The Apache Jena Triple to convert from.
     *
     * @return
     *   The generated Cap Triple.
     */
    public static Triple fromJenaTriple(org.apache.jena.graph.Triple jenaTriple) {
        String subject = jenaTriple.getSubject().toString(true);
        String predicate = jenaTriple.getPredicate().toString(true);
        String object = jenaTriple.getObject().toString(true);
        return Triple.of(subject, predicate, object);
    }

    /**
     * Convert and validate a Cap Triple to an Apache Jena Triple.
     *
     * This uses the triple language N3 because it fully complies with the RDF specification in regards to a Triple and UTF-8.
     * When validation fails, InvalidTripleException is thrown.
     *
     * @param triple
     *   The triple to validate that it has the proper format and is properly escaped.
     *
     * @return
     *   The generated triple.
     *
     * @throws InvalidTripleException
     */
    public static org.apache.jena.graph.Triple toJenaTriple(Triple triple) throws InvalidTripleException {
        Model model =  ModelFactory.createDefaultModel();
        InputStream stream = new ByteArrayInputStream(triple.toString().getBytes(StandardCharsets.UTF_8));
        RDFDataMgr.read(model, stream, RDFLanguages.N3);
        org.apache.jena.graph.Triple jenaTriple = model.getGraph().find().next();

        if (!jenaTriple.getSubject().toString(true).equals(triple.getSubject())) {
            throw new InvalidTripleException("Triple Subject is invalid.");
        }

        if (!jenaTriple.getPredicate().toString(true).equals(triple.getPredicate())) {
            throw new InvalidTripleException("Triple Predicate is invalid.");
        }

        if (!jenaTriple.getObject().toString(true).equals(triple.getObject())) {
            throw new InvalidTripleException("Triple Object is invalid.");
        }

        return jenaTriple;
    }

    /**
     * Validate a Cap Triple.
     *
     * @param triple
     *   The triple to validate that it has the proper format and is properly escaped.
     *
     * @throws InvalidTripleException
     */
    public static void validateTriple(Triple triple) throws InvalidTripleException {
        Triple.toJenaTriple(triple);
    }

    @Override
    public String toString() {
        return String.format("<%s> <%s> %s", subject, predicate, object);
    }

}
