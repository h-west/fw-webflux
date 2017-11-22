package io.hsjang.fw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.hsjang.fw.config.security.TodoToken;
import io.hsjang.fw.model.Data;
import io.hsjang.fw.model.Result;
import io.hsjang.fw.service.LoginService;
import io.hsjang.fw.service.MongoService;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	MongoService mongoService;

	@Autowired
	LoginService loginService;

	@PostMapping(value = "", consumes = "application/json")
	@ResponseBody
	public Mono<Result> addUserData(@RequestBody Mono<Data> data) {
		return mongoService.upsertData("user", loginService.getUserFromGoogleAuth(data))
				.map(d -> new Result(d).add("token", TodoToken.toToken(d)));
	}

	@GetMapping("")
	public String login() {
		return "login";
	}
}