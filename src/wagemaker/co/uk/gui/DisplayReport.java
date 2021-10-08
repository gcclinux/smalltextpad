package wagemaker.co.uk.gui;

/******************************************************************************************************
 * @author by Ricardo Wagemaker (["java"] + "@" + "wagemaker.co.uk") 2010-2018
 * @title SmallTextPad
 * @version 1.3.2
 * @since   2013 - 2018
 ******************************************************************************************************/

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import wagemaker.co.uk.display.Position;
import wagemaker.co.uk.display.Size;
import wagemaker.co.uk.utility.Details;


public class DisplayReport
		extends 	JFrame
		implements	HyperlinkListener
{
	private static final long serialVersionUID = 1L;

	private	JEditorPane	html;
	
    private void loadFrameIcon() {
        URL imgUrl = null;
        ImageIcon imgIcon = null;
        
        imgUrl = DisplayReport.class.getResource("/gcclinux.png");
        imgIcon = new ImageIcon(imgUrl);
        Image img = imgIcon.getImage();
        this.setIconImage(img);
    }
	
	public DisplayReport() throws IOException
	{
		setTitle(Details.Name+Details.Separate+Details.Title);
		setSize(Size.getFrameWidth(), Size.getFrameHeight());
		setBackground(Details.DARKGREY);
		setLocation(Position.getPosX(), Position.getPosY());
		loadFrameIcon();
		
		File filename = Details.historyFile();

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel, BorderLayout.CENTER );
		

    	try {
    		URL url = new URL( "file:///"+filename);
		    html = new JEditorPane( url );
		    //html.setPreferredSize(new Dimension(800, 600));
		    html.setEditable( false );
		    JScrollPane scrollPane = new JScrollPane();
		    scrollPane.getViewport().add(html);
		    topPanel.add( scrollPane, BorderLayout.CENTER );
		    html.addHyperlinkListener( this );
		}
		catch( MalformedURLException e )
		{
		    System.out.println( "Malformed URL: " + e );
		}
		catch( IOException e )
		{
		    System.out.println( "IOException: " + e );
		}
    	
	}
	

    public void hyperlinkUpdate( HyperlinkEvent event )
    {
		if( event.getEventType() == HyperlinkEvent.EventType.ACTIVATED )
		{
			// Load some cursors
			Cursor cursor = html.getCursor();
			Cursor waitCursor = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR );
			html.setCursor( waitCursor );

			// Handle the hyperlink change
			SwingUtilities.invokeLater( new PageLoader( html,event.getURL(), cursor ) );
		}
    }

	public static void Launch( String args[] ) throws IOException
	{
		DisplayReport mainFrame	= new DisplayReport();
		mainFrame.setVisible( true );
	}
}

