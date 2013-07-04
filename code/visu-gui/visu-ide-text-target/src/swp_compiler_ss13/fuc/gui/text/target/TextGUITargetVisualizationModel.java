package swp_compiler_ss13.fuc.gui.text.target;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
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
	private static final char[] HEX_ARRAY = { '0','1','2','3','4','5','6','7','8','9','A','B','C',
			'D','E','F' };

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
						LOG.error("bad inputstream from target code generator", e);
					} finally {
						try {
							reader.close();
						} catch (IOException e) {}
					}
					entry.setValue(new ByteArrayInputStream(code.toString().getBytes()));
					// only one file in llvm backend
					break;
				} else {
					StringBuilder code = new StringBuilder();
					ByteArrayOutputStream returnStream = new ByteArrayOutputStream();
					result.add(new StringColourPair().setText(entry.getKey() + ":\n").setColor(
							DefaultColorWrapper.BLUE));
					List<JavaInformation> infos = new LinkedList<>();
					infos.add(new JavaInformation("header", 4, true, returnStream));
					infos.add(new JavaInformation("minor version number", 2, true, returnStream));
					infos.add(new JavaInformation("major version number", 2, true, returnStream));
					infos.add(new JavaInformation("constant pool count", 2, true, returnStream));
					for (JavaInformation info : infos) {
						result.addAll(info.getInformation(entry.getValue()));
					}
					int read;
					byte[] bytes = new byte[50];
					try {
						while ((read = entry.getValue().read(bytes)) < 50) {
							if (showFullCode) {
								result.add(new StringColourPair().setText(bytesToHex(bytes) + "\n"));
							}
							returnStream.write(bytes);
						}
						if (read > 0) {
							if (showFullCode) {
								result.add(new StringColourPair().setText(bytesToHex(bytes)));
							}
							returnStream.write(bytes);
						}
					} catch (IOException e) {
						LOG.error("bad inputstream from target code generator", e);
					} finally {
						try {
							entry.getValue().close();
						} catch (IOException e) {}
					}
					entry.setValue(new ByteArrayInputStream(returnStream.toByteArray()));
				}
			}
		}
		return result;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder(bytes.length * 2);
		for (int j = 0, v; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			builder.append(HEX_ARRAY[v >>> 4]);
			builder.append(HEX_ARRAY[v & 0xF]);
		}
		return builder.toString();
	}

	public void toogleCodeAmount() {
		showFullCode = !showFullCode;
		setTargetCode(lastTarget);
	}

	private class JavaInformation {
		private String name;
		private int size;
		private boolean toHex;
		private ByteArrayOutputStream resultStream;

		public JavaInformation(String name, int size, boolean toHex,
				ByteArrayOutputStream resultStream) {
			this.name = name;
			this.size = size;
			this.toHex = toHex;
			this.resultStream = resultStream;
		}

		public List<StringColourPair> getInformation(InputStream stream) {
			StringColourPair header = new StringColourPair().setText(name + "\n").setColor(
					DefaultColorWrapper.GREEN);
			int offset = 0;
			byte[] bytes = new byte[size];
			try {
				int read = 0;
				do {
					offset += read;
					read = stream.read(bytes, offset, size - offset);
				} while (read > -1 && offset + read < size);
				if (read == -1) {
					return Collections.emptyList();
				}
			} catch (IOException e) {
				LOG.error("cant read outputstream", e);
				return Collections.emptyList();
			}
			try {
				resultStream.write(bytes);
			} catch (IOException e) {
				LOG.error("cant write outputstream", e);
			}
			StringColourPair information = new StringColourPair();
			information.setText((toHex ? bytesToHex(bytes) : new String(bytes)) + "\n");
			return Arrays.asList(header,information);
		}
	}
}
