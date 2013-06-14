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

public class UnderlineStyledEditorKit extends StyledEditorKit {

	private static final long serialVersionUID = 1L;
	private final ViewFactory defaultFactory;

	public static final Object UNDERLINE_COLOR = "underline-color";

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

		static class UnderlineColorLabelView extends LabelView {

			public UnderlineColorLabelView(Element elem) {
				super(elem);
			}

			@Override
			public void paint(Graphics g, Shape a) {
				super.paint(g, a);
				paintUnderline(g, a);
			}

			protected void paintUnderline(Graphics g, Shape a) {
				AttributeSet attributes = getElement().getAttributes();
				if (StyleConstants.isUnderline(attributes)) {
					Color newColor = (Color) attributes.getAttribute(UNDERLINE_COLOR);
					if (newColor != null) {
						/*
						 * copy from superclass
						 */
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
							x0 += (int) getGlyphPainter()
									.getSpan(this, p, p0, getTabExpander(), x0);
						}
						int x1 = x0
								+ (int) getGlyphPainter().getSpan(this, p0, p1, getTabExpander(),
										x0);

						// calculate y coordinate
						int y = alloc.y + alloc.height - (int) getGlyphPainter().getDescent(this)
								+ 1;

						Color oldColor = g.getColor();
						g.setColor(newColor);
						g.drawLine(x0, y, x1, y);
						g.setColor(oldColor);
					}
				}
			}
		}
	}
}
