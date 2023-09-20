package com.kh.baseball.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ReservationDto {
	private int reservationNo;
	private int matchNo;
	private int seatNo;
	private String homeTeam, awayTeam;
	private String memberId;
	private Date reservationDate;
	
}
