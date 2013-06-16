package swp_compiler_ss13.fuc.gui.sourcecode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.Segment;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * {@link StyledEditorKit} with function for wavy line ({@link #WAVY_LINE}) and
 * underline color ({@link #UNDERLINE_COLOR})
 * 
 * @author "Eduard Wolf"
 * 
 */
public class UnderlineStyledEditorKit extends StyledEditorKit {

	private static final long serialVersionUID = 1L;

	/**
	 * Key for {@link AttributeSet} to set a {@link Color} for the
	 * {@link StyleConstants#Underline}
	 */
	public static final Object UNDERLINE_COLOR = "underline-color";
	/**
	 * Key for {@link AttributeSet} to draw a wavy line with the
	 * {@link #UNDERLINE_COLOR} or if not specified with the default text color
	 * under the Element
	 */
	public static final Object WAVY_LINE = "wavy line";

	private final ViewFactory defaultFactory;

	public UnderlineStyledEditorKit() {
		super();
		defaultFactory = new UnderlineViewFactory(super.getViewFactory());
	}

	@Override
	public ViewFactory getViewFactory() {
		return defaultFactory;
	}

	static class UnderlineViewFactory implements ViewFactory {

		private final ViewFactory defaultFactury;

		public UnderlineViewFactory(ViewFactory defaultFactury) {
			if (defaultFactury == null) {
				throw new NullPointerException("defaultFactory cannot be null");
			}
			this.defaultFactury = defaultFactury;
		}

		@Override
		public View create(Element elem) {
			if (AbstractDocument.ContentElementName.equals(elem.getName())) {
				return new UnderlineColorLabelView(elem);
			}
			return defaultFactury.create(elem);
		}

	}

	static class UnderlineColorLabelView extends LabelView {

		public UnderlineColorLabelView(Element elem) {
			super(elem);
		}

		@Override
		public void paint(Graphics g, Shape a) {
			super.paint(g, a);
			paintUnderline(g, a);
		}

		/**
		 * copy from {@link GlyphView#paintTextUsingColor}
		 */
		protected void paintUnderline(Graphics g, Shape a) {
			AttributeSet attributes = getElement().getAttributes();
			boolean underline = StyleConstants.isUnderline(attributes);
			Boolean wavy = (Boolean) attributes.getAttribute(WAVY_LINE);
			boolean wavyLine = wavy == null ? false : wavy.booleanValue();
			Color newColor = (Color) attributes.getAttribute(UNDERLINE_COLOR);
			if (wavyLine || underline && newColor != null) {
				if (newColor == null) {
					newColor = StyleConstants.getForeground(attributes);
				}
				int p0 = getStartOffset();
				int p1 = getEndOffset();
				// calculate x coordinates
				Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
				View parent = getParent();
				if ((parent != null) && (parent.getEndOffset() == p1)) {
					// strip whitespace on end
					Segment s = getText(p0, p1);
					while (Character.isWhitespace(s.last())) {
						p1 -= 1;
						s.count -= 1;
					}
				}
				int x0 = alloc.x;
				int p = getStartOffset();
				if (p != p0) {
					x0 += (int) getGlyphPainter().getSpan(this, p, p0, getTabExpander(), x0);
				}
				int x1 = x0 + (int) getGlyphPainter().getSpan(this, p0, p1, getTabExpander(), x0);

				// calculate y coordinate
				int y = alloc.y + alloc.height - (int) getGlyphPainter().getDescent(this) + 1;

				Color oldColor = g.getColor();
				g.setColor(newColor);
				if (underline) {
					g.drawLine(x0, y, x1, y);
				}
				if (wavyLine) {
					int y1 = 0;
					int yChange = 1;
					for (int i = x0; i <= x1; i++, y1 += yChange) {
						g.drawLine(i, y + y1, i, y + y1);
						if (y1 == yChange) {
							yChange = -yChange;
						}
					}
				}
				g.setColor(oldColor);

			}
		}
	}
}
