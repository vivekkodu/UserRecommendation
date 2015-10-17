package com.recommendation;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Get all books liked by a user on facebook
 * 
 * @author VIVEK VERMA
 *
 */
public class GetFBUserBooks {

	@SuppressWarnings("rawtypes")
	List<String> get(String token) {
		List<String> userBooks = new ArrayList<String>();
		Gson gson = new Gson();
		String responseString = "";
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI(token));
		responseString = target.request().accept(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println("Obtained response is " + responseString);

		try {
			Map responseObject = (Map) gson.fromJson(responseString, Map.class);
			if (responseObject != null && responseObject.containsKey("data")) {
				List data = (List) responseObject.get("data");
				if (data.size() > 0) {
					for (int i = 0; i < data.size(); i++) {
						Map book = (Map) data.get(i);
						if (book.containsKey("name")) {
							userBooks.add((String) book.get("name"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userBooks;
	}

	private URI getBaseURI(String token) {

		String baseUrl = "https://graph.facebook.com/v2.5/10154270417553484/books";
		String url = baseUrl + "?" + "client_id=" + FBLoginDetails.clientId
				+ "&" + "client_secret=" + FBLoginDetails.clientSecret
				+ "&access_token=" + token;

		url = url.replace(" ", "%20");

		System.out.println("url = " + url);
		return UriBuilder.fromUri(url).build();

	}
}
