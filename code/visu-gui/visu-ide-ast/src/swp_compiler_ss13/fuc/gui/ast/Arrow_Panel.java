package swp_compiler_ss13.fuc.gui.ast;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

/**
 * Component to draw arrows.<br/>
 * The points can be set by {@link #addArrow}.
 * 
 * @author "Eduard Wolf"
 * 
 */
class Arrow_Panel extends JLabel {

	private static final long serialVersionUID = 2703529835276986317L;

	private final int ARR_SIZE = 4;
	private final List<int[]> points = new ArrayList<>();

	@Override
	protected void paintComponent(Graphics g) {
		if (g instanceof Graphics2D) {
			Graphics2D graphics = (Graphics2D) g;
			graphics.setColor(Color.BLACK);
			AffineTransform transform = graphics.getTransform();
			for (int[] point : points) {
				int x1 = point[0];
				int y1 = point[1];
				int x2 = point[2];
				int y2 = point[3];
				double dx = x2 - x1, dy = y2 - y1;
				double angle = Math.atan2(dy, dx);
				int len = (int) Math.sqrt(dx * dx + dy * dy);
				AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
				at.concatenate(AffineTransform.getRotateInstance(angle));
				graphics.transform(at);

				g.drawLine(0, 0, len, 0);
				g.fillPolygon(new int[] { len,len - ARR_SIZE,len - ARR_SIZE,len }, new int[] { 0,
						-ARR_SIZE,ARR_SIZE,0 }, 4);
				graphics.setTransform(transform);
			}
		}
	}

	public void addArrow(int x1, int y1, int x2, int y2) {
		points.add(new int[] { x1,y1,x2,y2 });
	}

	public void deletePoints() {
		points.clear();
	}

}
