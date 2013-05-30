package swp_compiler_ss13.fuc.gui.ide;

import java.util.ServiceLoader;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;

public class FucIdeController {
	private FucIdeModel model;
	private FucIdeView view;
	private ServiceLoader<Lexer> lexerComponents;
	private ServiceLoader<Parser> parserComponents;
	private ServiceLoader<SemanticAnalyser> semanticAnalysisComponents;
	private ServiceLoader<IntermediateCodeGenerator> irgenComponents;
	private ServiceLoader<Backend> backendComponents;

	public FucIdeController() {
		this.model = new FucIdeModel(this);
		this.view = new FucIdeView(this);

		this.loadModules();

		this.view.setVisible(true);
	}

	private void loadModules() {
		this.lexerComponents = ServiceLoader.load(Lexer.class);
		this.parserComponents = ServiceLoader.load(Parser.class);
		this.semanticAnalysisComponents = ServiceLoader.load(SemanticAnalyser.class);
		this.irgenComponents = ServiceLoader.load(IntermediateCodeGenerator.class);
		this.backendComponents = ServiceLoader.load(Backend.class);

		boolean lexerFound = false;
		boolean parserFound = false;
		boolean semanticFound = false;
		boolean irgenFound = false;
		boolean backendFound = false;

		for (Lexer lex : this.lexerComponents) {
			this.view.addComponentRadioMenuItem(lex);
			lexerFound = true;
		}

		for (Parser parser : this.parserComponents) {
			this.view.addComponentRadioMenuItem(parser);
			parserFound = true;
		}

		for (SemanticAnalyser sem : this.semanticAnalysisComponents) {
			this.view.addComponentRadioMenuItem(sem);
			semanticFound = true;
		}

		for (IntermediateCodeGenerator irgen : this.irgenComponents) {
			this.view.addComponentRadioMenuItem(irgen);
			irgenFound = true;
		}

		for (Backend backend : this.backendComponents) {
			this.view.addComponentRadioMenuItem(backend);
			backendFound = true;
		}

		if (!lexerFound) {
			new FucIdeError(this.view, "No implementation for " + Lexer.class.getName() + " found in the classpath!");
		}

		if (!parserFound) {
			new FucIdeError(this.view, "No implementation for " + Parser.class.getName() + " found in the classpath!");
		}

		if (!semanticFound) {
			new FucIdeError(this.view, "No implementation for " + SemanticAnalyser.class.getName()
					+ " found in the classpath!");
		}

		if (!backendFound) {
			new FucIdeError(this.view, "No implementation for " + Backend.class.getName() + " found in the classpath!");
		}

		if (!irgenFound) {
			new FucIdeError(this.view, "No implementation for " + IntermediateCodeGenerator.class.getName()
					+ " found in the classpath!");
		}
	}

	public static void main(String[] args) {
		new FucIdeController();
	}
}
