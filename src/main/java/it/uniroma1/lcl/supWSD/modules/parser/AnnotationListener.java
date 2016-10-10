package it.uniroma1.lcl.supWSD.modules.parser;

import java.util.List;
import org.xml.sax.SAXException;
import it.uniroma1.lcl.supWSD.data.Annotation;

/**
 * @author Simone Papandrea
 *
 */
public interface AnnotationListener {

	void notifyAnnotations(List<Annotation> annotations) throws SAXException ;
}
