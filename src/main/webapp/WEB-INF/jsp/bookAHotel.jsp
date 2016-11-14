<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html xmlns:th="http://www.thymeleaf.org">
<head>   
	<title>Person Page</title>
	<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
</head>
<body ng-app="HotelApp">
<h1>
	HOTEL BOOKING
</h1>

<%-- <form:form action="/bookAHotel" commandName="bookAHotel"> --%>
<table>
	<%-- <tr>
		<td>
			City <form:input path="city" size="8" />
		</td> 
	</tr> --%>
	<div>
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>Id</th>
					<th>Hotel Name</th>
					<th>City</th>
					<th>No of rooms available</th>
					<th>Price</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="details in ${listOfHotels} | filter:SearchBox">
					<td>{{details.id}}</td>
					<td>{{details.hotelName}}</td>
					<td>{{details.city}}</td>
					<td>{{details.noOfRoomsAvailable}}</td>
					<td>{{details.price}}</td>
				</tr>
			</tbody>
		</table> 
	</div>
</table>	
<%-- </form:form> --%>
<br>
</body>
</html>
