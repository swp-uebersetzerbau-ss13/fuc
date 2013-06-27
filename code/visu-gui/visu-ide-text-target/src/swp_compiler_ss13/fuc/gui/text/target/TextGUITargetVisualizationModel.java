package swp_compiler_ss13.fuc.gui.text.target;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import swp_compiler_ss13.fuc.gui.text.StringColourPair;
import swp_compiler_ss13.fuc.gui.text.StringColourPair.DefaultColorWrapper;
import swp_compiler_ss13.fuc.gui.text.Text_Controller;
import swp_compiler_ss13.fuc.gui.text.Text_Model;

/**
 * @author "Eduard Wolf"
 * 
 */
public class TextGUITargetVisualizationModel extends Text_Model {

	private static final Logger LOG = Logger.getLogger(TextGUITargetVisualizationModel.class);

	private Map<String, InputStream> lastTarget;
	private boolean showFullCode = true;

	public TextGUITargetVisualizationModel(Text_Controller controller) {
		super(controller, ModelType.TARGET);
	}

	@Override
	protected List<StringColourPair> targetToViewInformation(Map<String, InputStream> target) {
		lastTarget = target;
		List<StringColourPair> result = new LinkedList<>();
		if (target != null) {
			BufferedReader reader;
			String line;
			for (Entry<String, InputStream> entry : target.entrySet()) {
				if ("main.ll".equals(entry.getKey())) {
					StringBuilder code = new StringBuilder();
					boolean inMain = false;
					String codeStart = "define i64 @main() {";
					result.add(new StringColourPair().setText(entry.getKey() + ":\n").setColor(
							DefaultColorWrapper.BLUE));
					reader = new BufferedReader(new InputStreamReader(entry.getValue()));
					try {
						while ((line = reader.readLine()) != null) {
							code.append(line);
							code.append('\n');
							if (codeStart.equals(line)) {
								inMain = true;
							} else if (line.isEmpty()) {
								if (inMain) {}
								inMain = false;
							}
							LOG.debug("line: " + line);
							if (showFullCode || inMain) {
								if (line.startsWith(";")) {
									result.add(new StringColourPair().setText(line + "\n")
											.setColor(DefaultColorWrapper.GRAY));
								} else {
									result.add(new StringColourPair().setText(line + "\n"));
								}
							}
						}
						if (code.length() != 0) {
							code.setLength(code.length() - 1);
						}
					} catch (IOException e) {
						LOG.error("bad inputstream in from target code generator", e);
					} finally {
						try {
							reader.close();
						} catch (IOException e) {}
					}
					entry.setValue(new ByteArrayInputStream(code.toString().getBytes()));
					// only one file in llvm backend
					break;
				} else {
					// TODO java code
				}
			}
		}
		return result;
	}

	public void toogleCodeAmount() {
		showFullCode = !showFullCode;
		setTargetCode(lastTarget);
	}
}
