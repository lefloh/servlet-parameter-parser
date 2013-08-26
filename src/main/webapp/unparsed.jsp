<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Encoding Test &ndash; unparsed SerlvetRequestParameters</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	</head>
	<body>
	
		<h2>RequestParameters</h2>

		<ul id="requestParameters">
			<c:forEach items="${pageContext.request.parameterMap}" var="item">
				<li>${item.key}
					<ul>
						<c:forEach items="${item.value}" var="val">
							<li>${val}</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>

	</body>
</html>