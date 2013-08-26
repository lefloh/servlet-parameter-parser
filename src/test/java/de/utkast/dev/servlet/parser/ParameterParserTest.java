package de.utkast.dev.servlet.parser;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.utkast.dev.servlet.test.TestServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class ParameterParserTest {
	
	@ArquillianResource 
	private URL deploymentURL;
	
	@Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
    		.addClasses(ParameterParser.class, ParameterWrapper.class, TestServlet.class)
            .addAsWebResource(new File("src/main/webapp/parsed.jsp"))
            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
            .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Test
    public void testGETUtf_8() throws Exception {
    	testGET("foo=bar&füü=bär&city=M%C3%BCnchen&array=one&array=two", "UTF-8");
    }
    
    @Test
    public void testGETIso8859_15() throws Exception {
    	// we use München cause M%FCnchen will be decoded to M%3Fnchen which results to M?nchen
    	testGET("foo=bar&füü=bär&city=München&array=one&array=two", "ISO-8859-15");
    }
    
    private void testGET(String queryString, String encoding) {
    	Parser parser = Parser.newGETInstance(queryUrl(queryString), encoding);
    	Map<String, String[]> expected = urlParameterMap();
    	compareMaps(expected, parser.getQueryParameters());
    	compareMaps(expected, parser.getAllParameters());
    	Assert.assertTrue("Recieved form-params which are not passed", parser.getFormParameters().isEmpty());
    }
    
    @Test
    public void testPOSTUtf_8() throws Exception {
    	testPOST("foo=bar&füü=bär&city=M%C3%BCnchen&array=one&array=two", formParameterMap(), "UTF-8");
    }
    
    @Test
    public void testPOSTIso8859_15() throws Exception {
    	// we use München cause M%FCnchen will be decoded to M%3Fnchen which results to M?nchen
    	testPOST("foo=bar&füü=bär&city=München&array=one&array=two", formParameterMap(), "ISO-8859-15");
    }    
    
    private void testPOST(String queryString, Map<String, String[]> formParams, String encoding) throws Exception {
    	Parser parser = Parser.newPOSTInstance(queryUrl(queryString), formParams, encoding);
    	Map<String, String[]> expectedUrlParams = urlParameterMap();
    	compareMaps(expectedUrlParams, parser.getQueryParameters());
    	compareMaps(formParams, parser.getFormParameters());
    	Map<String, String[]> allParams = new HashMap<String, String[]>(expectedUrlParams);
    	allParams.putAll(formParams);
    	compareMaps(allParams, parser.getAllParameters());
    }

	private Map<String, String[]> urlParameterMap() {
		Map<String, String[]> expected = new HashMap<String, String[]>();
    	expected.put("foo", new String[] { "bar" });
    	expected.put("füü", new String[] { "bär" });
    	expected.put("city", new String[] { "München" });
    	expected.put("array", new String[] { "one", "two" });
		return expected;
	}
	
	private Map<String, String[]> formParameterMap() {
		Map<String, String[]> expected = new HashMap<String, String[]>();
    	expected.put("ta", new String[] { "tü" });
    	expected.put("türü", new String[] { "lülü" });
    	expected.put("street", new String[] { "Münchener Straße 13" });
    	expected.put("formArray", new String[] { "three", "four" });
		return expected;
	}
    
    private String queryUrl(String query) {
    	return String.format("%s://localhost:%d%sparse?%s", // jodd doesn't like [0:0:0:0] as host 
    			deploymentURL.getProtocol(), deploymentURL.getPort(), deploymentURL.getPath(), query);
    }
    
    private void compareMaps(Map<String, String[]> expected, Map<String, String[]> actual) {
    	Assert.assertEquals("Maps don't have the same size", expected.size(), actual.size());
    	for (String key : expected.keySet()) {
    		Assert.assertTrue(String.format("Map does not contain key '%s'", key), actual.containsKey(key));
    		Assert.assertTrue(String.format("Result for key '%s' differs", key), Arrays.equals(expected.get(key), actual.get(key)));
    	}
    }
    
}
