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

	private Text_View view;
	private Text_Model model;

	public Text_Controller() {}

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

	protected void initView(Text_View view) {
		if (this.view != null) {
			throw new IllegalStateException("view is already initialized");
		}
		if (view == null) {
			throw new NullPointerException("view can't be null");
		}
		this.view = view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView() {
		return this.view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model getModel() {
		this.checkModelAndView();
		return this.model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyModelChanged() {
		this.checkModelAndView();
		final ModelType type;
		switch (this.view.getPosition()) {
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
		case RESULT:
			type = ModelType.RESULT;
			break;
		default:
			type = null;
			break;
		}
		this.view.setViewInformation(this.model.getViewInformation(type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IDE ide) {
		this.view.initComponents(ide);
	}

	private void checkModelAndView() {
		if (this.model == null) {
			throw new IllegalStateException("model isn't initialized");
		}
		if (this.view == null) {
			throw new IllegalStateException("view isn't initialized");
		}
	}
}
