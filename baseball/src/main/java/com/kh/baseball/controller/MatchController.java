package com.kh.baseball.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.baseball.dao.MatchDao;
import com.kh.baseball.dao.StadiumDao;
import com.kh.baseball.dao.TeamDao;
import com.kh.baseball.dto.MatchDto;
import com.kh.baseball.dto.StadiumDto;
import com.kh.baseball.dto.TeamDto;
@Repository
//잠시 admin 뺐음
@RequestMapping("/match")
public class MatchController {
	
	@Autowired 
	private MatchDao matchDao;
	
	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private StadiumDao stadiumDao;
	
	
	@GetMapping("/insert")
	public String insert(@ModelAttribute TeamDto teamDto, StadiumDto stadiumDto, Model model) {
		List<TeamDto> teamList = teamDao.selectList();
		List<StadiumDto> stadiumList = stadiumDao.selectList();
		model.addAttribute("teamList", teamList);
		model.addAttribute("stadiumList", stadiumList);
		
		return "/WEB-INF/views/admin/match/insert.jsp";
	}
	@PostMapping("/insert")
	public String insert(@ModelAttribute MatchDto matchDto, @RequestParam("matchDto.matchDateStr") String matchDateStr) {
		int matchNo = matchDao.sequence();
		matchDto.setMatchNo(matchNo);
		
	    // matchDateStr을 Timestamp로 변환
	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	        Date parsedDate = dateFormat.parse(matchDateStr);

	        // java.util.Date를 java.sql.Timestamp으로 변환
	        java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

	        matchDto.setMatchDate(timestamp);
	    } catch (ParseException e) {
	        // 예외 처리 로직 추가
	    }
		
		matchDao.insertMatch(matchDto);

		return "redirect:detailMatch?matchNo="+matchNo;
	}
	
	@GetMapping("/updateDate")
	public String updateDate(Model model, @RequestParam int matchNo) {
		MatchDto matchDto = matchDao.selectOne(matchNo);
		model.addAttribute("matchDto", matchDto);
		
		return "/WEB-INF/views/admin/match/updateDate.jsp";
	}
	
	@PostMapping("/updateDate")
	public String updateDate(@ModelAttribute MatchDto matchDto) {
		matchDao.updateDate(matchDto);
		return "redirect:detailMatch?matchNo="+matchDto.getMatchNo();
	}
	
	@GetMapping("/insertResult")
	public String insertResult(Model model,
			@RequestParam int matchNo) {
		MatchDto matchDto = matchDao.selectOne(matchNo);
		model.addAttribute("matchDto", matchDto);
		
		return "/WEB-INF/views/admin/match/insertResult.jsp";
	}
	
	@PostMapping("/insertResult")
	public String insertResult(@ModelAttribute MatchDto matchDto) {
		matchDao.update(matchDto);
		
		int matchNo = matchDto.getMatchNo();
		matchDto = matchDao.selectOne(matchNo);
		String homeTeam = matchDto.getHomeTeam();
		String awayTeam = matchDto.getAwayTeam();
		int matchHomeScore = matchDto.getMatchHomeScore();
		int matchAwayScore = matchDto.getMatchAwayScore();
		
		if (matchHomeScore > matchAwayScore) {
			teamDao.updateWin(homeTeam);
			teamDao.updateLose(awayTeam);
		}
		else if (matchHomeScore < matchAwayScore) {
			teamDao.updateWin(awayTeam);
			teamDao.updateLose(homeTeam);
		}
		else {
			teamDao.updateDraw(homeTeam);
			teamDao.updateDraw(awayTeam);
		}
		teamDao.updateWinRate(homeTeam);
		teamDao.updateWinRate(awayTeam);
		teamDao.updateGameGap();

		return "redirect:detailMatchResult?matchNo="+matchNo;
	}
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<MatchDto> list = matchDao.selectList();
		model.addAttribute("list",list);
		return "/WEB-INF/views/match/list.jsp";				
	}
	

	@RequestMapping("/detailMatch")
	public String detailMatch(@RequestParam int matchNo, Model model) {
		MatchDto matchDto = matchDao.selectOne(matchNo);
		model.addAttribute("matchDto", matchDto);
		return "/WEB-INF/views/admin/match/detailMatch.jsp";
	}
	
	@RequestMapping("/detailMatchResult")
	public String detailMatchResult(@RequestParam int matchNo, Model model) {
		MatchDto matchDto = matchDao.selectOne(matchNo);
		model.addAttribute("matchDto", matchDto);
		return "/WEB-INF/views/admin/match/detailMatchResult.jsp";
	}
	
}
