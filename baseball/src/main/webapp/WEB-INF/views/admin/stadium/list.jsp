<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/template/adminHeader.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/template/stadiumSidebar.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/css/list.css">

<table width="800">
	<thead>
		<tr>
			<th>경기장 이름</th>
		</tr>
	</thead>
	<tbody align="center">
		<!--<c:forEach var="stadiumDto" items="${list}">-->
		<tr>
			<td><a href="detail?stadiumNo=${stadiumDto.stadiumNo}" class="link">${stadiumDto.stadiumName}</a></td>
		</tr>
		<!--</c:forEach>-->
	</tbody>
</table>
<jsp:include page="/WEB-INF/views/template/adminFooter.jsp"></jsp:include>
    