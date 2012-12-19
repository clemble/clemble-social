<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Sign In</title>
</head>
<body>
	<h2>Google</h2>
	<form action="<c:url value="/signin/google" />" method="POST">
		<button type="submit">Sign in with Google</button>
		<input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo#email https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/tasks https://www-opensocial.googleusercontent.com/api/people" />
	</form>
	<h2>Facebook</h2>
	<form action="<c:url value="/signin/facebook" />" method="POST">
		<button type="submit">Sign in with Facebook</button>
		<input type="hidden" name="scope"
			value="email,user_about_me,user_about_me,friends_about_me,friends_activities,user_activities,user_birthday,friends_birthday,user_checkins,friends_checkins,user_education_history,friends_education_history,user_events,friends_events,user_groups,friends_groups,user_hometown,friends_hometown,user_interests,friends_interests,user_likes,friends_likes,user_location,friends_location,user_notes,friends_notes,user_photos,friends_photos,user_questions,friends_questions,user_relationships,friends_relationships,user_religion_politics,friends_religion_politics,user_status,friends_status,user_subscriptions,friends_subscriptions,user_videos,friends_videos,user_website,friends_website,user_work_history,friends_work_history,manage_pages,read_friendlists,read_insights,read_mailbox,read_requests,read_stream,xmpp_login,ads_management,create_event,manage_friendlists,manage_notifications,user_online_presence,friends_online_presence,publish_checkins,publish_stream,rsvp_event,publish_actions,user_actions.music,friends_actions.music,user_actions.news,friends_actions.news,user_actions.video,friends_actions.video,user_games_activity,friends_games_activity" />
	</form>
	<h2>Twitter</h2>
	<form action="<c:url value="/signin/twitter" />" method="POST">
		<button type="submit">Sign in with Twitter</button>
		<input type="hidden" name="scope"
			value="email,publish_stream,offline_access" />
	</form>
	<h2>LinkedIn</h2>
	<form action="<c:url value="/signin/linkedin" />" method="POST">
		<button type="submit">Sign in with LinkedIn</button>
		<input type="hidden" name="scope"
			value="email,publish_stream,offline_access" />
	</form>
	<h2>VKontakte</h2>
	<form action="<c:url value="/signin/vkontakte" />" method="POST">
		<button type="submit">Sign in with VK</button>
		<input type="hidden" name="scope"
			value="friends,photos,audio,video,questions,wall,groups,notifications,stats,offline" />
	</form>
</body>
</html>
