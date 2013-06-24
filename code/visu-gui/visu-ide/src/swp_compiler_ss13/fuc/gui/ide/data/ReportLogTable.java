package swp_compiler_ss13.fuc.gui.ide.data;

import java.awt.Component;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.LogEntry.Type;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.gui.ide.FucIdeView;

public class ReportLogTable extends JTable {

	private class ImageRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			JLabel lbl = ((JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column));
			lbl = new JLabel((ImageIcon) value);
			return lbl;
		}
	}

	private ImageIcon error = new ImageIcon(
			FucIdeView.class.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/error.png"));
	private ImageIcon warning = new ImageIcon(
			FucIdeView.class.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/warning.png"));

	public ReportLogTable() {
		super(new DefaultTableModel(new String[] { "", "", "Type", "Line", "Column", "Message" }, 0));
		this.setUpModel();

	}

	private void setUpModel() {

		this.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(30);
		this.getTableHeader().getColumnModel().getColumn(0).setMinWidth(30);
		this.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(30);

		this.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(30);
		this.getTableHeader().getColumnModel().getColumn(1).setMinWidth(30);
		this.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(30);

		this.getTableHeader().getColumnModel().getColumn(2).setMinWidth(150);
		this.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(150);
		this.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(150);

		this.getTableHeader().getColumnModel().getColumn(3).setMinWidth(50);
		this.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(50);
		this.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(50);

		this.getTableHeader().getColumnModel().getColumn(4).setMinWidth(50);
		this.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(50);
		this.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(50);

		TableColumn tc = this.getColumnModel().getColumn(1);
		tc.setCellRenderer(new ImageRenderer());
	}

	public void displayReportLog(ReportLogImpl rli) {
		this.clearTable();

		for (LogEntry entry : rli.getEntries()) {
			this.addRow(entry);
		}
	}

	public void clearTable() {
		this.setModel(new DefaultTableModel(new String[] { "", "", "Type", "Line", "Column", "Message" }, 0));
		this.setUpModel();
	}

	public void addRow(LogEntry entry) {
		Object[] row = new Object[6];
		row[0] = this.getModel().getRowCount() + 1;
		row[1] = entry.getLogType() == Type.ERROR ? this.error : this.warning;
		row[2] = entry.getReportType().toString();
		row[3] = this.getLines(entry.getTokens());
		row[4] = this.getColumns(entry.getTokens());
		row[5] = entry.getMessage();

		((DefaultTableModel) this.getModel()).addRow(row);
	}

	public String getLines(List<Token> tokens) {
		int max = -1;
		int min = -1;

		if (tokens == null) {
			return "" + min;
		}

		for (Token t : tokens) {
			max = Math.max(max, t.getLine());
			if (min < 0) {
				min = t.getLine();
			}
			min = Math.min(min, t.getLine());
		}

		if (min == max) {
			return "" + min;
		}
		return min + " - " + max;
	}

	public String getColumns(List<Token> tokens) {
		int max = -1;
		int min = -1;

		if (tokens == null) {
			return "" + min;
		}

		for (Token t : tokens) {
			max = Math.max(max, t.getColumn());
			if (min < 0) {
				min = t.getColumn();
			}
			min = Math.min(min, t.getColumn());
		}

		if (min == max) {
			return "" + min;
		}
		return min + " - " + max;
	}
}
