package de.unihannover.l3s.solrclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MementoClient
{


    private static String mementoArchiveURL;
    private URL mementoArchiveUrlObj;
    private DateFormat dateTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public MementoClient()
    {
	mementoArchiveURL = "http://wayback.archive-it.org/";
    }

    public List<ArchiveUrl> getArchiveItVersions(int collectionId, String resourceURI)
    {

	try
	{
	    mementoArchiveUrlObj = new URL(mementoArchiveURL + collectionId + "/timemap/link/" + resourceURI);
	}
	catch(MalformedURLException e)
	{
		e.printStackTrace();
	}

	List<ArchiveUrl> archiveVersions = new LinkedList<ArchiveUrl>();

	try
	{
	    HttpURLConnection con = (HttpURLConnection) mementoArchiveUrlObj.openConnection();

	    con.setRequestMethod("GET");
	    con.setDoOutput(true);

	    if(con.getResponseCode() == 404)
	    {
		System.out.println("Not found in archive");
	    }
	    else
	    {
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while((inputLine = in.readLine()) != null)
		{
		    response.append(inputLine);
		}

		Pattern p = Pattern.compile("(https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])(>; rel=\"[^\"]+\"; )(datetime=)(\"[^\"]+\")");
		Matcher m = p.matcher(response.toString());
		while(m.find())
		{
		    archiveVersions.add(new ArchiveUrl(m.group(1), dateTimeFormat.parse(m.group(4).replaceAll("\"", ""))));
		}
		in.close();
	    }
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	catch(ParseException e)
	{
		e.printStackTrace();
	}
	return archiveVersions;
    }
}
