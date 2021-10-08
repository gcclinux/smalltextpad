package wagemaker.co.uk.utility;

/******************************************************************************************************
 * @author by Ricardo Wagemaker (["java"] + "@" + "wagemaker.co.uk") 2010-2018
 * @title SmallTextPad
 * @version 1.3.1
 * @since   2010 - 2018
 * @License MIT
 ******************************************************************************************************/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class logHistory {

	public logHistory(String filePath, String action) {
		FileWriter resultfilestr = null;
		try {
			resultfilestr = new FileWriter(Details.historyFile(), true);
			BufferedWriter outHist = new BufferedWriter(resultfilestr);
			outHist.write(
					"<tr>" +
					"<td align=\"center\" bgcolor=\"#d3d3d3\">" + new Date().toString() + "</td>" +
					"<td align=\"center\" bgcolor=\"#d3d3d3\">" + action + "</td>" +
					"<td align=\"center\" bgcolor=\"#d3d3d3\">" + filePath + "</td>" +
					"</tr>");
			outHist.newLine();
			outHist.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	public static void main(String filePath, String action) {
		
		new logHistory(filePath, action);

	}

}
