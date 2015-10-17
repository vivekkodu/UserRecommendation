package com.recommendation;

import javax.ws.rs.Path;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

@Path("hptest")
public class Test {
	@GET
	public String test(@QueryParam("book") String book) {

		// new GetBookAuthor().get(q);
		// new GetAllBookTitlesOfAnAuthor().get("ayn rand");
		return new UserRecommendationV1().recommend(book);
		// return "success";
	}

}