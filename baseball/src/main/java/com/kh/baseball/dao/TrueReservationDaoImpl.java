package com.kh.baseball.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kh.baseball.dto.SeatListDto;
import com.kh.baseball.dto.TrueReservationDto;
import com.kh.baseball.mapper.MatchInfoMapper;
import com.kh.baseball.mapper.SeatListMapper;
import com.kh.baseball.mapper.TrueReservationMapper;
import com.kh.baseball.vo.ReservationVO;

@Repository
public class TrueReservationDaoImpl implements TrueReservationDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TrueReservationMapper trueReservationMapper;
	
	@Autowired
	private MatchInfoMapper matchInfoMapper;

	@Override
	public int sequence() {
		String sql = "select reservation_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	@Override
	public void insert(TrueReservationDto trueReservationDto) {
		String sql = "INSERT INTO reservation (reservation_no,"
				+ " match_no, seat_no, "
				+ "home_team, away_team, "
				+ "member_id, seat_area_no, "
				+ "reservation_date, "
				+ "reservation_ticket) " +
	             "VALUES (reservation_seq.nextval, ?, ?, " +
	             "(SELECT team_no FROM team WHERE team_name = (SELECT home_team FROM match WHERE match_no = ?)), " +
	             "(SELECT team_no FROM team WHERE team_name = (SELECT away_team FROM match WHERE match_no = ?)), " +
	             "?, ?, sysdate, ?)";
//		String sql = "INSERT INTO reservation (reservation_no, match_no, seat_no, home_team, away_team, member_id, seat_area_no, reservation_date, reservation_ticket) " +
//	             "VALUES (reservation_seq.nextval, ?, ?, ?, ?, ?, ?, sysdate, ?)";

	Object[] data = {
	    trueReservationDto.getMatchNo(),
	    trueReservationDto.getSeatNo(),
	    trueReservationDto.getHomeTeam(),
	    trueReservationDto.getAwayTeam(),
	    trueReservationDto.getMemberId(),
	    trueReservationDto.getSeatAreaNo(),
	    trueReservationDto.getReservationTicket()
	};
		jdbcTemplate.update(sql, data);
	}

	@Override
	public List<ReservationVO> selectList(int matchNo) {
		String sql = "SELECT " +
			    "ma.match_no, " +
			    "ma.home_team, " +
			    "ma.away_team, " +
			    "st.stadium_name, " +
			    "seat_area_no, " +
			    "seat_area_price, " +
			    "st.stadium_no, " +
			    "seat_area_zone, " +
			    "match_date " +
			    "FROM " +
			    "match ma " +
			    "INNER JOIN " +
			    "stadium st ON ma.stadium_name = st.stadium_name " +
			    "INNER JOIN " +
			    "seat_area sa ON st.stadium_no = sa.stadium_no " +
			    "WHERE " +
			    "ma.match_no = ?";
		Object[] data = {matchNo};
		return jdbcTemplate.query(sql, matchInfoMapper,data);
	}
	@Autowired
	private SeatListMapper seatListMapper;
	@Override
	public List<SeatListDto> findSeatForReservation(int matchNo, int seatAreaNo) {
		String sql = "SELECT " +
			    "s.seat_no, " +
			    "s.seat_col, " +
			    "s.seat_row, " +
			    "s.SEAT_AREA_NO, " +
			    "sa.seat_area_zone, " +
			    "st.STADIUM_NAME, " +
			    "st.STADIUM_NO " +
			    "FROM " +
			    "seat s " +
			    "INNER JOIN " +
			    "seat_area sa ON s.SEAT_AREA_NO = sa.SEAT_AREA_NO " +
			    "INNER JOIN " +
			    "stadium st ON sa.STADIUM_NO = st.STADIUM_NO " +
			    "INNER JOIN " +
			    "match ma ON ma.STADIUM_name = st.STADIUM_Name " +
			    "WHERE " +
			    "match_no = ? AND seat_area_zone = ?";
		Object[] data = {matchNo, seatAreaNo};
		return jdbcTemplate.query(sql , seatListMapper,data);
	}
	
}
