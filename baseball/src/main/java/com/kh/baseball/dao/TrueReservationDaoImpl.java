package com.kh.baseball.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kh.baseball.dto.DeleteReservationDto;
import com.kh.baseball.dto.TrueReservationDto;
import com.kh.baseball.mapper.MatchInfoMapper;
import com.kh.baseball.mapper.ReservationVoMapper;
import com.kh.baseball.mapper.SeatListMapper;
import com.kh.baseball.mapper.SeatListVoMapper;
import com.kh.baseball.mapper.TrueReservationMapper;
import com.kh.baseball.vo.PaginationVO;
import com.kh.baseball.vo.ReservationVO;
import com.kh.baseball.vo.SeatListVO;

@Repository
public class TrueReservationDaoImpl implements TrueReservationDao {

   @Autowired
   private JdbcTemplate jdbcTemplate;

   @Autowired
   private TrueReservationMapper trueReservationMapper;

   @Autowired
   private MatchInfoMapper matchInfoMapper;

//   @Override
//   public int sequence() {
//      String sql = "select reservation_seq.nextval from dual";
//      return jdbcTemplate.queryForObject(sql, int.class);
//   }
//
//   @Override
//   public void insert(TrueReservationDto trueReservationDto) {
//      String sql = "INSERT INTO reservation (reservation_no," + " match_no, seat_no, " + "home_team, away_team, "
//            + "member_id, seat_area_no, " + "reservation_date, " + "reservation_ticket) "
//            + "VALUES (reservation_seq.nextval, ?, ?, "
//            + "(SELECT team_no FROM team WHERE team_name = (SELECT home_team FROM match WHERE match_no = ?)), "
//            + "(SELECT team_no FROM team WHERE team_name = (SELECT away_team FROM match WHERE match_no = ?)), "
//            + "?, ?, sysdate, ?)";
////      String sql = "INSERT INTO reservation (reservation_no, match_no, seat_no, home_team, away_team, member_id, seat_area_no, reservation_date, reservation_ticket) " +
////                "VALUES (reservation_seq.nextval, ?, ?, ?, ?, ?, ?, sysdate, ?)";
//
//      Object[] data = { trueReservationDto.getMatchNo(), trueReservationDto.getSeatNo(),
//            trueReservationDto.getHomeTeam(), trueReservationDto.getAwayTeam(), trueReservationDto.getMemberId(),
//            trueReservationDto.getSeatAreaNo(), trueReservationDto.getReservationTicket() };
//      jdbcTemplate.update(sql, data);
//   }
   @Override
   public void insert(TrueReservationDto trueReservationDto) {
       String sql = "INSERT INTO reservation (reservation_no, match_no, seat_no, home_team, away_team, member_id, seat_area_no, reservation_date, reservation_ticket) " +
               "VALUES (reservation_seq.nextval, ?, ?, " +
               "(SELECT team_no FROM team WHERE team_name = (SELECT home_team FROM match WHERE match_no = ?)), " +
               "(SELECT team_no FROM team WHERE team_name = (SELECT away_team FROM match WHERE match_no = ?)), " +
               "?, ?, sysdate, ?)";

       int[] seatNos = trueReservationDto.getSeatNo(); // seatNo를 int 배열로 받아옴
       int reservationTicket = trueReservationDto.getReservationTicket(); // 예약 티켓 수를 가져옴

       for (int i = 0; i < reservationTicket; i++) {
           Object[] data = { trueReservationDto.getMatchNo(), seatNos[i], // reservationTicket 수만큼 seatNo를 반복해서 설정
                   trueReservationDto.getHomeTeam(), trueReservationDto.getAwayTeam(), trueReservationDto.getMemberId(),
                   trueReservationDto.getSeatAreaNo(), 1 }; // 1은 예약 티켓 수를 나타냄

           jdbcTemplate.update(sql, data);
       }
   }
//   @Override
//   public void insert(TrueReservationDto trueReservationDto) {
//       String sql = "INSERT INTO reservation (reservation_no, match_no, seat_no, home_team, away_team, member_id, seat_area_no, reservation_date, reservation_ticket) " +
//                    "VALUES (reservation_seq.nextval, ?, ?, (SELECT team_no FROM team WHERE team_name = (SELECT home_team FROM match WHERE match_no = ?)), " +
//                    "(SELECT team_no FROM team WHERE team_name = (SELECT away_team FROM match WHERE match_no = ?)), ?, ?, sysdate, 1)";
//
//           Object[] data = {
//               trueReservationDto.getMatchNo(),
//               trueReservationDto.getSeatNo(),
//               trueReservationDto.getMatchNo(),
//               trueReservationDto.getMatchNo(),
//               trueReservationDto.getMemberId(),
//               trueReservationDto.getSeatAreaNo()
//           };
//           jdbcTemplate.update(sql, data);
//   
//   }

   @Override
   public List<ReservationVO> selectList(int matchNo) {
      String sql = "SELECT " + "ma.match_no, " + "ma.home_team, " + "ma.away_team, " + "st.stadium_name, "
            + "seat_area_no, " + "seat_area_price, " + "st.stadium_no, " + "seat_area_zone, " + "match_date "
            + "FROM " + "match ma " + "INNER JOIN " + "stadium st ON ma.stadium_name = st.stadium_name "
            + "INNER JOIN " + "seat_area sa ON st.stadium_no = sa.stadium_no " + "WHERE " + "ma.match_no = ?";
      Object[] data = { matchNo };
      return jdbcTemplate.query(sql, matchInfoMapper, data);
      
   }

   @Autowired
   private SeatListVoMapper seatListVoMapper;


      
   public List<SeatListVO> findSeatForReservation(int matchNo, int seatAreaNo) {
      String sql = "SELECT " +
                "ma.MATCH_NO, " +
                "s.seat_no, " +
                "s.seat_row, " +
                "s.seat_col, " +
                "s.SEAT_AREA_NO, " +
                "sa.seat_area_zone, " +
                "st.STADIUM_NAME, " +
                "st.STADIUM_NO, " +
                "sa.seat_area_price, " +
                "rs.reservation_no,"
                + "s.seat_status " +
                "FROM seat s " +
                "LEFT OUTER JOIN seat_area sa ON s.SEAT_AREA_NO = sa.SEAT_AREA_NO " +
                "LEFT OUTER JOIN stadium st ON sa.STADIUM_NO = st.STADIUM_NO " +
                "LEFT OUTER JOIN match ma ON ma.STADIUM_name = st.STADIUM_Name " +
                "LEFT OUTER JOIN reservation rs ON (rs.SEAT_NO = s.seat_no AND rs.MATCH_NO = ma.MATCH_NO) " +
                "WHERE ma.match_no = ? AND sa.seat_area_no = ?" +
                "order by s.seat_no asc";
      Object[] data = { matchNo, seatAreaNo };
      return jdbcTemplate.query(sql, seatListVoMapper, data);
   }

   @Override
   public boolean seatStatusUpdate(TrueReservationDto trueReservationDto) {
      String sql = "update seat set seat_status = case when seat_status ='Y' then 'N' else 'Y' end where seat_no = ?";
      
       int[] seatNos = trueReservationDto.getSeatNo(); // seatNo를 int 배열로 받아옴
       int reservationTicket = seatNos.length; // 예약 티켓 수를 가져옴

       int updatedCount = 0; // 업데이트된 행의 수를 카운트

       for (int i = 0; i < reservationTicket; i++) {
           Object[] data = {seatNos[i] };
           int updatedRows = jdbcTemplate.update(sql, data);
           updatedCount += updatedRows;
       }

       // 모든 업데이트가 성공했을 때 true를 반환
       return updatedCount == reservationTicket;
   }
   
   //회원별 예매 리스트
   @Autowired
   private ReservationVoMapper reservationVoMapper;
   @Override
   public List<ReservationVO> reservationList(PaginationVO vo,String memberId) {
      String sql = "select * from ("
            + "select rownum rn, TMP.* from ("
            + "select * from reservation_Vo where member_id =? order by reservation_no desc "
         + ")TMP"
      + ") where rn between ? and ?";
      
      return jdbcTemplate.query(sql, reservationVoMapper, memberId, vo.getStartRow(), vo.getFinishRow());
   }

   
   //예매 상세
   @Override
   public ReservationVO reservationSelectOne(int reservationNo) {
      String sql = "select * from reservation_vo where reservation_no = ?";
      List<ReservationVO> list = jdbcTemplate.query(sql, reservationVoMapper,reservationNo);
      
      return list.isEmpty() ? null:list.get(0);
      
      
      
   }

   @Override
   public boolean reservationDeleteByTicket(DeleteReservationDto deleteReservationDto) {
      String sql = "delete from reservation where reservation_no = ?";
      
       int[] seatNos = deleteReservationDto.getReservationNo(); // seatNo를 int 배열로 받아옴
       int reservationTicket = seatNos.length; // 예약 티켓 수를 가져옴

       int updatedCount = 0; // 업데이트된 행의 수를 카운트

       for (int i = 0; i < reservationTicket; i++) {
           Object[] data = {seatNos[i] };
           int updatedRows = jdbcTemplate.update(sql, data);
           updatedCount += updatedRows;
       }

       // 모든 업데이트가 성공했을 때 true를 반환
       return updatedCount == reservationTicket;
   }

//   @Override
//   public void reservationCancelInsertBySeatNo(ReservationCancelDto reservationCancelDto) {
//      String sql = "INSERT INTO reservation_cancel (reservation_cancel_no, reservation_no, match_no, reservation_cancel_time, seat_no, member_id) " +
//                "VALUES (reservation_cancel_seq.nextval, " +
//                "(SELECT reservation_no FROM reservation WHERE seat_no = ? and match_no =(SELECT match_no FROM reservation WHERE seat_no = ?)), " +
//                "(SELECT match_no FROM reservation WHERE seat_no = ? and match_no =(SELECT match_no FROM reservation WHERE seat_no = ?)), " +
//                "SYSDATE, ?, ?)";
//       
//       int[] seatNos = reservationCancelDto.getSeatNo(); 
//       int reservationTicket = seatNos.length; 
//
//       for (int i = 0; i < reservationTicket; i++) {
//           Object[] data = {seatNos[i], seatNos[i],seatNos[i], reservationCancelDto.getMemberId()};
//
//           jdbcTemplate.update(sql, data);
//       }
//   }
   


   @Override
   public int countList(PaginationVO vo, String memberId) {
      String sql = "select count(*) from reservation where member_id = ?";
      return jdbcTemplate.queryForObject(sql, int.class,memberId);
   }

   @Override
   public ReservationVO selectOneTeam(int matchNo) {
      String sql = "SELECT " + "ma.match_no, " + "ma.home_team, " + "ma.away_team, " + "st.stadium_name, "
            + "seat_area_no, " + "seat_area_price, " + "st.stadium_no, " + "seat_area_zone, " + "match_date "
            + "FROM " + "match ma " + "INNER JOIN " + "stadium st ON ma.stadium_name = st.stadium_name "
            + "INNER JOIN " + "seat_area sa ON st.stadium_no = sa.stadium_no " + "WHERE " + "ma.match_no = ?";
      Object[] data = { matchNo };
      
      List<ReservationVO> list =jdbcTemplate.query(sql, matchInfoMapper, data);
      return list.isEmpty()? null:list.get(0);
   }
   }
   
   