package swp_compiler_ss13.fuc.gui.text;

import java.awt.Color;

/**
 * @author "Eduard Wolf"
 *
 */
public class StringColourPair {

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
		BLACK(Color.BLACK), BLUE(Color.BLUE), CYAN(Color.CYAN), GRAY(Color.GRAY), GREEN(Color.GREEN), MAGENTA(
				Color.MAGENTA), ORANGE(Color.ORANGE), PINK(Color.PINK), RED(Color.RED), YELLOW(
				Color.YELLOW);

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
