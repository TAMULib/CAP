package edu.tamu.cap.utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;

import edu.tamu.cap.exceptions.InvalidTripleException;
import edu.tamu.cap.model.response.Triple;

public class TripleUtil {

    /**
     * Convert and validate an Cap Triple to a Apache Jena Triple.
     *
     * This uses the triple language N3 because it fully complies with the RDF specification in regards to a triple and UTF-8.
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

}
