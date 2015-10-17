package com.recommendation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("test")
public class TestClass {

	@GET
	String test(@QueryParam("book") String book){
		new GetBookAuthor().get(book);
		return "success";
	}
}
