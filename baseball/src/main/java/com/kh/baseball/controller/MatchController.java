package com.kh.baseball.controller;

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
import com.kh.baseball.dto.MatchDto;
@Repository
@RequestMapping("/match")
public class MatchController {
	@Autowired 
	private MatchDao matchDao;
	
	
	@GetMapping("/insert")
	public String insert() {
		return "/WEB-INF/views/match/insert.jsp";
	}
	@PostMapping("/insert")
	public String insert(@ModelAttribute MatchDto matchDto) {
		int matchNo = matchDao.sequence();
		matchDto.setMatchNo(matchNo);
		
		matchDao.insert(matchDto);
		return "redirect:list";
	}
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<MatchDto> list = matchDao.selectList();
		model.addAttribute("list",list);
		return "/WEB-INF/views/match/list.jsp";				
	}

	@RequestMapping("/detail")
	public String detail(@RequestParam int matchNo, Model model) {
		MatchDto matchDto = matchDao.selectOne(matchNo);
		model.addAttribute("matchDto", matchDto);
		return "/WEB-INF/views/match/detail.jsp";
	}
	
	@GetMapping("/change")
	public String change(Model model, @RequestParam int matchNo) {
		MatchDto matchDto = matchDao.selectOne(matchNo);
		model.addAttribute("matchDto", matchDto);
		return "/WEB-INF/views/match/change.jsp";
	}
	
	@PostMapping("/change")
	public String change(@ModelAttribute MatchDto matchDto) {
		boolean result = matchDao.update(matchDto);
		if(result) {
			return "redirect:detail?matchNo="+matchDto.getMatchNo();
		}
		else {
			return "redirecr:error";
		}
	}
}
