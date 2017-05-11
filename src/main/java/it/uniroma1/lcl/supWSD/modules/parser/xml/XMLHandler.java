package it.uniroma1.lcl.supWSD.modules.parser.xml;

import java.util.LinkedList;
import org.xml.sax.helpers.DefaultHandler;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.modules.parser.AnnotationListener;

/**
 * @author Simone Papandrea
 *
 */
public abstract class XMLHandler extends DefaultHandler {

	private final LinkedList<Enum<?>> mTAGs;
	protected AnnotationListener mAnnotationListener;

        
	public XMLHandler() {

		this.mTAGs = new LinkedList<Enum<?>>();
	}


	protected  final void push(Enum<?> tag) {

		this.mTAGs.push(tag);
	}

	protected  final Enum<?> pop() {

		return mTAGs.pop();
	}

	protected final Enum<?> get() {

		return mTAGs.getFirst();
	}
	
	
	public final void setHandlerListener(AnnotationListener annotationListener) {
		
		this.mAnnotationListener=annotationListener;
	
	}
	
	protected String formatAnnotation(String text){
		
		String ann=text.trim().replaceAll("[^A-Za-z0-9]+", "_");
		
		ann=ann.replaceAll("_$", "");	
	
		return Annotation.ANNOTATION_TAG+ann+Annotation.ANNOTATION_TAG;
	}
	
}
