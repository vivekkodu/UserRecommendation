package com.recommendation;

import javax.ws.rs.Path;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

/**
 * Searches for a book in walmart
 * 
 * @author VIVEK VERMA
 *
 */
@Path("walmartsearch")
public class WalmartSearch {

	int numItems = 25;
	String baseUrl = "http://api.walmartlabs.com/v1";
	String action = "search";
	int retry = 3;
	int categoryId=3920;
	Map response;

	// Sort values: [relevance, price, title, bestseller, customerRating, new]
	@GET
	public String search(@QueryParam("query") String query,
			@QueryParam("sort") String sort) {

		String responseString = "";
		Gson gson = new Gson();
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI(query, sort));

		try {
			while (retry > 0) {
				responseString = target.request()
						.accept(MediaType.APPLICATION_JSON).get(String.class);
				//System.out.println("WalmartSearch: Obtained responseString is "+responseString);
				response = gson.fromJson(responseString, Map.class);
				if (response.containsKey("errors")) {
					System.out.println("\n Walmart returned error for book "+query+"\n");
					List errorList = (List) response.get("errors");
					String errorMessage = (String) ((Map) errorList.get(0))
							.get("message");
					System.out.println("Request failed with error: "
							+ errorMessage);
					return null;
				} else {
					if (response.containsKey("numItems")) {
						double numItems=(double) response.get("numItems");
						if (numItems == 0) {
							System.out
									.println("Items not found. Trying again. Retry count="
											+ retry);
							Thread.sleep(200);
						}
						if (numItems > 0) {
							//System.out.println("\n Obtained book from walmart"+ responseString + "\n");
							return responseString;
						}
					}
				}
				retry--;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return null;
	}

	private URI getBaseURI(String query, String sort) {

		String url = baseUrl + "/" + action + "?" + "apiKey="
				+ WalmartAPIDetails.apiKey + "&" + "query=" + query;
		url+="&categoryId="+categoryId;
		if (sort != null) {
			url += "&sort=" + sort;
		}

		url=url.replace(" ", "%20");
		//url = url.replace(":", "%3A");
		System.out.println("url = " + url);
		return UriBuilder.fromUri(url).build();

	}

}