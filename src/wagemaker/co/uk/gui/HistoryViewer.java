package wagemaker.co.uk.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

// ...unused imports removed
import javax.swing.JButton;
import javax.swing.JDialog;
// ...unused imports removed
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import wagemaker.co.uk.utility.STPFileCrypter;
import wagemaker.co.uk.utility.logHistory;

import wagemaker.co.uk.utility.Details;
import wagemaker.co.uk.lang.LangS;
import wagemaker.co.uk.main.Launcher;

public class HistoryViewer extends JDialog {

    private static final long serialVersionUID = 1L;
    Locale locale = Locale.of(LangS.getLanguage());
    ResourceBundle labels = ResourceBundle.getBundle("wagemaker.co.uk.lang.LabelsBundle", locale);

    public static class Entry {
        public final String timestamp;
        public final String action;
        public final String path;
        public Entry(String t, String a, String p) { timestamp=t; action=a; path=p; }
        public String toString() { return timestamp+" — "+action+" — "+path; }
    }

    private HistoryTableModel tableModel;
    private JTable table;

    public HistoryViewer() throws IOException {
        super((javax.swing.JFrame) null, "SmallTextPad History Picker", true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 400));

        java.util.List<Entry> entries = loadEntries();

        tableModel = new HistoryTableModel(entries);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        // checkbox column width
        TableColumnModel tcm = table.getColumnModel();
        if (tcm.getColumnCount() > 0) {
            tcm.getColumn(0).setPreferredWidth(30);
            tcm.getColumn(1).setPreferredWidth(220);
            tcm.getColumn(2).setPreferredWidth(120);
            tcm.getColumn(3).setPreferredWidth(520);
        }

        // attach renderers: checkbox renderer and gray-out renderer for other columns
        tcm.getColumn(0).setCellRenderer(new TableCheckRenderer());
        RowStatusRenderer rowRenderer = new RowStatusRenderer();
        tcm.getColumn(1).setCellRenderer(rowRenderer);
        tcm.getColumn(2).setCellRenderer(rowRenderer);
        tcm.getColumn(3).setCellRenderer(rowRenderer);

        // double-click to open
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    if (row >= 0) {
                        String p = tableModel.getPathAt(row);
                        SwingUtilities.invokeLater(() -> {
                            openPath(p);
                        });
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton openBtn = new JButton(labels.getString("label009"));
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // attempt to open first checked entry, otherwise selected row
                String chosen = null;
                for (String path : tableModel.getCheckedPaths()) { chosen = path; break; }
                if (chosen == null) {
                    int selRow = table.getSelectedRow();
                    if (selRow < 0) return;
                    int modelRow = table.convertRowIndexToModel(selRow);
                    chosen = tableModel.getPathAt(modelRow);
                }
                if (chosen == null) return;
                final String pToOpen = chosen;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        openPath(pToOpen);
                    }
                });
            }
        });
        bottom.add(openBtn);

        JButton closeBtn = new JButton(labels.getString("label037"));
        closeBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { HistoryViewer.this.dispose(); } });
        bottom.add(closeBtn);

        add(bottom, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private java.util.List<Entry> loadEntries() throws IOException {
        File f = Details.historyFile();
        if (!f.exists()) return new ArrayList<>();
        BufferedReader r = new BufferedReader(new FileReader(f));
        String line;
        List<Entry> entries = new ArrayList<>();
        while ((line = r.readLine()) != null) {
            line = line.trim();
            if (!line.startsWith("<tr>")) continue;
            // crude parse: expect <tr><td...>timestamp</td><td...>action</td><td...>path</td></tr>
            try {
                String t = between(line, "<td", "</td>");
                // remove any >content< in t
                t = stripTags(t).trim();
                String rest = line.substring(line.indexOf("</td>")+5);
                String a = stripTags(between(rest, "<td", "</td>")).trim();
                String rest2 = rest.substring(rest.indexOf("</td>")+5);
                String p = stripTags(between(rest2, "<td", "</td>")).trim();
                entries.add(new Entry(t, a, p));
            } catch (Exception ex) {
                // ignore unparsable lines
            }
        }
        r.close();
        // deduplicate by path keeping newest entry: iterate from the end (newest)
        Set<String> seen = new HashSet<>();
        List<Entry> dedup = new ArrayList<>();
        for (int i = entries.size() - 1; i >= 0; i--) {
            Entry e = entries.get(i);
            String p = (e.path == null) ? "" : e.path.trim();
            if (p.length() == 0) continue;
            if (seen.contains(p)) continue;
            dedup.add(e);
            seen.add(p);
        }
        return dedup;
    }

    // Table model for the history table: checkbox, date, action, path
    private static class HistoryTableModel extends AbstractTableModel {
        private final String[] cols = {"", "Date", "Action", "File"};
        private final List<Entry> rows;
        private final List<Boolean> checks;
        HistoryTableModel(List<Entry> entries) {
            this.rows = new ArrayList<>(entries);
            this.checks = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) checks.add(Boolean.FALSE);
        }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int col) { return cols[col]; }
        @Override public Class<?> getColumnClass(int col) {
            if (col == 0) return Boolean.class;
            return String.class;
        }
        @Override public boolean isCellEditable(int row, int col) {
            if (col != 0) return false;
            // checkbox only editable if file exists
            String path = rows.get(row).path;
            if (path == null) return false;
            File f = new File(path);
            return f.exists();
        }
        @Override public Object getValueAt(int row, int col) {
            Entry e = rows.get(row);
            switch (col) {
                case 0: return checks.get(row);
                case 1: return e.timestamp;
                case 2: return e.action;
                case 3: return e.path;
            }
            return null;
        }
        @Override public void setValueAt(Object value, int row, int col) {
            if (col == 0 && value instanceof Boolean) {
                boolean v = (Boolean) value;
                if (v) {
                    // enforce only a single checked row: clear others
                    for (int i = 0; i < checks.size(); i++) {
                        if (checks.get(i)) {
                            checks.set(i, Boolean.FALSE);
                            fireTableCellUpdated(i, 0);
                        }
                    }
                    checks.set(row, Boolean.TRUE);
                    fireTableCellUpdated(row, 0);
                } else {
                    checks.set(row, Boolean.FALSE);
                    fireTableCellUpdated(row, 0);
                }
            }
        }
        public String getPathAt(int row) { return rows.get(row).path; }
        public java.util.List<String> getCheckedPaths() {
            List<String> out = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) if (checks.get(i)) out.add(rows.get(i).path);
            return out;
        }
    }

    // Renderer for the checkbox that disables it when file missing
    private static class TableCheckRenderer extends JCheckBox implements TableCellRenderer {
        public TableCheckRenderer() { setHorizontalAlignment(CENTER); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int modelRow = table.convertRowIndexToModel(row);
            HistoryTableModel m = (HistoryTableModel) table.getModel();
            String path = m.getPathAt(modelRow);
            boolean exists = (path != null) && new File(path).exists();
            boolean val = false;
            if (value instanceof Boolean) val = (Boolean) value;
            setSelected(val);
            setEnabled(exists);
            if (!exists) setForeground(Color.GRAY); else setForeground(table.getForeground());
            if (isSelected) setBackground(table.getSelectionBackground()); else setBackground(table.getBackground());
            return this;
        }
    }

    // Renderer for normal cells: gray out when file missing
    private static class RowStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int modelRow = table.convertRowIndexToModel(row);
            HistoryTableModel m = (HistoryTableModel) table.getModel();
            String path = m.getPathAt(modelRow);
            boolean exists = (path != null) && new File(path).exists();
            c.setForeground(exists ? table.getForeground() : Color.GRAY);
            return c;
        }
    }

    private static String between(String s, String a, String b) {
        int ia = s.indexOf(a);
        if (ia < 0) return "";
        int start = s.indexOf('>', ia);
        if (start < 0) start = ia + a.length(); else start++;
        int ib = s.indexOf(b, start);
        if (ib < 0) ib = s.length();
        return s.substring(start, ib);
    }

    private static String stripTags(String s) {
        return s.replaceAll("<.*?>", "");
    }

    public static void Launch() {
        try {
            HistoryViewer hv = new HistoryViewer();
            hv.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Try to open a path in the running Launcher; if it's an encrypted file
    // (endsWith Details.encryptionExtention) then prompt for password and
    // attempt to decrypt before opening the plaintext file in the editor.
    private void openPath(String path) {
        if (path == null) return;
        File file = new File(path);
        boolean isEncrypted = path.endsWith("." + Details.encryptionExtention) || path.endsWith(Details.encryptionExtention);
        if (!file.exists()) {
            javax.swing.JOptionPane.showMessageDialog(this, "File not found: " + path);
            return;
        }

        if (isEncrypted) {
            // Ask for password (simple dialog)
            //String justName = file.getName();
            // use shared PasswordPrompter
            java.util.ResourceBundle labels = java.util.ResourceBundle.getBundle("wagemaker.co.uk.lang.LabelsBundle", java.util.Locale.of("en"));
            String codeGet = wagemaker.co.uk.utility.PasswordPrompter.askForPassword(this, "SmallTextPad " + "Decrypt", labels, false);
            if (codeGet == null || codeGet.length() == 0) return;
            // try decrypt loop until success or cancel
            boolean ok = false;
            while (!ok) {
                try {
                    ok = STPFileCrypter.main(path, codeGet, "decrypt");
                } catch (Exception ex) {
                    ok = false;
                }
                if (!ok) {
                    codeGet = wagemaker.co.uk.utility.PasswordPrompter.askForPassword(this, "SmallTextPad " + "Decrypt (retry)", labels, false);
                    if (codeGet == null || codeGet.length() == 0) break;
                }
            }
            if (!ok) return;
            // After successful decrypt, the decrypted file is expected to be
            // the encrypted file path without the .sstp extension
            String plainPath = path;
            if (plainPath.indexOf('.') > 0) plainPath = plainPath.substring(0, plainPath.lastIndexOf('.'));
            // record history for decrypted
            try { logHistory.main(plainPath + "." + Details.encryptionExtention, "decrypted"); } catch (Exception ex) {}
            // Attempt to open the decrypted file in the Launcher instance
            boolean opened = Launcher.openFileFromPath(plainPath);
            if (!opened) javax.swing.JOptionPane.showMessageDialog(this, "Failed to open decrypted file: " + plainPath);
            else this.dispose();
            return;
        }

        // not encrypted: open directly
        boolean opened = Launcher.openFileFromPath(path);
        if (!opened) javax.swing.JOptionPane.showMessageDialog(this, "Failed to open: " + path);
        else this.dispose();
    }
}
