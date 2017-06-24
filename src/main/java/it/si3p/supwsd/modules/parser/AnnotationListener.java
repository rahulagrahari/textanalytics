package it.si3p.supwsd.modules.parser;

import java.util.List;
import org.xml.sax.SAXException;
import it.si3p.supwsd.data.Annotation;

/**
 * @author papandrea
 *
 */
public interface AnnotationListener {

	void notifyAnnotations(List<Annotation> annotations) throws SAXException ;
}
