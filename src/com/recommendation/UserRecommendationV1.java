package com.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * This API gives bok recommendation based on a given book
 * 
 * @author VIVEK VERMA
 *
 */
public class UserRecommendationV1 {

	String recommend(String bookName) {

		Gson gson = new Gson();

		// Get author of the book
		String author = new GetBookAuthor().get(bookName);
		if (author == null) {
			return "Author not found";
		}

		// Get other books of the author sorted in order of the rating
		List<String> otherBooksOfAuthors = new GetAllBookTitlesOfAnAuthor()
				.get(author);
		System.out.println("\n Obtained " + otherBooksOfAuthors.size()
				+ " books from author " + author + "\n");

		// Get book info from walmart for the obtained books
		List<Object> recommendedBooks = getWalmartBookObject(otherBooksOfAuthors);

		// Return json object to be used to display to the user
		if (recommendedBooks != null) {
			return gson.toJson(recommendedBooks);
		}

		return "No recommendation";

	}

	@SuppressWarnings("rawtypes")
	List<Object> getWalmartBookObject(List<String> otherBooksOfAuthors) {
		List<Object> recommendedBooks = new ArrayList<Object>();

		// Check if author has other book
		if (otherBooksOfAuthors != null && otherBooksOfAuthors.size() > 0) {
			for (int i = 0; i < otherBooksOfAuthors.size(); i++) {
				try {
					WalmartSearch walmartSearch = new WalmartSearch();
					System.out.println("\n searching for book "
							+ otherBooksOfAuthors.get(i) + " in walmart \n");

					// Search for the bo
					walmartSearch.search(otherBooksOfAuthors.get(i), null);
					if (walmartSearch.response != null
							&& walmartSearch.response.containsKey("items")) {
						// Obtained book from walmart
						List items = (List) walmartSearch.response.get("items");
						// Add obtained result to the recommended book list
						recommendedBooks.add(items.get(0));
					}

					// Since walmart allows only 5 calls per second, this sleep
					// is 300 ms to be on safe side(instead o 200 ms)
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return recommendedBooks;
	}
}
