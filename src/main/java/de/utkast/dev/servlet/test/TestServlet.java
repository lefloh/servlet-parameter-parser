package de.utkast.dev.servlet.test;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.utkast.dev.servlet.parser.ParameterParser;
import de.utkast.dev.servlet.parser.ParameterWrapper;

@WebServlet("/parse")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private ParameterParser parser;
		
	public void parse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Modern browser encode form-data according according to the encoding used for the submitting page 
		// or defined by the meta-charset-attribute but they don't tell the server which encoding they have used. 
		// Accept-charset header is not send any more.
		// As we want to test with multiple encodings we are doing this terrible hack...
		// note that we can't pass a hidden field because reading will affect the encoding of RequestParameterMap
		String referrer = request.getHeader("referer");
		String encoding = null;
		if (referrer != null && referrer.contains("test-")) {
			encoding = referrer.replaceAll(".*test-", "").replaceAll("\\.html", "");
		} else {
			encoding = request.getCharacterEncoding();
		}
		ParameterWrapper wrapper = encoding == null ? parser.parseParameters(request) : parser.parseParameters(request, encoding);
		request.setAttribute("wrapper", wrapper);	
		request.getRequestDispatcher("parsed.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		parse(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		parse(request, response);
	}
	
}
