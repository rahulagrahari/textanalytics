package it.si3p.supwsd.modules.extraction.features;

import java.util.Objects;

/**
 * @author papandrea
 *
 */
public class POSTag extends Feature {

	private static final String DEFAULT = "!DEF!";
	private static final String KEY_PREFIX = "POS";
	private final int mI;

	public POSTag(int index, String name) {

		this(KEY_PREFIX + index, index, name);

	}

	POSTag(String key, int index, String name) {

		super(key, name);

		this.mI = index;
	}

	final int getI() {

		return this.mI;
	}

	@Override
	public String getDefaultValue() {

		return DEFAULT;
	}

	@Override
	public int hashCode() {

		return Objects.hash(getI());
	}

	@Override
	public boolean equals(Object arg) {

		if (arg instanceof POSTag) {

			POSTag feature = (POSTag) arg;

			return this.getI() == feature.getI();
		}

		return false;
	}
}
