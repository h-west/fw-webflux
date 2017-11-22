package io.hsjang.fw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

	/*
	 * 
	 * token : ReactiveSecurityContextHolder.getContext()
	 * 
	 * 
	 */

	@GetMapping(value = { "", "page" })
	public String main() {
		return "main";
	}

	@GetMapping("page/{page}")
	public String page(@PathVariable String page) {
		return "page/"+page;
	}
}