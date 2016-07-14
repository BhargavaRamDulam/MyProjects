

package com.bhargav.rssfeed;



import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.Statement;

import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class Reader {

	public static void main(String[] args) throws Exception {

		URL url = new URL("https://www.medtechintelligence.com/category/medflix/medflix/feed/");
		
		XmlReader reader = null;
		Connection conn = null;
		Statement stmt = null;
		URLConnection uc;
		String title = "";
        String link = "";
        String desc = "";
        String pDate = "";
		try {
			uc = url.openConnection();
			uc.addRequestProperty("User-Agent", 
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			reader = new XmlReader(uc);
			SyndFeed feed = new SyndFeedInput().build(reader);

			for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
				SyndEntry entry = (SyndEntry) i.next();
				
				title = title + entry.getTitle();
				link = link + entry.getLink();
				desc = desc + entry.getDescription();
				pDate = pDate + entry.getPublishedDate();
                
			}
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sample", "root", "PASSWORD");
			stmt = conn.createStatement();
			stmt.execute("insert into feed (title,link,description,publishdate) values(' " + title + "','"+link+"','"+desc+"','"+pDate+"')");
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}