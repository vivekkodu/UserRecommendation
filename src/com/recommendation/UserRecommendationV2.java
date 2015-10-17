package com.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;

@Path("recommend")
public class UserRecommendationV2 {
	@GET
	public String recommend(@QueryParam("userId") String userId) {

		Gson gson = new Gson();

		List<Books> recommendedBooks = new ArrayList<Books>();

		// String tempToken = null;
		// If user is not loggedIn FBLogin.jsp can be used to login the user.
		// Assuming token generated is tempToken
		// String longToken = new GetLongAccessToken().get(tempToken);
		// I already created a longToken and saved in the separate
		// class(FBLoginDetails).

		// Get 25 books liked by a user on facebook
		List<String> userBooks = new GetFBUserBooks()
				.get(FBLoginDetails.accessToken);
		if (userBooks == null || userBooks.size() == 0) {
			return "No suggestions for the user";
		}

		try {
			for (int i = 0; i < userBooks.size(); i++) {

				// Get author of book obtained from facebook using GoodReads.
				String author = new GetBookAuthor().get(userBooks.get(i));
				if (author == null) {
					continue;
				}

				// Get other books of the same author
				String responseString = new GetAllBooksOfAnAuthor().get(author);
				if (responseString == null) {
					continue;
				}

				// Get books object from the books response obtained from
				// GoodReads
				List<Books> books = BookActions.getBooks(responseString);

				// Add Books list to the recommended books list
				recommendedBooks.addAll(books);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Sort all the recommended books corresponsing to 25 books liked on
		// facebook on the basis of rating
		Collections.sort(recommendedBooks);

		//Get book titles of the recommended books
		List<String> books = BookActions
				.getTitlesFromBookList(recommendedBooks);

		//Search for all the books in walmart
		List<Object> bookRecommendation = getWalmartBookObject(books);

		if (bookRecommendation != null) {
			return gson.toJson(bookRecommendation);
		}

		return "No recommendation for the user";

	}

	@SuppressWarnings("rawtypes")
	List<Object> getWalmartBookObject(List<String> otherBooksOfAuthors) {
		List<Object> recommendedBooks = new ArrayList<Object>();

		if (otherBooksOfAuthors != null && otherBooksOfAuthors.size() > 0) {
			for (int i = 0; i < otherBooksOfAuthors.size(); i++) {
				try {
					WalmartSearch walmartSearch = new WalmartSearch();
					System.out.println("\n searching for book "
							+ otherBooksOfAuthors.get(i) + " in walmart \n");
					
					//Search for one of the recommended book
					walmartSearch.search(otherBooksOfAuthors.get(i), null);
					if (walmartSearch.response != null
							&& walmartSearch.response.containsKey("items")) {
						List items = (List) walmartSearch.response.get("items");
						recommendedBooks.add(items.get(0));
					}
					//Sleep to avoid throttling from Walamrt
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return recommendedBooks;
	}

}