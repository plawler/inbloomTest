<!doctype html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Spring MVC Application</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="http://twitter.github.io/bootstrap/assets/css/bootstrap.css" rel="stylesheet">
    <link href="http://twitter.github.io/bootstrap/assets/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<body>
<h1>Student</h1>

<p>${student.fullName}</p>

<c:forEach items="${student.addresses}" var="address">
    <p>
        ${address.streetNumberName} ${address.apartmentRoomSuiteNumber}<br/>
        ${address.city}, ${address.stateAbbreviation} ${address.postalCode}
    </p>
</c:forEach>

</body>

</html>