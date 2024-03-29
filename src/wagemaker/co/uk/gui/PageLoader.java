package wagemaker.co.uk.gui;

/******************************************************************************************************
 * @author by Ricardo Wagemaker (["java"] + "@" + "wagemaker.co.uk") 2010-2018
 * @title SmallTextPad
 * @version 1.2.4
 * @since   2013 - 2018
 ******************************************************************************************************/

import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;


class PageLoader implements Runnable
{
    private JEditorPane html;
    private URL         url;
    private Cursor      cursor;

    PageLoader( JEditorPane html, URL url, Cursor cursor ) 
    {
        this.html = html;
        this.url = url;
        this.cursor = cursor;
    }

    public void run() 
    {
	    if( url == null ) 
	    {

	    	html.setCursor( cursor );

    		Container parent = html.getParent();
    		parent.repaint();
        }
        else 
        {
    		Document doc = html.getDocument();
	    	try {
		        html.setPage( url );
    		}
    		catch( IOException ioe ) 
    		{
    		    html.setDocument( doc );
    		}
    		finally
    		{
		        url = null;
    		    SwingUtilities.invokeLater( this );
	    	}
	    }
	}
}