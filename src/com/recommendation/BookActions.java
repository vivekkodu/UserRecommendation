package com.recommendation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BookActions {

	public static List<String> getTitlesFromBookList(List<Books> books) {

		if (books != null && books.size() > 0) {
			List<String> bookTitles = new ArrayList<String>();
			for (int i = 0; i < books.size(); i++) {
				String bookTitle = books.get(i).getBookName();
				System.out.println("Adding book in the return doc: "
						+ bookTitle + ", rating" + books.get(i).getRating());
				bookTitles.add(bookTitle);
			}

			return bookTitles;
		} else {
			return null;
		}
	}

	/**
	 * This method returns the list of books sorted in order of rating
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static List<Books> getBooksByRating(String response)
			throws Exception {

		List<Books> bookList = getBooks(response);

		if (bookList != null && bookList.size() > 0) {
			Collections.sort(bookList);
			return bookList;
		}

		throw new Exception("No books present for this author");

	}

	/**
	 * This method returns creates Books object from the input stream obtained
	 * from GoodReads.com and returns a list of them
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static List<Books> getBooks(String response) throws Exception {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(
				response)));
		NodeList workList = doc.getElementsByTagName("work");
		List<Books> bookList = new ArrayList<Books>();
		if (workList != null && workList.getLength() > 0) {
			for (int i = 0; i < workList.getLength(); i++) {
				Element work = (Element) workList.item(i);
				try {
					float rating = Float.parseFloat(work
							.getElementsByTagName("average_rating").item(0)
							.getTextContent());
					String title = work.getElementsByTagName("title").item(0)
							.getTextContent();
					Books book = new Books(rating, title);
					bookList.add(book);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return bookList;
	}
}
