package wagemaker.co.uk.main;

/******************************************************************************************************
 * @author by Ricardo Wagemaker
 * @title SmallTextPad
 * @version 1.5.0
 * @since   2010 - 2025
 * @License MIT
 ******************************************************************************************************/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import wagemaker.co.uk.display.LastPath;
import wagemaker.co.uk.display.Position;
import wagemaker.co.uk.display.Size;
import wagemaker.co.uk.gui.About;
import wagemaker.co.uk.gui.DisplayReport;
import wagemaker.co.uk.utility.Details;
import wagemaker.co.uk.utility.FontTheme;
import wagemaker.co.uk.utility.LaunchBrowser;
import wagemaker.co.uk.utility.STPFileCrypter;
import wagemaker.co.uk.utility.logHistory;
import wagemaker.co.uk.lang.LangS;
import java.util.ArrayList;
import java.util.List;

	public class Launcher extends JFrame {
	
		private static final long serialVersionUID = 1L;
		
		Locale locale = Locale.of(LangS.getLanguage());
		ResourceBundle labels = ResourceBundle.getBundle("wagemaker.co.uk.lang.LabelsBundle", locale);
	
		private JTextComponent textComp;
		final MessageFormat header = new MessageFormat(Details.Header);
		 
		static String Title = Details.Title;
		static String FileTitle = null;
		static File FilePathTrue = null;
		static String FullPathName = null;
		JFrame frameEncryption = new JFrame();
		
	public static void main(String[] args) {
		
		if(args.length == 0)
	    {
			final Launcher editor = new Launcher(null);
			editor.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e)
				{
					Position.setPosX(editor.getX());
					Position.setPosY(editor.getY());
					Size.setFrameHeight(editor.getHeight());
					Size.setFrameWidth(editor.getWidth());
					e.getWindow().dispose();
					System.exit(0);
					
				}
			});
			editor.setVisible(true);
	    } else {
	    	final Launcher editor = new Launcher(args[0]);
			editor.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e)
				{
					Position.setPosX(editor.getX());
					Position.setPosY(editor.getY());
					Size.setFrameHeight(editor.getHeight());
					Size.setFrameWidth(editor.getWidth());
					e.getWindow().dispose();
					System.exit(0);
				}
			});
	    	editor.setVisible(true);
	    }
		
	
	}
	
	// Create an editor.
	@SuppressWarnings({ "static-access" })
	public Launcher(String args) {
		
	 super(Title);
	// Use the main window as the parent for encryption dialogs so they center properly
	frameEncryption = this;
		// Check History File	
		File historyCheck;
		try {
			historyCheck = Details.historyFile();
			if (!historyCheck.exists()){
				historyCheck.createNewFile();
				FileWriter resultfilestr = new FileWriter(Details.historyFile(), true);
				BufferedWriter outHist = new BufferedWriter(resultfilestr);
				outHist.write("<center><b><h1>\"SmallTextPad History\"</h1></b>");
				outHist.newLine();
				outHist.write("<table width=\"1010\" border cellpadding=\"2\">");
				outHist.newLine();
				outHist.close();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	 
	 final ImageIcon printIcon = new ImageIcon(getClass().getResource("/printer.png"));
	 final ImageIcon ImageIcon = new ImageIcon(getClass().getResource("/gcclinux.png"));
	 final ImageIcon openIcon = new ImageIcon(getClass().getResource("/open.png"));
	 final ImageIcon saveIcon = new ImageIcon(getClass().getResource("/save.png"));
	 final JPopupMenu popupMenu = new JPopupMenu();
	 textComp = createTextComponent();
	 //textComp.setFont(new Font("SERIF", Font.TRUETYPE_FONT, 14));
	 undoRedo();
	 makeActionsPretty();
	 Container content = getContentPane();
	 
	 // Creating Popup menu #
	
	 JMenuItem printItem = new JMenuItem(labels.getString("label016"), printIcon);
	 JMenuItem openItem = new JMenuItem(labels.getString("label009"), openIcon);
	 JMenuItem saveItem = new JMenuItem(labels.getString("label010"), saveIcon);
	 
	 printItem.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 openItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 
	 Action cut = textComp.getActionMap().get(DefaultEditorKit.cutAction);
	 cut.putValue(Action.NAME, labels.getString("label017"));
	 cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
	 
	 Action word = textComp.getActionMap().get(DefaultEditorKit.selectWordAction);
	 word.putValue(Action.NAME, labels.getString("label020"));
	 word.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
	 
	 Action copy = textComp.getActionMap().get(DefaultEditorKit.copyAction);
	 copy.putValue(Action.NAME, labels.getString("label019"));
	 copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
	 
	 Action paste = textComp.getActionMap().get(DefaultEditorKit.pasteAction);
	 paste.putValue(Action.NAME, labels.getString("label018"));
	 paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
	 
	//Adding Popup menu
	 popupMenu.add(openItem);
	 popupMenu.add(saveItem);
	 popupMenu.add(cut);
	 popupMenu.add(copy);
	 popupMenu.add(paste);
	 popupMenu.add(printItem);
	 
	
	 textComp.setComponentPopupMenu(popupMenu);
	 
	 content.add(textComp, BorderLayout.CENTER);
	 content.add(createToolBar(), BorderLayout.NORTH);
	 content.add(new JScrollPane(textComp));
	 
	 setJMenuBar(createMenuBar());
	 setSize(Details.SizeW, Details.SizeH);
	 this.setMinimumSize(getSize());
	 
	 // Right click menu  //
	 openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				FileFilter filter2 = new FileNameExtensionFilter("Text File (TXT)", "txt", "TXT");
				FileFilter filterSecure = new FileNameExtensionFilter("SmallTextPad (SSTP)", Details.encryptionExtention);
				
				   JFileChooser chooser = new JFileChooser(LastPath.getLastPath());
				   chooser.setDialogTitle(labels.getString("label009"));
				   chooser.setAcceptAllFileFilterUsed(false);
				   chooser.addChoosableFileFilter(filter2);
				   chooser.addChoosableFileFilter(filterSecure);
				   
				   if (chooser.showOpenDialog(Launcher.this) != 
				       JFileChooser.APPROVE_OPTION)
					   return;
				   		File file = chooser.getSelectedFile();
						   String filePath = file.getAbsolutePath();
				   if (filePath == null)
				     return;
	
				   FileReader reader = null;
				   String fileName = file.toString();
				   try {
					   if(fileName.endsWith(".txt") || fileName.endsWith(".java")) {
						   reader = new FileReader(file);
						   textComp.read(reader, null);
						   String filename=chooser.getSelectedFile().getName();
						   setTitle(Title+" ~ " + filename);
						   
						   filename=chooser.getSelectedFile().getName();
						   FileTitle = filename;
						   FullPathName = filePath;
						   FilePathTrue = file;
						   
						   logHistory.main(filePath, "open");
						   String lastPath = chooser.getCurrentDirectory().toString();
						   LastPath.setLastPath(lastPath);				   
						  
						   
					   } else if (fileName.endsWith(Details.encryptionExtention)){
						   
						   //System.out.println("encrypted file");
						   String justName = file.getName();
						      
						   String codeGet = new String();
						   
						   while (codeGet.length() <= 7) {
							   codeGet = JOptionPane.showInputDialog(
								        frameEncryption, 
								        labels.getString("label031")+":\n"+labels.getString("label001")+": "+justName, 
								        "SmallTextPad  "+labels.getString("label033"), 
								        JOptionPane.OK_CANCEL_OPTION
								    );	
							   if ((codeGet == null) || (codeGet.length() == 0)) {
								   break;
							   }
						   }
						   
						   if ((codeGet != null) && (codeGet.length() > 0)) {
							   while (STPFileCrypter.main(fileName, codeGet, "decrypt") == false) {
								   codeGet = JOptionPane.showInputDialog(
									        frameEncryption, 
									        labels.getString("label035")+":\n"+labels.getString("label001")+": "+justName, 
									        "SmallTextPad  "+labels.getString("label033"), 
									        JOptionPane.OK_CANCEL_OPTION
									    );	
								   if ((codeGet == null) || (codeGet.length() == 0)) {
									   break;
								   }
							   }
		
						   }  
						   
	
						   if(filePath.endsWith("."+Details.encryptionExtention)) {
							   if (filePath.indexOf(".") > 0)
									filePath = filePath.substring(0, filePath.lastIndexOf("."));
						   }
						   
						   if ((codeGet != null) && (codeGet.length() > 0)) {
								File varDecryptedFile = new File(filePath);
								String decryptFile = varDecryptedFile.toString();
								
									   if(decryptFile.endsWith(".txt")) {
										   try {
											reader = new FileReader(varDecryptedFile);
										} catch (FileNotFoundException e) {
											e.printStackTrace();
										}
										   try {
											textComp.read(reader, null);
										} catch (IOException e) {
											e.printStackTrace();
										}
	
										   setTitle(Title+" ~ " + varDecryptedFile.getName());
										   FileTitle = varDecryptedFile.getName();
										   FullPathName = filePath;
										   FilePathTrue = file;
									   } 	
									   
									   logHistory.main(filePath+"."+Details.encryptionExtention, "decrypted");
						   }
					   
					   } else {
						   JOptionPane.showMessageDialog(Launcher.this,
								     "Unknown File Format", "ERROR", JOptionPane.ERROR_MESSAGE);	   
					   }
				   }
				   catch (IOException ex) {
				     JOptionPane.showMessageDialog(Launcher.this,
				     "Unknown File Format", "ERROR", JOptionPane.ERROR_MESSAGE);
				   }
				   finally {
				     if (reader != null) {
				       try {
				         reader.close();
				       } catch (IOException x) {}
				     }
				   }
					undoRedo();
			}
	
	 });
	 
	 printItem.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) {
				try {
					textComp.print(null, null, true, null, null, true);
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
	     }
	 });
	 
	 saveItem.addActionListener(new ActionListener() {
	     @SuppressWarnings("unused")
		public void actionPerformed(ActionEvent e) {
	
				FileFilter filter = new FileNameExtensionFilter("SmallTextPad (TXT)", "txt", "TXT");
				JFileChooser chooser = new JFileChooser(LastPath.getLastPath());
				chooser.setDialogTitle(labels.getString("label010"));
				chooser.addChoosableFileFilter(filter);
				
				if (FileTitle == null) {
				   if (chooser.showSaveDialog(Launcher.this) != JFileChooser.APPROVE_OPTION) return;
				   File file = chooser.getSelectedFile();
				   String filePath = file.getAbsolutePath();
				   
				   if(!filePath.endsWith(".txt")) {
					   file = new File(filePath + ".txt");
				   }
				   if (file == null)
				     return;
	
				   FileWriter writer = null;
				   try {
				     writer = new FileWriter(file);
				     textComp.write(writer);
				     
					   String filename=chooser.getSelectedFile().getName();
					   String lastPath = chooser.getCurrentDirectory().toString();
					   LastPath.setLastPath(lastPath);
					   
					   if(!filePath.endsWith(".txt")) {
						   setTitle(Title+" ~ " + filename+ ".txt");
						   filename = filename+".txt";
						   FileTitle = filename;
					   } else {
						   setTitle(Title+" ~ " + filename);
						   FileTitle = filename;
					   }	   
				   }
				   catch (IOException ex) {
				     JOptionPane.showMessageDialog(Launcher.this,
				    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
				   }
				   finally {
				     if (writer != null) {
				       try {
				         writer.close();
				       } catch (IOException x) {}
				     }
				   }
				   
				   String filename=chooser.getSelectedFile().getName();
				   FullPathName = filePath;
				   FilePathTrue = file;
				   String lastPath = chooser.getCurrentDirectory().toString();
				   LastPath.setLastPath(lastPath);
				   
				   if(!filePath.endsWith(".txt"))
					   filePath = filePath + ".txt";
				   
				   logHistory.main(filePath, labels.getString("label027"));
	
				 } else {  // Just save file is already exist
					 
					   File file = FilePathTrue;
					   if (file.toString().endsWith(Details.encryptionExtention) && file.toString().indexOf(".") > 0) {
								file = new File(file.toString().substring(0, file.toString().lastIndexOf(".")));
						}	   
					   
					   if (file == null)
					     return;
					   FileWriter writer = null;
					   try {
					     writer = new FileWriter(file);
					     textComp.write(writer);   
					   }
					   catch (IOException ex) {
					     JOptionPane.showMessageDialog(Launcher.this,
					    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
					   }
					   finally {
					     if (writer != null) {
					       try {
					         writer.close();
					       } catch (IOException x) {}
					     }
					   }
					   String lastPath = chooser.getCurrentDirectory().toString();
					   LastPath.setLastPath(lastPath);				   
					   logHistory.main(file.toString(), labels.getString("label027"));
				 }
	     }
	 });
	 
	 
	 // End popup menu action
	 
	 // Make all JFrame utilise the custom UIManager
	 
		UIManager UI=new UIManager();
		UI.put("OptionPane.buttonOrientation", 0);		
		UI.put("OptionPane.buttonFont", FontTheme.size15b);
		UI.put("OptionPane.messageFont", FontTheme.size15i);
		UI.put("OptionPane.messageForeground",Details.BLACK);
		UI.put("Panel.background",  Details.ORANGE);
		UI.put("OptionPane.background", Details.ORANGE);
	 
		this.setSize(Size.getFrameWidth(), Size.getFrameHeight());
		 this.setLocation(Position.getPosX(), Position.getPosY());
	 
	 // Adding Application ICON
		 Image Logo = ImageIcon.getImage();
		 this.setIconImage(Logo);
	 
	 
		// CHECK VERSION
		
		@SuppressWarnings("unused")
		boolean connectivity;
		 
		   try
		   {
			 URL version = new URI(Details.versionFile).toURL();
		     URLConnection conn = version.openConnection();
		     conn.setConnectTimeout(5000);
		     conn.connect();
		     connectivity = true;
		     
		        BufferedReader in = new BufferedReader(new InputStreamReader(version.openStream()));
		        
		        String newVersion = in.readLine();
		        String currentVersion = Details.Version;
	
		        String currentValue = currentVersion.replaceAll("\\.", "");
		        String newValue = newVersion.replaceAll("\\.", "");
		        
		        int x = Integer.parseInt(currentValue);
		        int y = Integer.parseInt(newValue);
	
		        if (x < y){
		        	int selectedOption = JOptionPane.showConfirmDialog(null, 
		        			" EN - Download new version? \n", "Update Available - " +newVersion, JOptionPane.YES_NO_OPTION);
		        			if (selectedOption == JOptionPane.YES_OPTION) {
		        				try 
		        		        {
		        					LaunchBrowser.launcher("http://www.wagemaker.co.uk/?page_id=74");
		        		        }           
		        		        catch (Exception e) {}
		        			} 	
		        }
		   }
		   catch (Exception e)
		   {
			   connectivity = false;
		   }
		      
		   if(args != null) {
		   		File file = new File(args);
				String filePath = file.getAbsolutePath();
		   
				if (filePath == null)
					return;
	
				FileReader reader = null;
				try {
				   reader = new FileReader(file);
				   textComp.read(reader, null);
				   String filename = file.getName();
				   setTitle(Title+" ~ " + filename);
			   } catch (IOException ex) {
			     JOptionPane.showMessageDialog(Launcher.this,
			     "File Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
			   }
			   finally {
			     if (reader != null) {
			       try {
			         reader.close();
			       } catch (IOException x) {}
			     }
			   }
		   }
		
		// END VERSION
	}
	
	// Create the JTextComponent subclass.
	protected JTextComponent createTextComponent() {
	 JTextArea EditingArea = new JTextArea();
	 EditingArea.setLineWrap(true);
	 EditingArea.setWrapStyleWord(true);
	 return EditingArea;
	}
	
	protected void undoRedo() {
		final UndoManager undoManager = new UndoManager();
		
		 textComp.getDocument().addUndoableEditListener(undoManager);
		 InputMap im = textComp.getInputMap(JComponent.WHEN_FOCUSED);
		 ActionMap am = textComp.getActionMap();
		 im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Undo");
		 im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Redo");
	
		 am.put("Undo", new AbstractAction() {
			private static final long serialVersionUID = 1L;
	
				@Override
			    public void actionPerformed(ActionEvent e) {
			        try {
			            if (undoManager.canUndo()) {
			                undoManager.undo();
			            }
			        } catch (CannotUndoException exp) {
			            exp.printStackTrace();
			        }
			    }
			});
			am.put("Redo", new AbstractAction() {
				private static final long serialVersionUID = 1L;
	
				@Override
			    public void actionPerformed(ActionEvent e) {
			        try {
			            if (undoManager.canRedo()) {
			                undoManager.redo();
			            }
			        } catch (CannotUndoException exp) {
			            exp.printStackTrace();
			        }
			    }
			});
		
	}
	
	// Add icons and friendly names to actions we care about.
	protected void makeActionsPretty() {
	
	 Action a;
	 a = textComp.getActionMap().get(DefaultEditorKit.cutAction);
	 a.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/cut.png")));
	 a.putValue(Action.NAME, labels.getString("label017"));
	 a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
	
	 a = textComp.getActionMap().get(DefaultEditorKit.copyAction);
	 a.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/copy.png")));
	 a.putValue(Action.NAME, labels.getString("label019"));
	 a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
	
	 a = textComp.getActionMap().get(DefaultEditorKit.pasteAction);
	 a.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/paste.png")));
	 a.putValue(Action.NAME, labels.getString("label018"));
	 a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control P"));
	
	 a = textComp.getActionMap().get(DefaultEditorKit.selectAllAction);
	 a.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/all.png")));
	 a.putValue(Action.NAME, labels.getString("label021"));
	 a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
	 
	 a = textComp.getActionMap().get(DefaultEditorKit.selectWordAction);
	 a.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/text.png")));
	 a.putValue(Action.NAME, labels.getString("label022"));
	 a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W")); 
	 
	 a = textComp.getActionMap().get(DefaultEditorKit.backwardAction);
	 a.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/undo.png")));
	 a.putValue(Action.NAME, labels.getString("label023"));
	 a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U")); //backwardAction
	
	}
	
	// Create a simple JToolBar with some buttons.
	protected JToolBar createToolBar() {
		 JToolBar bar = new JToolBar();
			final ImageIcon openIcon = new ImageIcon(getClass().getResource("/open.png"));
			Action OpenAction = new AbstractAction(labels.getString("label009"), openIcon) {
				private static final long serialVersionUID = 4751705064620194963L;
				public void actionPerformed(ActionEvent evt) {
	   
					FileFilter filter2 = new FileNameExtensionFilter("Text File (TXT)", "txt", "TXT");
					FileFilter filterSecure = new FileNameExtensionFilter("SmallTextPad (SSTP)", Details.encryptionExtention);
					
					   JFileChooser chooser = new JFileChooser(LastPath.getLastPath());
					   chooser.setDialogTitle(labels.getString("label009"));
					   chooser.setAcceptAllFileFilterUsed(false);
					   chooser.addChoosableFileFilter(filter2);
					   chooser.addChoosableFileFilter(filterSecure);
					   
					   if (chooser.showOpenDialog(Launcher.this) !=
					       JFileChooser.APPROVE_OPTION)
						   return;
					   		File file = chooser.getSelectedFile();
							   String filePath = file.getAbsolutePath();
					   if (filePath == null)
					     return;
	
					   FileReader reader = null;
					   String fileName = file+"";
					   try {
						   if(fileName.endsWith(".txt") || fileName.endsWith(".java")) {
							   reader = new FileReader(file);
							   textComp.read(reader, null);
							   String filename=chooser.getSelectedFile().getName();
							   setTitle(Title+" ~ " + filename);
							   
							   filename=chooser.getSelectedFile().getName();
							   FileTitle = filename;
							   FullPathName = filePath;
							   FilePathTrue = file;
							   
							   logHistory.main(filePath, "opened");
							   String lastPath = chooser.getCurrentDirectory().toString();
							   LastPath.setLastPath(lastPath);				   
							   
						   } else if (fileName.endsWith(Details.encryptionExtention)){
							   
							   String justName = file.getName();
							   String codeGet = new String();
							   
							   while (codeGet.length() <= 7) {
								   codeGet = JOptionPane.showInputDialog(
									        frameEncryption, 
									        labels.getString("label031")+":\n"+labels.getString("label001")+": "+justName, 
									        "SmallTextPad  "+labels.getString("label033"), 
									        JOptionPane.OK_CANCEL_OPTION
									    );	
								   if ((codeGet == null) || (codeGet.length() == 0)) {
									   break;
								   }
							   }
							   
							   if ((codeGet != null) && (codeGet.length() > 0)) {
								   while (STPFileCrypter.main(fileName, codeGet, "decrypt") == false) {
									   codeGet = JOptionPane.showInputDialog(
										        frameEncryption, 
										        labels.getString("label035")+":\n"+labels.getString("label001")+": "+justName, 
										        "SmallTextPad  "+labels.getString("label033"), 
										        JOptionPane.OK_CANCEL_OPTION
										    );	
									   if ((codeGet == null) || (codeGet.length() == 0)) {
										   break;
									   }
								   }
			
							   }
	
							   if(filePath.endsWith("."+Details.encryptionExtention)) {
								   if (filePath.indexOf(".") > 0)
										filePath = filePath.substring(0, filePath.lastIndexOf("."));
							   }
							   
							   if ((codeGet != null) && (codeGet.length() > 0)) {
									File varDecryptedFile = new File(filePath);
									String decryptFile = varDecryptedFile.toString();
									
										   if(decryptFile.endsWith(".txt")) {
											   try {
												reader = new FileReader(varDecryptedFile);
											} catch (FileNotFoundException e) {
												e.printStackTrace();
											}
											   try {
												textComp.read(reader, null);
											} catch (IOException e) {
												e.printStackTrace();
											}
	
											   setTitle(Title+" ~ " + varDecryptedFile.getName());
											   FileTitle = varDecryptedFile.getName();
											   FullPathName = filePath;
											   FilePathTrue = file;
										   } 	
							   }
							   
							   logHistory.main(filePath+"."+Details.encryptionExtention, "decrypted");
							   String lastPath = chooser.getCurrentDirectory().toString();
							   LastPath.setLastPath(lastPath);				   
						   
						   } else {
							   JOptionPane.showMessageDialog(Launcher.this,
									     "Unknown File Format", "ERROR", JOptionPane.ERROR_MESSAGE);	   
						   }
					   }
					   catch (IOException ex) {
					     JOptionPane.showMessageDialog(Launcher.this,
					     "Unknown File Format", "ERROR", JOptionPane.ERROR_MESSAGE);
					   }
					   finally {
					     if (reader != null) {
					       try {
					         reader.close();
					       } catch (IOException x) {}
					     }
					   }
						undoRedo();
		   }
	 };
	 
	 
	 JButton openButton = new JButton(OpenAction);
	 openButton.setVerticalTextPosition(SwingConstants.BOTTOM);
	 openButton.setHorizontalTextPosition(SwingConstants.CENTER);
	 openButton.setText(labels.getString("label009"));
	 bar.add(openButton);
	
	 // END OPEN ICON
	 
	 // SAVE ICON
	 final ImageIcon saveIcon = new ImageIcon(getClass().getResource("/save.png"));
	 
		Action SaveAction = new AbstractAction(labels.getString("label010"), saveIcon) {
	
			private static final long serialVersionUID = 1L;
	
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent ev) {
				FileFilter filter = new FileNameExtensionFilter("SmallTextPad (TXT)", "txt", "TXT");
				JFileChooser chooser = new JFileChooser(LastPath.getLastPath());
				chooser.setDialogTitle(labels.getString("label010"));
				chooser.addChoosableFileFilter(filter);
				
				if (FileTitle == null) {
	
				   if (chooser.showSaveDialog(Launcher.this) != JFileChooser.APPROVE_OPTION) return;
				   File file = chooser.getSelectedFile();
				   String filePath = file.getAbsolutePath();
				   
				   if(!filePath.endsWith(".txt")) {
					   file = new File(filePath + ".txt");
				   }
				   if (file == null)
				     return;
	
				   FileWriter writer = null;
				   try {
				     writer = new FileWriter(file);
				     textComp.write(writer);
				     
					   String filename=chooser.getSelectedFile().getName();
					   String lastPath = chooser.getCurrentDirectory().toString();
					   LastPath.setLastPath(lastPath);
					   
					   if(!filePath.endsWith(".txt")) {
						   setTitle(Title+" ~ " + filename+ ".txt");
						   filename = filename+".txt";
						   FileTitle = filename;
					   } else {
						   setTitle(Title+" ~ " + filename);
						   FileTitle = filename;
					   }
					   
				   }
				   catch (IOException ex) {
				     JOptionPane.showMessageDialog(Launcher.this,
				    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
				   }
				   finally {
				     if (writer != null) {
				       try {
				         writer.close();
				       } catch (IOException x) {}
				     }
				   }
				   
				   String filename=chooser.getSelectedFile().getName();
				   FullPathName = filePath;
				   FilePathTrue = file;
				   String lastPath = chooser.getCurrentDirectory().toString();
				   LastPath.setLastPath(lastPath);
				   
				   if(!filePath.endsWith(".txt"))
					   filePath = filePath + ".txt";
				   
				   logHistory.main(filePath, labels.getString("label027"));			   
				   
				 } else {  // Just save file is already exist
					 	 
					   File file = FilePathTrue;
					   if (file.toString().endsWith(Details.encryptionExtention) && file.toString().indexOf(".") > 0) {
								file = new File(file.toString().substring(0, file.toString().lastIndexOf(".")));
						}	 
					   
					   if (file == null)
					     return;
	
					   FileWriter writer = null;
					   try {
					     writer = new FileWriter(file);
					     textComp.write(writer);   
					   }
					   catch (IOException ex) {
					     JOptionPane.showMessageDialog(Launcher.this,
					    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
					   }
					   finally {
					     if (writer != null) {
					       try {
					         writer.close();
					       } catch (IOException x) {}
					     }
					   }
					   
					   String lastPath = chooser.getCurrentDirectory().toString();
					   LastPath.setLastPath(lastPath);				   
					   logHistory.main(file.toString(), labels.getString("label027"));
				 }
			}  
		};
	 
		JButton saveButton = new JButton(SaveAction);
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setText(labels.getString("label010"));
		bar.add(saveButton);
		 
		// END SAVE ICON
		
		//NEW ICON
		
		final ImageIcon newIcon = new ImageIcon(getClass().getResource("/new.png"));
		
		Action NewAction = new AbstractAction(labels.getString("label012"), newIcon) {
	
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent ev) {
				
				textComp.setText(null);
				   setTitle(Title);
				   FileTitle = null;	
				 }
				 
		};
	 
		JButton newButton = new JButton(NewAction);
		newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		newButton.setHorizontalTextPosition(SwingConstants.CENTER);
		newButton.setText(labels.getString("label012"));
		bar.add(newButton);
		 
		// END NEW ICON
		
		bar.addSeparator();
	
	 // Add cut/copy/paste buttons.
	 bar.add(textComp.getActionMap().get(DefaultEditorKit.cutAction)).setText(labels.getString("label017"));
	 bar.add(textComp.getActionMap().get(DefaultEditorKit.copyAction)).setText(labels.getString("label019"));
	 bar.add(textComp.getActionMap().get(DefaultEditorKit.pasteAction)).setText(labels.getString("label018"));
	 
		// PRINTING ICON
		
	 final ImageIcon printIcon = new ImageIcon(getClass().getResource("/printer.png"));
		Action printAction = new AbstractAction(labels.getString("label016"), printIcon) {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent ev) {		
				try {
					textComp.print(null, null, true, null, null, true);
				} catch (PrinterException e) {
					e.printStackTrace();
				}
				
				 }	 
		};
	
		JButton printButton = new JButton(printAction);
		printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		printButton.setHorizontalTextPosition(SwingConstants.CENTER);
		printButton.setText(labels.getString("label016"));
		bar.add(printButton);
	
		// END PRINTING
		
		bar.addSeparator();
		
		//ENCRYPT BUTTON //
		
		final ImageIcon encryptIcon = new ImageIcon(getClass().getResource("/encrypt32.png"));
	
		Action encryptAction = new AbstractAction("", encryptIcon) {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent ev) {
				
				if (FileTitle == null || FileTitle.contains("###")) {
							JOptionPane.showMessageDialog(Launcher.this, labels.getString("label034"));
					     return;
				   }
					
				   if(!FileTitle.endsWith(".txt")) {
					   					
							UIManager.put("OptionPane.buttonOrientation", 0);		
							UIManager.put("OptionPane.buttonFont", FontTheme.size15b);
							UIManager.put("OptionPane.messageFont", FontTheme.size15p);
							UIManager.put("OptionPane.messageForeground",Details.BLACK);
							UIManager.put("Panel.background",  Details.ORANGE);
							UIManager.put("OptionPane.background", Details.ORANGE);
							
			    JOptionPane.showMessageDialog(Launcher.this, "\n"
				    + "  Only (txt) files can be encrypted!  "
				    + "\n" + "\n", null,
				    JOptionPane.PLAIN_MESSAGE);
	
				   } else {
					   File file = FilePathTrue;
					   if (file == null)
					     return;
	
					   FileWriter writer = null;
					   try {
					     writer = new FileWriter(file);
					     textComp.write(writer); 
					     writer.flush();
					     writer.close();
					   }
					   catch (IOException ex) {
					     JOptionPane.showMessageDialog(Launcher.this,
					    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
					   }
					   
					   String codeGet = new String();
					   
					   while (codeGet.length() <= 7) {
						   codeGet = JOptionPane.showInputDialog(
							        frameEncryption, 
							        labels.getString("label031")+":\n  *** "+labels.getString("label032")+" ***", 
							        "SmallTextPad "+ labels.getString("label033"), 
							        JOptionPane.OK_CANCEL_OPTION
							    );	
						   if ((codeGet == null) || (codeGet.length() == 0)) {
							   break;
						   }
					   }
					    if (codeGet != null && codeGet.length() >= 7) {	    	 
							   if(!FullPathName.endsWith(".txt")) {
								   FullPathName = (FullPathName + ".txt");
							   }
					    	 
				    		STPFileCrypter.main(FullPathName, codeGet, "encrypt"); 	
							textComp.setText(null);
							setTitle(Title+ " ~ ##### ENCRYPTED #####");
							
							FileTitle = "ENCRYPTED";
							
					    }	
					    logHistory.main(file.toString(), "encrypted");
					    
				   }
			}
			
		};
		
		JButton encryptButton = new JButton(encryptAction);
		encryptButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		encryptButton.setHorizontalTextPosition(SwingConstants.CENTER);
	    encryptButton.setText(labels.getString("label024"));
	    encryptButton.setToolTipText(labels.getString("label025"));
		bar.add(encryptButton);
		
		
		// Set last ICON to the RIGHT //
			bar.add(Box.createHorizontalGlue()); 
			final ImageIcon lastIcon = new ImageIcon(getClass().getResource("/gcclinux.png"));
			Action lastAction = new AbstractAction("", lastIcon) {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent ev) {
					LaunchBrowser.launcher("http://www.wagemaker.co.uk");
				}
			};
			bar.add(lastAction);
	
	 return bar;
	}
	
	
	// Create a JMenuBar with file & edit menus.
	protected JMenuBar createMenuBar() {
	 JMenuBar menubar = new JMenuBar();
	 JMenu lingo = new JMenu(labels.getString("label028"));
	 JMenu file = new JMenu(labels.getString("label001"));
	 JMenu edit = new JMenu(labels.getString("label002"));
	 JMenu help = new JMenu(labels.getString("label003"));
	 JMenu tool = new JMenu(labels.getString("label004"));
	 tool.add(lingo);
	 menubar.add(file);
	 menubar.add(edit);
	 menubar.add(tool);
	 menubar.add(help);
	 
	 JMenuItem mnuItemAbout = new JMenuItem(labels.getString("label005"), new ImageIcon(getClass().getResource("/about.png"))); // Sub-Menu About Entry
	 JMenuItem mnuItemSupport = new JMenuItem(labels.getString("label006"), new ImageIcon(getClass().getResource("/support.png"))); // Sub-Menu About Entry
 	// Twitter and PayPal menu items removed
	 JMenuItem mnuItemOpen = new JMenuItem(labels.getString("label009"), new ImageIcon(getClass().getResource("/open.png"))); // Sub-Menu About Entry
	 JMenuItem mnuItemSave = new JMenuItem(labels.getString("label010"), new ImageIcon(getClass().getResource("/save.png"))); // Sub-Menu About Entry
	 JMenuItem mnuItemExit = new JMenuItem(labels.getString("label011"), new ImageIcon(getClass().getResource("/exit.png"))); // Sub-Menu About Entry
	 JMenuItem mnuItemNew = new JMenuItem(labels.getString("label012"), new ImageIcon(getClass().getResource("/new.png"))); // Sub-Menu About Entry
	 
	 //TODO More languages
	 JMenuItem lang_en = new JMenuItem(labels.getString("label029"));
	 JMenuItem lang_nl = new JMenuItem(labels.getString("label030"));
	 JMenuItem lang_pl = new JMenuItem(labels.getString("label036"));
	 JMenuItem lang_pt = new JMenuItem(labels.getString("label038"));
	 lingo.add(lang_en);
	 lingo.add(lang_pl);
	 lingo.add(lang_nl);
	 lingo.add(lang_pt);
	 
	 JMenu mnuItemHist = new JMenu(labels.getString("label013"));
	 mnuItemHist.setIcon(new ImageIcon(getClass().getResource("/history.png")));
	 JMenuItem mnuItemHisView = new JMenuItem(labels.getString("label014"), new ImageIcon(getClass().getResource("/history.png")));
	 JMenuItem mnuItemHisDelete = new JMenuItem(labels.getString("label015"), new ImageIcon(getClass().getResource("/delete.png")));
	 
	 file.add(mnuItemHist);
	 mnuItemOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 file.add(mnuItemOpen);
	 mnuItemSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 file.add(mnuItemSave);
	 mnuItemNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 file.add(mnuItemNew);
	 mnuItemExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 file.add(mnuItemExit);
	 
	 mnuItemHisView.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 mnuItemHisDelete.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 mnuItemHist.add(mnuItemHisView);
	 mnuItemHist.add(mnuItemHisDelete);
	 
	 final ImageIcon printIcon = new ImageIcon(getClass().getResource("/printer.png"));
	 JMenuItem printItem = new JMenuItem(labels.getString("label016"), printIcon);
	 printItem.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()));
	 
	 edit.add(textComp.getActionMap().get(DefaultEditorKit.cutAction));
	 edit.add(textComp.getActionMap().get(DefaultEditorKit.copyAction));
	 edit.add(textComp.getActionMap().get(DefaultEditorKit.pasteAction));
	 edit.add(printItem);
	 edit.add(textComp.getActionMap().get(DefaultEditorKit.selectWordAction));
	 edit.add(textComp.getActionMap().get(DefaultEditorKit.selectAllAction));
	
	 help.add(mnuItemSupport);
	 help.add(mnuItemAbout);
	 
	 lang_en.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) { 
			LangS.setLanguage("en");
			Position.setPosX(getX());
			Position.setPosY(getY());
			Size.setFrameHeight(getHeight());
			Size.setFrameWidth(getWidth());
			restartApplication();
	     }
	 });
	 
	 lang_nl.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) { 
			LangS.setLanguage("nl");
			Position.setPosX(getX());
			Position.setPosY(getY());
			Size.setFrameHeight(getHeight());
			Size.setFrameWidth(getWidth());
			restartApplication();
	     }
	 });
	 
	 lang_pt.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) { 
			LangS.setLanguage("pt");
			Position.setPosX(getX());
			Position.setPosY(getY());
			Size.setFrameHeight(getHeight());
			Size.setFrameWidth(getWidth());
			restartApplication();
	     }
	 });
	 
	 lang_pl.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) { 
			LangS.setLanguage("pl");
			Position.setPosX(getX());
			Position.setPosY(getY());
			Size.setFrameHeight(getHeight());
			Size.setFrameWidth(getWidth());
			restartApplication();
	     }
	 });
	 
	 printItem.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) {
				try {
					textComp.print(null, null, true, null, null, true);
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
	     }
	 });
	 
	 mnuItemHisDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				File file = null;
				try {
					file = Details.historyFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				boolean success = file.delete();
				if (!success) {
					try {
						
						file.createNewFile();
						FileWriter resultfilestr = new FileWriter(Details.historyFile(), true);
						BufferedWriter outHist = new BufferedWriter(resultfilestr);
						outHist.write("<center><b><h1>\"SmallTextPad "+labels.getString("label013")+"\"</h1></b>");
						outHist.newLine();
						outHist.write("<table width=\"1010\" border cellpadding=\"2\">");
						outHist.close();
						
						UIManager.put("OptionPane.buttonOrientation", 0);		
						UIManager.put("OptionPane.buttonFont", FontTheme.size15b);
						UIManager.put("OptionPane.messageFont", FontTheme.size15p);
						UIManager.put("OptionPane.messageForeground",Details.BLACK);
						UIManager.put("Panel.background",  Details.ORANGE);
						UIManager.put("OptionPane.background", Details.ORANGE);
						
					JOptionPane.showMessageDialog(Launcher.this, "\n"
						+ "  FAILED TO DELETE SmallTextPad HISTORY "
						+ "\n" + "\n", Details.history,
						JOptionPane.PLAIN_MESSAGE);
					} catch (Exception fz) {
					}
					;
				} else {
					try {
						
						file.createNewFile();
						FileWriter resultfilestr = new FileWriter(Details.historyFile(), true);
						BufferedWriter outHist = new BufferedWriter(resultfilestr);
						outHist.write("<center><b><h1>\"SmallTextPad "+labels.getString("label013")+"\"</h1></b>");
						outHist.newLine();
						outHist.write("<table width=\"1010\" border cellpadding=\"2\">");
						outHist.close();
						
						UIManager.put("OptionPane.buttonOrientation", 0);		
						UIManager.put("OptionPane.buttonFont", FontTheme.size15b);
						UIManager.put("OptionPane.messageFont", FontTheme.size15p);
						UIManager.put("OptionPane.messageForeground",Details.BLACK);
						UIManager.put("Panel.background",  Details.ORANGE);
						UIManager.put("OptionPane.background", Details.ORANGE);
						
			JOptionPane.showMessageDialog(Launcher.this, "\n"
				+ "  ALL HISTORY NOW DELETED  "
				+ "\n" + "\n", Details.history,
				JOptionPane.PLAIN_MESSAGE);
					} catch (Exception fz) {
					}
					;
				}
			}
		});
	 
	 mnuItemHisView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				File file = null;
				try {
					file = Details.historyFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (!file.exists()) {
					try {
						file.createNewFile();
						FileWriter resultfilestr = new FileWriter(Details.historyFile(), true);
						BufferedWriter outHist = new BufferedWriter(resultfilestr);
						outHist.write("<center><b><h1>\"SmallTextPad "+labels.getString("label013")+"\"</h1></b>");
						outHist.newLine();
						outHist.write("<table width=\"1000\" border cellpadding=\"2\">");
						outHist.close();
						DisplayReport.Launch(null);
					} catch (IOException e) {
					}
				} else {
					try {
						DisplayReport.Launch(null);
					} catch (IOException e) {
					}
				}
			}
	 });
	 
	 //Drop down menu
	 
	 mnuItemSave.addActionListener(new ActionListener() {
		 @SuppressWarnings("unused")
		@Override 
		 public void actionPerformed(ActionEvent event) {
			 
				FileFilter filter = new FileNameExtensionFilter("SmallTextPad (TXT)", "txt", "TXT");
				JFileChooser chooser = new JFileChooser(LastPath.getLastPath());
				chooser.setDialogTitle(labels.getString("label010"));
				chooser.addChoosableFileFilter(filter);
				
				if (FileTitle == null) {
	
				   if (chooser.showSaveDialog(Launcher.this) != JFileChooser.APPROVE_OPTION) return;
				   File file = chooser.getSelectedFile();
				   String filePath = file.getAbsolutePath();
				   
				   //System.out.println("filePath - "+filePath);
				   
				   if(!filePath.endsWith(".txt")) {
					   file = new File(filePath + ".txt");
				   }
				   if (file == null)
				     return;
	
				   FileWriter writer = null;
				   try {
				     writer = new FileWriter(file);
				     textComp.write(writer);
				     
					   String filename=chooser.getSelectedFile().getName();
					   String lastPath = chooser.getCurrentDirectory().toString();
					   LastPath.setLastPath(lastPath);
					   
					   if(!filePath.endsWith(".txt")) {
						   setTitle(Title+" ~ " + filename+ ".txt");
						   FileTitle = filename+".txt";
					   } else {
						   setTitle(Title+" ~ " + filename);
						   FileTitle = filename;
					   }
					   
				   }
				   catch (IOException ex) {
				     JOptionPane.showMessageDialog(Launcher.this,
				    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
				   }
				   finally {
				     if (writer != null) {
				       try {
				         writer.close();
				       } catch (IOException x) {}
				     }
				   }
				   
				   String filename=chooser.getSelectedFile().getName();
				   FullPathName = filePath;
				   FilePathTrue = file;
				   String lastPath = chooser.getCurrentDirectory().toString();
				   LastPath.setLastPath(lastPath);
				   
				   if(!filePath.endsWith(".txt"))
					   filePath = filePath + ".txt";
	
				   
				   logHistory.main(filePath, labels.getString("label027"));
				   
				 } else {  // Just save file is already exist
					 
					   File file = FilePathTrue;
					   if (file.toString().endsWith(Details.encryptionExtention) && file.toString().indexOf(".") > 0) {
								file = new File(file.toString().substring(0, file.toString().lastIndexOf(".")));
						}	 
	   
					   if (file == null)
					     return;
	
					   FileWriter writer = null;
					   try {
					     writer = new FileWriter(file);
					     textComp.write(writer);   
					   }
					   catch (IOException ex) {
					     JOptionPane.showMessageDialog(Launcher.this,
					    		 labels.getString("label026"), "ERROR", JOptionPane.ERROR_MESSAGE);
					   }
					   finally {
					     if (writer != null) {
					       try {
					         writer.close();
					       } catch (IOException x) {}
					     }
					   }
					   
					   String lastPath = chooser.getCurrentDirectory().toString();
					   LastPath.setLastPath(lastPath);				   
					   logHistory.main(file.toString(), labels.getString("label027"));
				 }
			} 
	 });
	 //open drop down menu
	 mnuItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {			
				FileFilter filter2 = new FileNameExtensionFilter("Text File (TXT)", "txt", "TXT");
				FileFilter filterSecure = new FileNameExtensionFilter("SmallTextPad (SSTP)", Details.encryptionExtention);
				
				   JFileChooser chooser = new JFileChooser(LastPath.getLastPath());
				   chooser.setAcceptAllFileFilterUsed(false);
				   chooser.setDialogTitle(labels.getString("label009"));
				   chooser.addChoosableFileFilter(filter2);
				   chooser.addChoosableFileFilter(filterSecure);
				   
				   if (chooser.showOpenDialog(Launcher.this) !=
				       JFileChooser.APPROVE_OPTION)
					   return;
				   		File file = chooser.getSelectedFile();
						   String filePath = file.getAbsolutePath();
				   if (filePath == null)
				     return;
	
				   FileReader reader = null;
				   String fileName = file.toString();
				   try {
					   if(fileName.endsWith(".txt") || fileName.endsWith(".java")) {
						   reader = new FileReader(file);
						   textComp.read(reader, null);
						   undoRedo(); 
						   String filename=chooser.getSelectedFile().getName();
						   setTitle(Title+" ~ " + filename);
						   
						   filename=chooser.getSelectedFile().getName();
						   FileTitle = filename;
						   FullPathName = filePath;
						   FilePathTrue = file;
						   
						   logHistory.main(filePath, "opened");
						   String lastPath = chooser.getCurrentDirectory().toString();
						   LastPath.setLastPath(lastPath);				   
						   
					   } else if (fileName.endsWith(Details.encryptionExtention)){
						   
						   String justName = file.getName();
						      
						   String codeGet = new String();
						   
						   while (codeGet.length() <= 7) {
							   codeGet = JOptionPane.showInputDialog(
								        frameEncryption, 
								        labels.getString("label031")+":\n"+labels.getString("label001")+": "+justName, 
								        "SmallTextPad  "+labels.getString("label033"), 
								        JOptionPane.OK_CANCEL_OPTION
								    );	
							   if ((codeGet == null) || (codeGet.length() == 0)) {
								   break;
							   }
						   }
						   
						   if ((codeGet != null) && (codeGet.length() > 0)) {
							   while (STPFileCrypter.main(fileName, codeGet, "decrypt") == false) {
								   codeGet = JOptionPane.showInputDialog(
									        frameEncryption, 
									        labels.getString("label035")+":\n"+labels.getString("label001")+": "+justName, 
									        "SmallTextPad  "+labels.getString("label033"), 
									        JOptionPane.OK_CANCEL_OPTION
									    );	
								   if ((codeGet == null) || (codeGet.length() == 0)) {
									   break;
								   }
							   }
		
						   }
						   
						   
	
						   if(filePath.endsWith("."+Details.encryptionExtention)) {
							   if (filePath.indexOf(".") > 0)
									filePath = filePath.substring(0, filePath.lastIndexOf("."));
						   }
						   
						   if ((codeGet != null) && (codeGet.length() > 0)) {
								File varDecryptedFile = new File(filePath);
								String decryptFile = varDecryptedFile.toString();
								
									   if(decryptFile.endsWith(".txt")) {
										   try {
											reader = new FileReader(varDecryptedFile);
										} catch (FileNotFoundException e) {
											e.printStackTrace();
										}
										   try {
											textComp.read(reader, null);
										} catch (IOException e) {
											e.printStackTrace();
										}
	
										   setTitle(Title+" ~ " + varDecryptedFile.getName());
										   FileTitle = varDecryptedFile.getName();
										   FullPathName = filePath;
										   FilePathTrue = file;
									   } 	
						   }
						   
						   logHistory.main(filePath+"."+Details.encryptionExtention, "decrypted");
						   String lastPath = chooser.getCurrentDirectory().toString();
						   LastPath.setLastPath(lastPath);				   
					   
					   } else {
						   JOptionPane.showMessageDialog(Launcher.this,
								     "Unknown File Format", "ERROR", JOptionPane.ERROR_MESSAGE);	   
					   }
				   } catch (IOException ex) {
				     JOptionPane.showMessageDialog(Launcher.this,
				     "Unknown File Format", "ERROR", JOptionPane.ERROR_MESSAGE);
				   }
				   finally {
				     if (reader != null) {
				       try {
				         reader.close();
				       } catch (IOException x) {}
				     }
				   }
			}
		});
	 
	 mnuItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
	             About.Main(null);
			}
		});
	 
		mnuItemSupport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try 
		        {
					LaunchBrowser.launcher("https://github.com/gcclinux/smalltextpad/discussions/");
		        }           
		        catch (Exception e) {
	
			}
		}});

		
		mnuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Position.setPosX(getX());
				Position.setPosY(getY());
				Size.setFrameHeight(getHeight());
				Size.setFrameWidth(getWidth());
				System.exit(0);
			}
		});
		
		mnuItemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				textComp.setText(null);
				   setTitle(Title);
				   FileTitle = null;
			}
		});
		
	 return menubar;
	}
	
	protected JTextComponent getTextComponent() { 
		return textComp; 
		}
	
	public class ExitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ExitAction() { 
			super("Exit", new ImageIcon("/exit.png")); 
			}

            
		public void actionPerformed(ActionEvent ev) { 
			Position.setPosX(getParent().getX());
			Position.setPosY(getParent().getY());
			Size.setFrameHeight(getParent().getHeight());
			Size.setFrameWidth(getParent().getWidth());
			System.exit(0); 
		}
	}

	/**
	 * Relaunch the Java application with the same classpath and main class.
	 * This method attempts to find the java executable from java.home and
	 * starts a new process, then exits the current JVM.
	 */
	private void restartApplication() {
		try {
			String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			String classpath = System.getProperty("java.class.path");
			List<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classpath);
			command.add("wagemaker.co.uk.main.Launcher");

			ProcessBuilder builder = new ProcessBuilder(command);
			builder.inheritIO();
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
