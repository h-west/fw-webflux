package io.hsjang.fw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.hsjang.fw.model.Data;
import io.hsjang.fw.service.MongoService;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	MongoService mongoService;
	
	@GetMapping("/me")
	@ResponseBody public Mono<Data> getData() {
		return ReactiveSecurityContextHolder.getContext().flatMap(context->
			mongoService.getData("user", context.getAuthentication().getPrincipal().toString())
		);
		
	}
	
}
