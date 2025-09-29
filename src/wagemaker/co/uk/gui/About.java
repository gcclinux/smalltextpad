package wagemaker.co.uk.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
 

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
import javax.swing.UIManager;

import wagemaker.co.uk.utility.Details;
import wagemaker.co.uk.utility.FontTheme;
import wagemaker.co.uk.main.Launcher;

/**
 * Modernized About dialog for SmallTextPad.
 * Uses layout managers, pack(), and a rectangular window.
 */
public final class About {

	private About() { /* utility class */ }

	public static void Main(String[] args) {
		// Small, modern UI defaults
		UIManager.put("Panel.background", Details.ORANGE);
		UIManager.put("Label.font", FontTheme.size15i);

		JDialog dlg = new JDialog();
		dlg.setTitle("About " + Details.Title);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.setResizable(false);

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout(12, 12));
		content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

	// Top: icon + title (centered)
	JPanel top = new JPanel();
	top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
	top.setOpaque(false);
	ImageIcon icon = new ImageIcon(Launcher.class.getResource("/gcclinux.png"));
	JLabel iconLabel = new JLabel(icon);
	iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	top.add(iconLabel);

	JLabel title = new JLabel(Details.Title, JLabel.CENTER);
	title.setFont(FontTheme.size20b);
	title.setForeground(Details.WHITE);
	title.setAlignmentX(Component.CENTER_ALIGNMENT);
	top.add(title);

	JLabel version = new JLabel("Version: " + Details.Version, JLabel.CENTER);
	version.setFont(FontTheme.size15i);
	version.setForeground(Details.WHITE);
	version.setAlignmentX(Component.CENTER_ALIGNMENT);
	top.add(version);

	content.add(top, BorderLayout.NORTH);

		// Center: info
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.setOpaque(false);
		center.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		JLabel dev = new JLabel("Developer: " + Details.Developer, JLabel.CENTER);
		dev.setFont(FontTheme.size15i);
		dev.setForeground(Details.WHITE);
		dev.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		center.add(dev);

		center.add(Box.createRigidArea(new Dimension(1, 6)));

		JLabel javaLabel = new JLabel("Java: " + System.getProperty("java.version"), JLabel.CENTER);
		javaLabel.setFont(FontTheme.size15i);
		javaLabel.setForeground(Details.WHITE);
		javaLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		center.add(javaLabel);

		center.add(Box.createRigidArea(new Dimension(1, 8)));

	// Description paragraph (HTML-wrapped for automatic wrapping)
	String desc = "<html><body style='width:420px; text-align:center;'><br>" +
		"SmallTextPad is a simple, lightweight Java text editor and is 100% free. " +
		"It requires a minimum of JRE 21 on your system unless you run it inside a container (snap/flatpak/other). " +
		"In addition to standard editing features, SmallTextPad supports encrypting and decrypting secure text files, " +
		"so you can safely store sensitive information alongside regular notes. " +
		"The project includes many useful features and continues to be developed in spare time.<br>" +
		"</body></html>";
	JLabel description = new JLabel(desc);
	description.setFont(FontTheme.size15i);
	description.setForeground(Details.WHITE);
	description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	center.add(description);

	center.add(Box.createRigidArea(new Dimension(1, 8)));

		JLabel licensed = new JLabel("Distributed under the MIT License", JLabel.CENTER);
		licensed.setFont(FontTheme.size15i);
		licensed.setForeground(Details.WHITE);
		licensed.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		center.add(licensed);

		content.add(center, BorderLayout.CENTER);

		// Bottom: close button
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
		bottom.setOpaque(false);
		JButton close = new JButton("Close");
		close.setFont(FontTheme.size15b);
		close.addActionListener(ev -> dlg.dispose());
		bottom.add(close);
		content.add(bottom, BorderLayout.SOUTH);

		dlg.setContentPane(content);
	dlg.pack();
	dlg.setSize(Math.max(dlg.getWidth(), 480), dlg.getHeight());
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
	}

}