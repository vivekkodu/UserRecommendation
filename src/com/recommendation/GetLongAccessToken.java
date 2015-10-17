package com.recommendation;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

@Path("getlongaccesstoken")
public class GetLongAccessToken {

	/**
	 * This receives long lived token for a user logged in using facebook
	 * 
	 * @param author
	 * @return Long lived token or null if token was not generated
	 */
	@GET
	String get(@QueryParam("tempToken") String tempToken) {

		if(tempToken==null)
			return "Invalid token provided";
		
		String responseString = "";
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI(tempToken));
		responseString = target.request().accept(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println("Obtained response is " + responseString);

		if (responseString.contains("access_token=")) {
			responseString = responseString.replace("access_token=", "");
			return responseString;
		}else
		{
			return null;
		}

		

	}

	private URI getBaseURI(String tempToken) {

		String baseUrl = "https://graph.facebook.com/oauth/access_token";
		String url = baseUrl + "?" + "client_id=" + FBLoginDetails.clientId
				+ "&" + "client_secret=" + FBLoginDetails.clientSecret
				+ "&fb_exchange_token=" + tempToken
				+ "&grant_type=fb_exchange_token";

		url = url.replace(" ", "%20");

		System.out.println("url = " + url);
		return UriBuilder.fromUri(url).build();

	}
}
