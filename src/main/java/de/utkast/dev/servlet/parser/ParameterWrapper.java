package de.utkast.dev.servlet.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper object for form- and query-parameters
 */
public class ParameterWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<String, String[]> formParameters;
	
	private final Map<String, String[]> queryParameters;

	public ParameterWrapper(Map<String, String[]> formParameters, Map<String, String[]> queryParameters) {
		this.formParameters = formParameters;
		this.queryParameters = queryParameters;
	}

	public Map<String, String[]> getFormParameters() {
		return formParameters;
	}

	public Map<String, String[]> getQueryParameters() {
		return queryParameters;
	}

	public Map<String, String[]> getParameters() {
		Map<String, String[]> params = new HashMap<String, String[]>(formParameters);
		params.putAll(queryParameters);
		return params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formParameters == null) ? 0 : formParameters.hashCode());
		result = prime * result + ((queryParameters == null) ? 0 : queryParameters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ParameterWrapper other = (ParameterWrapper) obj;
		if (formParameters == null) {
			if (other.formParameters != null) {
				return false;
			}
		} else if (!formParameters.equals(other.formParameters)) {
			return false;
		}
		if (queryParameters == null) {
			if (other.queryParameters != null) {
				return false;
			}
		} else if (!queryParameters.equals(other.queryParameters)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ParameterWrapper [formParameters=" + formParameters + ", queryParameters=" + queryParameters + "]";
	}
	
}
