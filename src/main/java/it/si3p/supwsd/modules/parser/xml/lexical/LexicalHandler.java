package it.si3p.supwsd.modules.parser.xml.lexical;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.parser.xml.XMLHandler;

/**
 * @author papandrea
 *
 */
public class LexicalHandler extends XMLHandler {
	
	private String mLexelID,mSentence, mInstanceID,mName;
	private List<Annotation>mAnnotations;
	private String mHead="";
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {
		
		LexicalTag tag=LexicalTag.valueOf(name.toUpperCase());
			
		switch (tag) {

		case LEXELT:
			
			mAnnotations=new  ArrayList<Annotation>();
			mName=attributes.getValue(LexicalAttribute.ITEM.name().toLowerCase());
			mLexelID=attributes.getValue(LexicalAttribute.ID.name().toLowerCase());
			break;
			
		case INSTANCE:

			mSentence="";
			mInstanceID=attributes.getValue(LexicalAttribute.ID.name().toLowerCase());			
			break;

		case HEAD:
			mHead="";
			break;
		
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		LexicalTag tag=LexicalTag.valueOf(name.toUpperCase());
		
		switch (tag) {

		case LEXELT:
			
			this.mAnnotationListener.notifyAnnotations(mAnnotations);
			this.mAnnotations=null;
			break;
			
		case INSTANCE:

			Annotation annotation;
			
			if(mHead.isEmpty())
				throw new  SAXException("Missed tag <HEAD> in sentence "+mInstanceID);
			
			annotation=new Annotation(mInstanceID,mSentence.trim());
			annotation.addLexel(new Lexel(mLexelID,mName));
			mAnnotations.add(annotation);
			break;
			
		case HEAD:

			mSentence +=formatAnnotation(mHead);
			break;
			
		default:
			break;
		}
		
		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) {

		switch ((LexicalTag)this.get()) {

		case CONTEXT:

			mSentence += new String(ch, start, length).replaceAll("[\r\n]", " ");
			break;
			
		case HEAD:

			mHead+= new String(ch, start, length).replaceAll("[\r\n]", " ");
			break;
		
		default:
			break;
		}
	}
	
}
