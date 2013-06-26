package swp_compiler_ss13.fuc.gui.ast;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

/**
 * @author "Eduard Wolf"
 * 
 */
public class AST_View implements View {

	private final AST_Component component;
	private final AST_Controller controller;

	public AST_View(AST_Controller controller, boolean isRoot) {
		this.controller = controller;
		component = new AST_Component(controller, isRoot);
	}

	public AST_View setNode(ASTNode node) {
		component.setNode(node);
		return this;
	}

	public void addChild(AST_View child) {
		component.addChild(child.component);
	}

	public void changeChildState() {
		component.changeChildState();
	}

	public void recalculateLayout() {
		component.recalculateLayout();
	}
	
	public void toggleNodeSize(){
		component.toggleSize();
	}

	@Override
	public String getName() {
		return "AST";
	}

	@Override
	public JComponent getComponent() {
		return component.getComponent();
	}

	@Override
	public Position getPosition() {
		return Position.AST;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public void initComponents(IDE ide) {
		if (component.isRoot()) {
			JButton button = new JButton("toggle node size");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					controller.toggleNodeSize();
				}
			});
			ide.addButton(button, getPosition(), false);
		}
	}
}
