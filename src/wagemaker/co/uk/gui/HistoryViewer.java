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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import wagemaker.co.uk.utility.Details;
import wagemaker.co.uk.main.Launcher;

public class HistoryViewer extends JDialog {

    private static final long serialVersionUID = 1L;

    public static class Entry {
        public final String timestamp;
        public final String action;
        public final String path;
        public Entry(String t, String a, String p) { timestamp=t; action=a; path=p; }
        public String toString() { return timestamp+" — "+action+" — "+path; }
    }

    private DefaultListModel<Entry> model = new DefaultListModel<>();
    private JList<Entry> list = new JList<>(model);

    public HistoryViewer() throws IOException {
        super((javax.swing.JFrame) null, "SmallTextPad History", true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 400));

        loadEntries();

        JScrollPane sp = new JScrollPane(list);
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton openBtn = new JButton("Open Selected");
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entry sel = list.getSelectedValue();
                if (sel == null) return;
                // attempt to open in launcher
                final String p = sel.path;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        boolean ok = Launcher.openFileFromPath(p);
                        if (!ok) {
                            javax.swing.JOptionPane.showMessageDialog(HistoryViewer.this, "Failed to open: " + p);
                        } else {
                            HistoryViewer.this.dispose();
                        }
                    }
                });
            }
        });
        bottom.add(openBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { HistoryViewer.this.dispose(); } });
        bottom.add(closeBtn);

        add(bottom, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void loadEntries() throws IOException {
        File f = Details.historyFile();
        if (!f.exists()) return;
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
        // populate model (reverse chronological if file already in that order)
        for (int i = entries.size()-1; i >= 0; i--) model.addElement(entries.get(i));
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
}
