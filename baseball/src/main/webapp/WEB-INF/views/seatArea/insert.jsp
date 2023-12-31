<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<script>
	
</script>

<form action="insert" method="post">
	<div class="container w-300">
		<div class="row left">
			경기장: <select class="form-input w-100" name="stadiumNo" class="w-100" id="stadiumNo">
				<c:forEach var="stadiumDto" items="${list}">
					<option value="${stadiumDto.stadiumNo}">${stadiumDto.stadiumName}</option>
				</c:forEach>
			</select> 구역: <select name="seatAreaZone" class="w-100">
				<option value="A">A</option>
				<option value="B">B</option>
				<option value="C">C</option>
			</select> 좌석가격: <input type="number" name="seatAreaPrice"
				value="${seatAreaDto.seatAreaPrice}" class="w-100"> <br>
			<br>
		</div>
		<div class="row w-100">
			<button type="submit" class="btn btn-positive w-100">등록</button>
		</div>
	</div>
</form>

<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>