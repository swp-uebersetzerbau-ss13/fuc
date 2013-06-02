package swp_compiler_ss13.fuc.gui.ide;

import java.util.List;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;

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
	}

	public void notifyModelAddedMenu() {

	}

	public void notifyModelAddedButton() {

	}

	public void notifyModelAddedLabel() {

	}

	public static void main(String[] args) {
		new FucIdeController();
	}
}
