package swp_compiler_ss13.fuc.gui.text;

import java.awt.Color;

/**
 * @author "Eduard Wolf"
 * 
 */
public class StringColourPair {

	private static final int MAX_VALUE = 170;

	private String text;
	private ColorWrapper color;

	/**
	 * text is an empty string<br/>
	 * color is black
	 */
	public StringColourPair() {
		text = "";
		color = DefaultColorWrapper.BLACK;
	}

	public StringColourPair setText(String text) {
		if (text == null) {
			text = "";
		}
		this.text = text;
		return this;
	}

	public StringColourPair setColor(ColorWrapper color) {
		if (color == null) {
			color = DefaultColorWrapper.BLACK;
		}
		this.color = color;
		return this;
	}

	public String getText() {
		return text;
	}

	public ColorWrapper getColor() {
		return color;
	}

	/**
	 * default wrapper for {@link java.awt.Color}
	 */
	public enum DefaultColorWrapper implements ColorWrapper {
		BLACK(Color.BLACK), BLUE(Color.BLUE), CYAN(new Color(0, MAX_VALUE, MAX_VALUE)), GRAY(
				Color.GRAY), GREEN(new Color(0, MAX_VALUE, 0)), MAGENTA(Color.MAGENTA), ORANGE(
				new Color(MAX_VALUE, MAX_VALUE * 3 / 4, 0)), PINK(new Color(MAX_VALUE,
				MAX_VALUE * 2 / 3, MAX_VALUE * 2 / 3)), RED(Color.RED), YELLOW(new Color(MAX_VALUE,
				MAX_VALUE, 0));

		private Color color;

		private DefaultColorWrapper(Color color) {
			this.color = color;
		}

		/**
		 * {@inheritDoc}
		 */
		public Color getColor() {
			return color;
		}
	}
}
