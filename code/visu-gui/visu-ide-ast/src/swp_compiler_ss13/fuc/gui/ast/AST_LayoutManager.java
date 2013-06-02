package swp_compiler_ss13.fuc.gui.ast;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

class AST_LayoutManager implements LayoutManager {

	public static final String BUTTON = "fuc.visualisation.ast.BUTTON";
	public static final String CHILDREN = "fuc.visualisation.ast.CHILDREN";
	public static final String ARROWS = "fuc.visualisation.ast.ARROWS";

	private Component buttonComponent;
	private final List<Component> children;
	private Arrow_Panel arrows;
	private int preferredWidth;
	private int preferredHeight;
	private int minWidth;
	private int minHeight;
	private static final int VGAP = 50;
	private static final int HGAP = 20;

	public AST_LayoutManager() {
		children = new ArrayList<>();
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		switch (name) {
		case BUTTON:
			if (buttonComponent == null) {
				buttonComponent = comp;
			} else {
				throw new IllegalStateException("Layout manager has already a button component");
			}
			break;
		case CHILDREN:
			children.add(comp);
			break;
		case ARROWS:
			if (arrows != null) {
				throw new IllegalStateException("Layout manager has already an arrow panel");
			} else if (comp instanceof Arrow_Panel) {
				arrows = (Arrow_Panel) comp;
				break;
			} else {
				throw new IllegalArgumentException("Component must be of type ArrowPanel");
			}
		default:
			throw new IllegalArgumentException("Unknown component name: " + name);
		}
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (comp == buttonComponent) {
			buttonComponent = null;
		} else if (comp == arrows) {
			arrows = null;
		}
		children.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		setSizes(parent);
		return new Dimension(preferredWidth + insets.left + insets.right, preferredHeight
				+ insets.bottom + insets.top);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		setSizes(parent);
		return new Dimension(minWidth + insets.left + insets.right, minHeight + insets.bottom
				+ insets.top);
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int x = insets.left;
		int y = insets.top;
		int buttonMiddle = preferredWidth / 2;
		int arrowsLeft = Integer.MAX_VALUE;
		int arrowsRight = Integer.MIN_VALUE;
		if (buttonComponent != null) {
			setSizes(parent);
			Dimension dim = buttonComponent.getPreferredSize();
			int buttonBottom = y + dim.height;
			buttonComponent.setBounds(buttonMiddle - dim.width / 2, y, dim.width, dim.height);
			y += dim.height + VGAP;
			if (arrows != null) {
				arrows.deletePoints();
			}
			for (int i = 0; i < children.size(); i++) {
				Component comp = children.get(i);
				dim = comp.getPreferredSize();
				comp.setBounds(x, y, dim.width, dim.height);
				if (i == 0) {
					arrowsLeft = x + dim.width / 2;
				}
				arrowsRight = x + dim.width / 2;
				arrows.addArrow(buttonMiddle - arrowsLeft + 2, 0, arrowsRight - arrowsLeft + 2, y
						- buttonBottom);
				x += dim.width + HGAP;
			}
			if (arrows != null && arrowsLeft < Integer.MAX_VALUE) {
				arrows.setBounds(arrowsLeft - 2, buttonBottom, arrowsRight - arrowsLeft + 4, y
						- buttonBottom);

			}
		}
	}

	private void setSizes(Container parent) {
		Dimension dimension = null;

		// Reset preferred/minimum width and height.
		preferredWidth = 0;
		preferredHeight = 0;
		minWidth = 0;
		minHeight = 0;
		if (buttonComponent != null) {
			for (int i = 0; i < children.size(); i++) {
				Component child = children.get(i);
				if (child.isVisible()) {
					dimension = child.getPreferredSize();
					preferredWidth += dimension.width + HGAP;
					preferredHeight = Math.max(preferredHeight, dimension.height);
					dimension = child.getMinimumSize();
					minWidth += dimension.width;
					minHeight = Math.max(minHeight, dimension.height);
				}
			}
			if (preferredWidth > 0) {
				preferredWidth -= HGAP;
			}
			dimension = buttonComponent.getPreferredSize();
			preferredWidth = Math.max(preferredWidth, dimension.width);
			preferredHeight += (preferredHeight == 0 ? 0 : VGAP) + dimension.height;
			dimension = buttonComponent.getMinimumSize();
			minWidth = Math.max(minWidth, dimension.width);
			minHeight += (minHeight == 0 ? 0 : VGAP) + dimension.height;
		}
	}

}
