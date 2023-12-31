package com.kh.baseball.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.baseball.dto.MatchDto;
import com.kh.baseball.mapper.MatchMapper;
import com.kh.baseball.mapper.MatchVoMapper;
import com.kh.baseball.vo.MatchVO;
import com.kh.baseball.vo.PaginationVO;

@Repository
public class MatchDaoImpl implements MatchDao{

	
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MatchMapper matchMapper;
	
	@Autowired
	private MatchVoMapper matchVoMapper;
	
	@Override
	public int sequence() {
		String sql ="select match_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	@Override
	public void insertMatch(MatchDto matchDto) {
		
		String sql = "insert into match"
				+ "(match_no, home_team, "
				+ "away_team, stadium_name, "
				+ "match_date) "
				+ "values(?, ?, ?, ?, ?)";
	    Object[] data = {
				matchDto.getMatchNo(), matchDto.getHomeTeam(),
				matchDto.getAwayTeam(),matchDto.getStadiumName(),
				matchDto.getMatchDate()
			};
		jdbcTemplate.update(sql,data);
	}

	@Override
	public boolean delete(int matchNo) {
		String sql = "delete match where match_no = ?";
		Object[] data = {matchNo} ;
		return jdbcTemplate.update(sql, data)>0;
	}

	@Override
	public boolean update(MatchDto matchDto) {
		String sql ="update match set match_home_score = ?, match_away_score = ? where match_no = ?";
		Object[] data = {matchDto.getMatchHomeScore(), matchDto.getMatchAwayScore(), matchDto.getMatchNo()} ;
		return jdbcTemplate.update(sql,data) > 0;
	}
	
	@Override
	public boolean updateDate(MatchDto matchDto) {
		String sql ="update match set match_date = ? where match_no = ?";
		Object[] data = {matchDto.getMatchDate(), matchDto.getMatchNo()} ;
		return jdbcTemplate.update(sql,data) > 0;
	}

	@Override
	public MatchDto selectOne(int matchNo) {
		String sql = "select * from match where match_no = ?";
		Object[] data = {matchNo} ; 
		List<MatchDto> list = jdbcTemplate.query(sql, matchMapper, data);
		return list.isEmpty() ? null : list.get(0);
	}
	
	@Override
	public MatchVO selectOneReservation(int matchNo) {
		String sql = "select * from match where match_no = ?";
		Object[] data = {matchNo} ; 
		List<MatchVO> list = jdbcTemplate.query(sql, matchVoMapper, data);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<MatchDto> selectList(PaginationVO vo) {
		String sql ="select *from("
				+ "			select rownum rn, TMP.* from("
				+ "					select * from match order by match_date desc"
				+ "		)TMP"
				+ "		)where rn BETWEEN ? and ?";
		return jdbcTemplate.query(sql, matchMapper, vo.getStartRow(), vo.getFinishRow());
	}
	
	
	@Override
	public List<MatchVO> selectNoList() {
		String sql = "SELECT " +
	             " t1.TEAM_NO AS home_team_no, " +
	             " t2.TEAM_NO AS away_team_no, " +
	             " ma.* " +
	             "FROM " +
	             " match ma " +
	             "LEFT OUTER JOIN " +
	             " team t1 ON ma.home_team = t1.team_name " +
	             "LEFT OUTER JOIN " +
	             " team t2 ON ma.away_team = t2.team_name " +
	             "ORDER BY ma.match_date DESC";
		return jdbcTemplate.query(sql, matchVoMapper);
	}
	
	@Override
	public boolean checkDuplicate(MatchDto matchDto, @RequestParam String subStrMatchDate) {
		String sql = "SELECT * FROM MATCH " +
	             "WHERE " +
	             "  (home_team IN (?, ?) OR away_team IN (?, ?) OR stadium_name = ?) " +
	             "  AND to_char(match_date, 'YYYY-MM-DD') = ?";
		Object[] data = {matchDto.getHomeTeam(), matchDto.getAwayTeam(),
				matchDto.getAwayTeam(), matchDto.getHomeTeam(),
				matchDto.getStadiumName(), subStrMatchDate};
		return jdbcTemplate.update(sql,data) > 0;
	}

	@Override
	public boolean seatStatusUpdateByMatchFinish(int matchNo) {
		String sql = "UPDATE SEAT " +
	             "SET SEAT_STATUS = 'Y' " +
	             "WHERE SEAT_NO IN (" +
	             "    SELECT s.SEAT_NO " +
	             "    FROM SEAT s " +
	             "    INNER JOIN SEAT_AREA sa ON s.SEAT_AREA_NO = sa.SEAT_AREA_NO " +
	             "    INNER JOIN STADIUM st ON st.STADIUM_NO = sa.STADIUM_NO " +
	             "    INNER JOIN MATCH ma ON ma.STADIUM_NAME = st.STADIUM_NAME " +
	             "    WHERE ma.MATCH_NO = ?" +
	             ")";
		return jdbcTemplate.update(sql,matchNo)>0;
	}

	@Override
	public int countList(PaginationVO vo) {
		String sql ="select count(*) from match";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}

}
