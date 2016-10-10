package it.uniroma1.lcl.supWSD.modules.extraction.features;

import java.util.Objects;

/**
 * @author Simone Papandrea
 *
 */
public class SyntacticRelation extends Feature {

	private static final String DEFAULT = null;
	private static final String KEY_PREFIX = "SR_";

	public enum DependecyType {

		HEAD, POS, VOICE, POSITION, LEFT, RIGHT, LEFT_POS, RIGHT_POS;

	}

	public SyntacticRelation(DependecyType type, String value) {

		super(KEY_PREFIX + type, value);

	}

	@Override
	public String getDefaultValue() {

		return DEFAULT;
	}

	@Override
	public int hashCode() {

		return Objects.hash(getKey());
	}

	@Override
	public boolean equals(Object arg) {

		if (arg instanceof SyntacticRelation) {

			SyntacticRelation feature = (SyntacticRelation) arg;

			return this.getKey().equals(feature.getKey());
		}

		return false;
	}

}
