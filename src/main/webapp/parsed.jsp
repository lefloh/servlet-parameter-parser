<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Encoding Test</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	</head>
	<body>
	
		<h2>Query Parameters</h2>

		<ul id="queryParameters">
			<c:forEach items="${wrapper.queryParameters}" var="item">
				<li>${item.key}
					<ul>
						<c:forEach items="${item.value}" var="val">
							<li>${val}</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>

		<h2>Form Parameters</h2>
		
		<ul id="formParameters">
			<c:forEach items="${wrapper.formParameters}" var="item">
				<li>${item.key}
					<ul>
						<c:forEach items="${item.value}" var="val">
							<li>${val}</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
		
		<h2>All Parameters</h2>
		
		<ul id="allParameters">
			<c:forEach items="${wrapper.parameters}" var="item">
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