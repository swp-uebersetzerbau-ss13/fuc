package swp_compiler_ss13.fuc.gui.ide;

import java.util.Arrays;
import java.util.List;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeMenu;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeTab;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;

import com.sun.istack.internal.logging.Logger;

/**
 * The FUC IDE Controllre
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeController {
	/**
	 * The model
	 */
	private FucIdeModel model;
	/**
	 * The view
	 */
	private FucIdeView view;

	private static Logger logger = Logger.getLogger(FucIdeController.class);

	/**
	 * Instantiate a new instance of the controller
	 */
	public FucIdeController() {
		this.model = new FucIdeModel(this);
		this.view = new FucIdeView(this);

		this.initComponents();

		this.view.setVisible(true);
	}

	/**
	 * Init the compiler copmonents
	 */
	private void initComponents() {
		List<Lexer> lexers = this.model.getLexers();
		for (Lexer lexer : lexers) {
			this.view.addComponentRadioMenuItem(lexer);
		}
		List<Parser> parsers = this.model.getParsers();
		for (Parser parser : parsers) {
			this.view.addComponentRadioMenuItem(parser);
		}
		List<SemanticAnalyser> semanticAnalyzers = this.model.getSemanticAnalysers();
		for (SemanticAnalyser sa : semanticAnalyzers) {
			this.view.addComponentRadioMenuItem(sa);
		}
		List<IntermediateCodeGenerator> irgs = this.model.getIntermediateCodeGenerators();
		for (IntermediateCodeGenerator irg : irgs) {
			this.view.addComponentRadioMenuItem(irg);
		}
		List<Backend> backends = this.model.getBackends();
		for (Backend backend : backends) {
			this.view.addComponentRadioMenuItem(backend);
		}

		if (lexers.size() == 0) {
			String error = String
					.format("No implementation for %s was found in the classpath.\nThe compiler will not work.",
							Lexer.class);
			new FucIdeCriticalError(this.view, error, false);
		}

		if (parsers.size() == 0) {
			String error = String
					.format("No implementation for %s was found in the classpath.\nThe compiler will not work.",
							Parser.class);
			new FucIdeCriticalError(this.view, error, false);
		}

		if (semanticAnalyzers.size() == 0) {
			String error = String
					.format("No implementation for %s was found in the classpath.\nThe compiler will not work.",
							SemanticAnalyser.class);
			new FucIdeCriticalError(this.view, error, false);
		}

		if (irgs.size() == 0) {
			String error = String
					.format("No implementation for %s was found in the classpath.\nThe compiler will not work.",
							IntermediateCodeGenerator.class);
			new FucIdeCriticalError(this.view, error, false);
		}

		if (backends.size() == 0) {
			String error = String
					.format("No implementation for %s was found in the classpath.\nThe compiler will not work.",
							Backend.class);
			new FucIdeCriticalError(this.view, error, false);
		}

		List<Controller> cl = this.model.getGUIControllers();
		for (Controller c : cl) {
			logger.info("Initializing gui component " + c.getClass().getName());
			c.init(this.model);
			boolean notify = false;
			notify = notify || c.getModel().setSourceCode("");
			notify = notify || c.getModel().setTokens(null);
			notify = notify || c.getModel().setAST(null);
			notify = notify || c.getModel().setTAC(null);
			notify = notify || c.getModel().setTargetCode(null);
			if (notify) {
				logger.info("notifying the controller " + c.getClass().getName() + " about model changes");
				c.notifyModelChanged();
			}
			this.model.addTab(c);
			this.notifyModelTab();
		}
	}

	public void notifyModelAddedMenu() {
		FucIdeMenu[] menus = this.model.getMenus().toArray(new FucIdeMenu[] {});
		Arrays.sort(menus);

		this.view.clearMenus();
		for (FucIdeMenu menu : menus) {
			this.view.addMenu(menu);
		}
	}

	public void notifyModelAddedButton() {

	}

	public void notifyModelAddedLabel() {

	}

	public void notifyModelTab() {
		FucIdeTab[] tabs = this.model.getTabs().toArray(new FucIdeTab[] {});
		Arrays.sort(tabs);

		this.view.clearTabs();
		for (FucIdeTab tab : tabs) {
			logger.info("adding tab " + tab.getName());
			this.view.addTab(tab.getName(), tab.getComponent());
		}
	}

	public static void main(String[] args) {
		new FucIdeController();
	}
}
