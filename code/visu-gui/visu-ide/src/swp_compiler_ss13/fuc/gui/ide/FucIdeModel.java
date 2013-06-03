package swp_compiler_ss13.fuc.gui.ide;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeButton;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeMenu;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeStatusLabel;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeTab;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

/**
 * FUC IDE Model
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeModel implements IDE {
	/**
	 * The controller associated with this model
	 */
	private FucIdeController controller;

	/**
	 * Instances of found lexer implementations
	 */
	private List<Lexer> lexer_instances = new LinkedList<>();

	/**
	 * Instances of found parser implementations
	 */
	private List<Parser> parser_instances = new LinkedList<>();

	/**
	 * Instances of found semantic analyzer implementations
	 */
	private List<SemanticAnalyser> semanticAnalyser_instances = new LinkedList<>();

	/**
	 * Instances of found intermediate code generator implementations
	 */
	private List<IntermediateCodeGenerator> intermediateCodeGenerator_instances = new LinkedList<>();

	/**
	 * Instances of found backend implementations
	 */
	private List<Backend> backend_instances = new LinkedList<>();

	/**
	 * List of menus
	 */
	private List<FucIdeMenu> menus = new LinkedList<>();

	/**
	 * List of tabs
	 */
	private List<FucIdeTab> tabs = new LinkedList<>();

	/**
	 * List of status labels
	 */
	private List<FucIdeStatusLabel> labels = new LinkedList<>();

	/**
	 * List of buttons
	 */
	private List<FucIdeButton> buttons = new LinkedList<>();

	/**
	 * All gui component controller instances
	 */
	private List<Controller> controller_instances = new LinkedList<>();

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(FucIdeModel.class);

	private FucIdeTab activeTab = null;

	private Lexer activeLexer;

	private Parser activeParser;

	private SemanticAnalyser activeAnalyzer;

	private IntermediateCodeGenerator activeIRG;

	private Backend activeBackend;

	private String sourcecode;

	/**
	 * Initialize the model
	 * 
	 * @param controller
	 *            The controller associated with this model
	 */
	public FucIdeModel(FucIdeController controller) {
		this.controller = controller;
		this.loadModules();
		this.loadVisualisations();
	}

	/**
	 * Load the compiler modules
	 */
	private void loadModules() {
		for (Lexer lexer : ServiceLoader.load(Lexer.class)) {
			logger.info("Found lexer: " + lexer.getClass().getName());
			this.lexer_instances.add(lexer);
		}
		for (Parser parser : ServiceLoader.load(Parser.class)) {
			logger.info("Found Parser: " + parser.getClass().getName());
			this.parser_instances.add(parser);
		}
		for (SemanticAnalyser sa : ServiceLoader.load(SemanticAnalyser.class)) {
			logger.info("Found SemanticAnalyser: " + sa.getClass().getName());
			this.semanticAnalyser_instances.add(sa);
		}
		for (IntermediateCodeGenerator ir : ServiceLoader.load(IntermediateCodeGenerator.class)) {
			logger.info("Found IntermediateCodeGenerator: " + ir.getClass().getName());
			this.intermediateCodeGenerator_instances.add(ir);
		}
		for (Backend backend : ServiceLoader.load(Backend.class)) {
			logger.info("Found Backend: " + backend.getClass().getName());
			this.backend_instances.add(backend);
		}
	}

	/**
	 * Load all visualisation components
	 */
	private void loadVisualisations() {

		try {
			for (Controller controller : ServiceLoader.load(Controller.class)) {
				logger.info("Found gui controller: " + controller.getClass().getName());
				this.controller_instances.add(controller);
			}
		} catch (ServiceConfigurationError e) {
			new FucIdeCriticalError(this.controller.getView(), e, false);
		}
	}

	@Override
	public void addMenu(JMenu menu, Position position, boolean displayAlways) {
		this.menus.add(new FucIdeMenu(displayAlways, position, menu));
		this.controller.notifyModelAddedMenu();
	}

	@Override
	public void addButton(JButton button, Position position, boolean displayAlways) {
		this.buttons.add(new FucIdeButton(displayAlways, position, button));
		this.controller.notifyModelAddedButton();
	}

	@Override
	public void addStatusLabel(JLabel label, Position position, boolean displayAlways) {
		this.labels.add(new FucIdeStatusLabel(displayAlways, position, label));
		this.controller.notifyModelAddedLabel();
	}

	/**
	 * Return the list of lexer instances
	 * 
	 * @return list of lexer instances
	 */
	public List<Lexer> getLexers() {
		return this.lexer_instances;
	}

	/**
	 * Return the list of parser instances
	 * 
	 * @return list of parser instances
	 */
	public List<Parser> getParsers() {
		return this.parser_instances;
	}

	/**
	 * Return the list of semanticAnalyzer instances
	 * 
	 * @return list of semanticAnalyzer instances
	 */
	public List<SemanticAnalyser> getSemanticAnalysers() {
		return this.semanticAnalyser_instances;
	}

	/**
	 * Return the list of intermediateCodeGenerator instances
	 * 
	 * @return list of intermediateCodeGenerator instances
	 */
	public List<IntermediateCodeGenerator> getIntermediateCodeGenerators() {
		return this.intermediateCodeGenerator_instances;
	}

	/**
	 * Return the list of backend instances
	 * 
	 * @return list of backend instances
	 */
	public List<Backend> getBackends() {
		return this.backend_instances;
	}

	/**
	 * Return the list of controller instances
	 * 
	 * @return list of controller instances
	 */
	public List<Controller> getGUIControllers() {
		return this.controller_instances;
	}

	public void addTab(Controller c) {
		this.tabs.add(new FucIdeTab(c, c.getView().getComponent(), c.getView().getName(), c.getView().getPosition()));
	}

	public List<FucIdeTab> getTabs() {
		return this.tabs;
	}

	public List<FucIdeMenu> getMenus() {
		return this.menus;
	}

	public List<FucIdeButton> getButtons() {
		return this.buttons;
	}

	public List<FucIdeStatusLabel> getLabels() {
		return this.labels;
	}

	public FucIdeTab getActiveTab() {
		return this.activeTab;
	}

	public void setActiveTab(FucIdeTab tab) {
		this.activeTab = tab;
	}

	public Position getActivePosition() {
		if (this.activeTab == null) {
			return null;
		}
		return this.activeTab.getPosition();
	}

	public void setActiveLexer(Lexer lexer) {
		this.activeLexer = lexer;
	}

	public void setActiveParser(Parser parser) {
		this.activeParser = parser;
	}

	public void setActiveAnalyzer(SemanticAnalyser analyzer) {
		this.activeAnalyzer = analyzer;
	}

	public void setActiveIRG(IntermediateCodeGenerator irgen) {
		this.activeIRG = irgen;
	}

	public void setActiveBackend(Backend backend) {
		this.activeBackend = backend;
	}

	public Lexer getActiveLexer() {
		return this.activeLexer;
	}

	public Parser getActiveParser() {
		return this.activeParser;
	}

	public SemanticAnalyser getActiveAnalyzer() {
		return this.activeAnalyzer;
	}

	public IntermediateCodeGenerator getActiveIRG() {
		return this.activeIRG;
	}

	public Backend getActiveBackend() {
		return this.activeBackend;
	}

	public String getSourceCode() {
		return this.sourcecode;
	}

	@Override
	public void setSourceCode(String sourceCode) {
		this.sourcecode = sourceCode;
		this.controller.notifySourceCodeChanged();
	}
}
