servlet-parameter-parser
========================

Distinguishes between query- and form-parameters and handles encoding.

[ServletRequest.html#setCharacterEncoding][1] only affects parameters passed in the body of the request. URI-Parameters are not affected. 

JBoss for example is using ISO-8859-1 as default wich can be overriden with the systemProperty ```org.apache.catalina.connector.URI_ENCODING```.

This leads to the problem that you may find two encodings in [ServletRequest.html#getParameterMap][2], and even worse: the servlet-API lacks a feature to distinguish between URI-Parameters and FORM-Parameters.

The ```ParameterParser``` in this project is one possible solution to solve this problem. 

   [1]: http://docs.oracle.com/javaee/7/api/javax/servlet/ServletRequest.html#setCharacterEncoding(java.lang.String)
   [2]: http://docs.oracle.com/javaee/7/api/javax/servlet/ServletRequest.html#getParameterMap()
