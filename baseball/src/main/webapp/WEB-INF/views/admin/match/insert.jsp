<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/template/adminHeader.jsp"></jsp:include>
<h2>등록</h2>
<form action="insert" method="post" autocomplete="off">
	<div class="container w-600">
		<div class="row">
			<br>
			홈팀 선택
			<select name="homeTeam" class="w-100" id="homeTeam">
				<c:forEach var="teamDto" items="${teamList}">
					<option value="${teamDto.teamName}">${teamDto.teamName}</option>
				</c:forEach>
			</select>

			어웨이팀 선택
			<select name="awayTeam" class="w-100" id="awayTeam">
				<c:forEach var="teamDto" items="${teamList}">
					<option value="${teamDto.teamName}">${teamDto.teamName}</option>
				</c:forEach>
			</select>
			<br>
			
			경기장이름 
			<select name="stadiumName" class="w-100" id="stadiumName">
				<c:forEach var="stadiumDto" items="${stadiumList}">
					<option value="${stadiumDto.stadiumName}">${stadiumDto.stadiumName}</option>
				</c:forEach>
			</select> 
			경기일 
			<input type="date" name="matchDate"
				value="${matchDto.matchDate}"><br><br> 
			
			홈팀스코어 
			<input type="number" name="matchHomeScore"
				value="${matchDto.matchHomeScore}"><br><br>
			
			어웨이팀스코어 
			<input type="number" name="matchAwayScore"
				value="${matchDto.matchAwayScore}"><br><br>
			
			<button class="btn btn-positive">등록하기</button>
		</div>
	</div>
</form>
<jsp:include page="/WEB-INF/views/template/adminFooter.jsp"></jsp:include>