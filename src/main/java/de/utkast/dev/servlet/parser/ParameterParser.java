package de.utkast.dev.servlet.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@ApplicationScoped
public class ParameterParser {

	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private static final String DEFAULT_URI_ENCODING = "ISO-8859-1";
	
	private static final String SYSTEM_PROPERTY_URI_ENCODING = "org.apache.catalina.connector.URI_ENCODING";
	
	private Charset uriEncoding;
	
	@PostConstruct
	public void onPostConstruct() {
		// determine if defaultUriEncoding is changed via a SystemProperty
		// NOTE: this property is used by JBoss. Other ApplicationServers might not use this.
		String systemUriEncoding = System.getProperty(SYSTEM_PROPERTY_URI_ENCODING);
		uriEncoding = systemUriEncoding == null || "".equals(systemUriEncoding.trim())
				? Charset.forName(DEFAULT_URI_ENCODING) : Charset.forName(systemUriEncoding);
	}
	
	/**
	 * Parses the parameters from the request to the given encoding
	 * This method will call request.setCharacterEncoding which will
	 * have no effect if the parameters or request.getReader() was called prior. 
	 */
	public ParameterWrapper parseParameters(HttpServletRequest request, String encoding) throws UnsupportedEncodingException {
		request.setCharacterEncoding(encoding);
		Set<String> queryKeys = parseQuery(request.getQueryString());
		Map<String, String[]> formParams = new HashMap<String, String[]>();
		Map<String, String[]> queryParams = new HashMap<String, String[]>();
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			if (queryKeys.contains(entry.getKey())) {
				List<String> values = new ArrayList<>(entry.getValue().length);
				for (String value : entry.getValue()) {
					values.add(recode(value, encoding));
				}
				queryParams.put(recode(entry.getKey(), encoding), values.toArray(new String[values.size()]));
			} else {
				formParams.put(entry.getKey(), entry.getValue());
			}
		}
		return new ParameterWrapper(formParams, queryParams);
	}

	/**
	 * Convenient method. Will use DEFAULT_ENCODING 
	 */
	public ParameterWrapper parseParameters(HttpServletRequest request) throws UnsupportedEncodingException {
		return parseParameters(request, DEFAULT_ENCODING);
	}
	
	/**
	 * Decodes the queryString and retrieves the keys.
	 * We don't use the values to avoid splitting them in another way then servletRequest.getParameters() does.
	 * Recoding from uriEncoding to wanted encoding is done at parseParameter. So we are using
	 * uriEncoding here to ensure passing back the same key back as we will find in the requestParameterMap
	 * and this may be something like mÃ¼sli.
	 */
	private Set<String> parseQuery(String queryString) throws UnsupportedEncodingException {
		Set<String> keys = new HashSet<String>();
		if (queryString == null || "".equals(queryString.trim())) {
			return keys;
		}
		String[] pairs = queryString.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        keys.add(URLDecoder.decode(pair.substring(0, idx), uriEncoding.name()));
	    }
	    return keys;
	}
	
	/**
	 * recodes a String from uriEncoding to encoding
	 */
	private String recode(String value, String encoding) throws UnsupportedEncodingException {
		return new String(value.getBytes(uriEncoding), encoding);
	}
	
}
