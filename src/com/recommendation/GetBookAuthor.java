package com.recommendation;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Gets author of a book using GoodReads
 * 
 * @author VIVEK VERMA
 *
 */
public class GetBookAuthor {

	String baseUrl = "https://www.goodreads.com/search/index.xml";

	public String get(String bookName) {

		String responseString = "";
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI(bookName));

		try {
			responseString = target.request()
					.accept(MediaType.APPLICATION_JSON).get(String.class);
			// System.out.println(responseString);
			if (responseString != null) {
				String author = getAuthor(responseString);
				System.out.println("\n Author is " + author + "\n");
				return author;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	private URI getBaseURI(String bookName) {

		String url = baseUrl + "?" + "key=" + GoodReadsDetails.key + "&" + "q="
				+ bookName;

		System.out.println("url = " + url);
		url = url.replace(" ", "%20");
		//url = url.replace(":", "%3A");
		return UriBuilder.fromUri(url).build();

	}

	String getAuthor(String responseString) throws SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(
				responseString)));

		Node author = doc.getElementsByTagName("name").item(0);

		return author.getTextContent().toString();

	}
}
