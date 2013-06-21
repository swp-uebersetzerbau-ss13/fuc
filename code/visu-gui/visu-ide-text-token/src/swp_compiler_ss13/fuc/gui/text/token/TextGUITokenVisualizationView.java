package swp_compiler_ss13.fuc.gui.text.token;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.text.Text_View;

public class TextGUITokenVisualizationView extends Text_View {

	private final TextGUITokenVisualizationController tokenController;

	public TextGUITokenVisualizationView(TextGUITokenVisualizationController controller) {
		super(controller, Position.TOKENS);
		tokenController = controller;
	}
	
	@Override
	public void initComponents(IDE ide) {
		super.initComponents(ide);
		JButton button = new JButton("toggle show line and column");
		button.addActionListener(new InfoListener());
		ide.addButton(button, Position.TOKENS, false);
	}
	
	private class InfoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			tokenController.toggleLineColumnInfo();
		}
		
	}

}
