<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<ul>
		<li><a href="<c:url value="/signout" />">Sign Out</a></li>
	</ul>
	<h3>Your Facebook Friends</h3>
		<td><c:forEach items="${contact}" var="friend">
				<tr>
					<c:forEacn items="${friend.contactProfiles}" var="profile">
						<img src="<c:out value="${profile.image}"/>" align="left" />
					</c:forEacn>
				</tr>
			</c:forEach></td>
	<h3>Your Twitter Account</h3>
	<ul>
		<c:forEach items="${twitter}" var="friend">
			<li><img src="<c:out value="${friend.profileImageUrl}"/>"
				align="middle" />
			<c:out value="${friend.name}" /></li>
		</c:forEach>
	</ul>
	<h3>Your LinkedIn Account</h3>
	<ul>
		<c:forEach items="${linkedIn}" var="friend">
			<li><img src="<c:out value="${friend.profilePictureUrl}"/>"
				align="middle" />
			<c:out value="${friend.firstName}" />
				<c:out value="${friend.lastName}" /></li>
		</c:forEach>
	</ul>
	<h3>Your VK Account</h3>
	<ul>
		<c:forEach items="${vkontakte}" var="friend">
			<li><img src="<c:out value="${friend.photo}"/>" align="middle" />
			<c:out value="${friend.firstName}" />
				<c:out value="${friend.lastName}" /></li>
		</c:forEach>
	</ul>
	<h3>Your Google</h3>
	<ul>
		<c:forEach items="${google}" var="friend">
			<li><img src="<c:out value="${friend.image.url}"/>"
				align="middle" />
			<c:out value="${friend.name.givenName}" />
				<c:out value="${friend.name.familyName}" /></li>
		</c:forEach>
	</ul>
</body>
</html>