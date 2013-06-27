package swp_compiler_ss13.fuc.gui.text.target;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.text.Text_View;

public class TextGUITargetVisualizationView extends Text_View {

	private final TextGUITargetVisualizationController targetController;

	public TextGUITargetVisualizationView(TextGUITargetVisualizationController controller) {
		super(controller, Position.TARGET_CODE);
		targetController = controller;
	}
	
	@Override
	public void initComponents(IDE ide) {
		super.initComponents(ide);
		JButton button = new JButton("toggle show full code");
		button.addActionListener(new InfoListener());
		ide.addButton(button, Position.TARGET_CODE, false);
	}
	
	private class InfoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			targetController.toggleFullCode();
		}
		
	}

}
