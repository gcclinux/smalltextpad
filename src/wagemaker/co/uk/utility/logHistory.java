package wagemaker.co.uk.utility;

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
