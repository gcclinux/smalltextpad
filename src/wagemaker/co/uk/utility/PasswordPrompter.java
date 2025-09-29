package wagemaker.co.uk.utility;

import java.awt.IllegalComponentStateException;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPanel;

/**
 * Shared password prompt used by Launcher and HistoryViewer.
 */
public final class PasswordPrompter {

    private PasswordPrompter() {}

    public static String askForPassword(Component parent, String title, ResourceBundle labels, boolean debug) {
        while (true) {
            JPasswordField pf1 = new JPasswordField(20);
            JPasswordField pf2 = new JPasswordField(20);
            JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
            panel.add(new javax.swing.JLabel(labels.getString("label031")));
            panel.add(pf1);
            panel.add(new javax.swing.JLabel(labels.getString("label032")));
            panel.add(pf2);

            JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog = pane.createDialog(parent, title);
            dialog.pack();

            if (debug) {
                if (parent != null) {
                    try {
                        java.awt.Point op = parent.getLocationOnScreen();
                        System.out.println("[DEBUG] Parent on-screen location: x=" + op.x + " y=" + op.y);
                    } catch (IllegalComponentStateException ice) {
                        System.out.println("[DEBUG] Parent on-screen location: not available (parent not showing)");
                    }
                }
            }

            dialog.setModal(true);
            final boolean prevAlwaysOnTop = dialog.isAlwaysOnTop();
            try { dialog.setAlwaysOnTop(true); } catch (Exception e) {}

            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowOpened(java.awt.event.WindowEvent we) {
                    try {
                        if (parent != null && parent.isShowing()) {
                            java.awt.Point p = parent.getLocationOnScreen();
                            int px = p.x;
                            int py = p.y;
                            int pw = parent.getWidth();
                            int ph = parent.getHeight();
                            int dx = dialog.getWidth();
                            int dy = dialog.getHeight();
                            int cx = px + (pw - dx) / 2;
                            int cy = py + (ph - dy) / 2;
                            dialog.setLocation(cx, cy);
                        } else {
                            dialog.setLocationRelativeTo(parent);
                        }
                    } catch (java.awt.IllegalComponentStateException iced) {
                        dialog.setLocationRelativeTo(parent);
                    }

                    try {
                        javax.swing.Timer t = new javax.swing.Timer(50, new java.awt.event.ActionListener() {
                            @Override
                            public void actionPerformed(java.awt.event.ActionEvent e) {
                                try {
                                    if (parent != null && parent.isShowing()) {
                                        java.awt.Point p2 = parent.getLocationOnScreen();
                                        int px2 = p2.x;
                                        int py2 = p2.y;
                                        int pw2 = parent.getWidth();
                                        int ph2 = parent.getHeight();
                                        int dx2 = dialog.getWidth();
                                        int dy2 = dialog.getHeight();
                                        int cx2 = px2 + (pw2 - dx2) / 2;
                                        int cy2 = py2 + (ph2 - dy2) / 2;
                                        dialog.setLocation(cx2, cy2);
                                    } else {
                                        dialog.setLocationRelativeTo(parent);
                                    }
                                } catch (Exception ex) {}
                                ((javax.swing.Timer) e.getSource()).stop();
                            }
                        });
                        t.setRepeats(false);
                        t.start();
                    } catch (Exception ex) {}

                    try { dialog.toFront(); dialog.requestFocus(); } catch (Exception ex) {}
                }
            });

            dialog.setVisible(true);

            try { dialog.setAlwaysOnTop(prevAlwaysOnTop); } catch (Exception e) {}

            Object selectedValue = pane.getValue();
            if (selectedValue == null) return null;
            int result;
            if (selectedValue instanceof Integer) result = ((Integer) selectedValue).intValue(); else result = JOptionPane.CLOSED_OPTION;
            if (result != JOptionPane.OK_OPTION) return null;
            String p1 = new String(pf1.getPassword());
            String p2 = new String(pf2.getPassword());
            if (p1.length() < 7) {
                JOptionPane.showMessageDialog(parent, labels.getString("label032"), title, JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(parent, labels.getString("label035"), title, JOptionPane.ERROR_MESSAGE);
                continue;
            }
            return p1;
        }
    }
}
