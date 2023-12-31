<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/template/adminHeader.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/template/seatSidebar.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/css/list.css">

<style>

</style>

   <table class="table table-slit" border="1" width="800">
      <thead>
         <tr>
            <th>경기장</th>
            <th>구역</th>
            <th>좌석수</th>
            <th>상세</th>
            <th>수정</th>

         </tr>
      </thead>
      <tbody align="center">
         <c:forEach var="SeatGroupDto" items="${list}">
            <tr>
               <td>${SeatGroupDto.stadiumName}</td>
               <td>${SeatGroupDto.seatAreaZone}</td>
               <td>${SeatGroupDto.seatCount}</td>
               <td><a href="/admin/seat/listByZone?seatAreaZone=${SeatGroupDto.seatAreaZone}&stadiumName=${SeatGroupDto.stadiumName}">상세</a></td>
               <td><a href="/admin/reservation/update?seatAreaZone=${SeatGroupDto.seatAreaZone}&stadiumName=${SeatGroupDto.stadiumName}">수정</a></td>
            </tr>
         </c:forEach>
      </tbody>
   </table>

<jsp:include page="/WEB-INF/views/template/adminFooter.jsp"></jsp:include>