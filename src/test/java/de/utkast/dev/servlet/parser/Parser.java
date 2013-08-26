package de.utkast.dev.servlet.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.http.HttpRequest;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class Parser {

	private Map<String, String[]> formParameters;
	
	private Map<String, String[]> queryParameters;
	
	private Map<String, String[]> allParameters;
	
	private Parser() {}
	
	public static Parser newGETInstance(String url, String queryEncoding) {		
		HttpRequest request = HttpRequest.get(url)
										.contentType("text/html;charset=" + queryEncoding)
										.queryEncoding(queryEncoding);
		return parseHtml(request.send().bodyText());
	}
	
	public static Parser newPOSTInstance(String url, Map<String, String[]> formParams, String encoding) throws IOException {
		HttpRequest request = HttpRequest.post(url)
										.contentType("text/html;charset=" + encoding)
										.queryEncoding(encoding)
										.formEncoding(encoding);
		for (Map.Entry<String, String[]> entry : formParams.entrySet()) {
			for (String value : entry.getValue()) {
				request.form(entry.getKey(), value);
			}
		}
		return parseHtml(request.send().bodyText());
	}
	
	private static Parser parseHtml(String html) {
		Jerry jerry = Jerry.jerry(html);
		Parser parser = new Parser();
		parser.queryParameters = parseList(jerry.$("#queryParameters"));
		parser.formParameters = parseList(jerry.$("#formParameters"));
		parser.allParameters = parseList(jerry.$("#allParameters"));
		return parser;
	}
	
	private static Map<String, String[]> parseList(Jerry list) {
		final Map<String, String[]> map = new HashMap<String, String[]>();
		list.children().each(new JerryFunction() {
			@Override
			public boolean onNode(Jerry $this, int index) {
				final List<String> values = new ArrayList<String>();
				Jerry valueItems = $this.$("ul");
				valueItems.children().each(new JerryFunction() {
					@Override
					public boolean onNode(Jerry $this, int index) {
						values.add($this.text().trim());
						return true;
					}
				});
				String key = $this.html().substring(0, $this.html().indexOf('<')).trim();
				map.put(key, values.toArray(new String[values.size()]));
				return true;
			}
		});
		return map;
	}

	public Map<String, String[]> getFormParameters() {
		return formParameters;
	}

	public Map<String, String[]> getQueryParameters() {
		return queryParameters;
	}

	public Map<String, String[]> getAllParameters() {
		return allParameters;
	}
	
}
