package com.kh.baseball.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.baseball.dao.SeatAreaDao;
import com.kh.baseball.dao.SeatDao;
import com.kh.baseball.dto.SeatAreaDto;
import com.kh.baseball.dto.SeatDto;

@Controller
@RequestMapping("/seat")
public class SeatController {
	
	@Autowired
	private SeatDao seatDao;
	
	@Autowired
	private SeatAreaDao seatAreaDao;
	
	@RequestMapping("/list")
	public String insert(@ModelAttribute SeatDto seatDto, Model model) {
		List<SeatDto> list = seatDao.selectList();
		model.addAttribute("list", list);
		return "/WEB-INF/views/seat/list.jsp";
	}
	
	@GetMapping("/insert")
	public String insert(@ModelAttribute SeatAreaDto seatAreaDto ,Model model) {
		List<SeatAreaDto> list = seatAreaDao.selectList();
		model.addAttribute("list",list);
		return "/WEB-INF/views/seat/insert.jsp";
	}
	@PostMapping("/insert")
	public String insert(SeatDto seatDto) {
		// int seatNo = seatDao.sequenceSeat();
		int seatNo = seatDao.sequenceSeat();
		//seatAreaNo 를 가져오려면 selectOne 으로 값을 가져온다
		//seatdto 에 저장을 해야하는데 
		seatDto.setSeatNo(seatNo);
		
		seatDao.insert(seatDto);
		return "/WEB-INF/views/seat/insertFinish.jsp";
	}
//	
	
//	@RequestMapping("/list")
//	public String list(@RequestParam int seatId) {
//		
//	}
	
}
