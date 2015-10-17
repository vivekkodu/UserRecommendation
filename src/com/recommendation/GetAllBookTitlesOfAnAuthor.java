package com.recommendation;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GetAllBookTitlesOfAnAuthor {

	

	/**
	 * This returns all the book titles which have been written by a author in descending order of rating.
	 * 
	 * @param author
	 * @return
	 */
	public List<String> get(String author) {

		String responseString = "";

		try {
			responseString = new GetAllBooksOfAnAuthor().get(author);
			if (responseString != null) {

				List<Books> books = BookActions
						.getBooksByRating(responseString);

				return BookActions.getTitlesFromBookList(books);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

}
