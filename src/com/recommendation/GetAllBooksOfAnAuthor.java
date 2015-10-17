package com.recommendation;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class GetAllBooksOfAnAuthor {

	/**
	 * This gets all the books written by a specific author
	 * 
	 * @param author
	 * @return
	 */
	String get(String author) {

		String responseString = "";
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI(author));
		responseString = target.request().accept(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println("Obtained response is " + responseString);
		return responseString;

	}

	private URI getBaseURI(String author) {

		String baseUrl = "https://www.goodreads.com/search/index.xml";
		String url = baseUrl + "?" + "key=" + GoodReadsDetails.key + "&" + "q="
				+ author;

		url += "&search%5Bfield%5D=author";
		url = url.replace(" ", "%20");
		//url = url.replace(":", "%3A");

		System.out.println("url = " + url);
		return UriBuilder.fromUri(url).build();

	}
}
