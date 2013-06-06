package swp_compiler_ss13.fuc.gui.text;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Model;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;
import swp_compiler_ss13.fuc.gui.text.Text_Model.ModelType;

/**
 * 
 * Controller of {@link IDE} to transfer data from {@link Text_Model} to
 * {@link Text_View}
 * 
 * @author "Eduard Wolf"
 * 
 */
public class Text_Controller implements Controller {

	private final Text_View view;
	private Text_Model model;

	public Text_Controller(Text_View view) {
		this.view = view;
	}

	public Text_Controller(Position position) {
		this.view = new Text_View(this, position);
	}

	protected void initModel(Text_Model model) {
		if (this.model != null) {
			throw new IllegalStateException("model is already initialized");
		}
		if (model == null) {
			throw new NullPointerException("model can't be null");
		}
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView() {
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model getModel() {
		chackModel();
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyModelChanged() {
		chackModel();
		final ModelType type;
		switch (view.getPosition()) {
		case TOKENS:
			type = ModelType.TOKEN;
			break;
		case AST:
		case CHECKED_AST:
			type = ModelType.AST;
			break;
		case TAC:
			type = ModelType.TAC;
			break;
		case TARGET_CODE:
			type = ModelType.TARGET;
			break;
		default:
			type = null;
			break;
		}
		view.setViewInformation(model.getViewInformation(type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IDE ide) {
		view.initComponents(ide);
	}

	private void chackModel() {
		if (model == null) {
			throw new IllegalStateException("model isn't initialized");
		}
	}
}
