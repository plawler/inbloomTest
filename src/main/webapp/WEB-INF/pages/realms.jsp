<!doctype html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Realm Selection</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="http://twitter.github.io/bootstrap/assets/css/bootstrap.css" rel="stylesheet">
    <link href="http://twitter.github.io/bootstrap/assets/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<c:url value="/realms" var="realmSelectionUrl"/>

<body>

<h1>Select your realm</h1>

<form:form action="${realmSelectionUrl}" modelAttribute="realmSelection" id="realmForm" method="post" cssClass="form-vertical" enctype="application/x-www-form-urlencoded">

    <form:select path="realmIdentifier" cssClass="input-xlarge">
        <form:option value="" label="Choose..."/>
        <form:options items="${realms}" itemLabel="name" itemValue="identifier"/>
    </form:select>

    <button type="submit" class="btn btn-primary">Save</button>

</form:form>

</body>
</html>