package io.hsjang.fw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.hsjang.fw.model.Data;
import io.hsjang.fw.service.MongoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/data")
public class DataController {

	@Autowired
	MongoService mongoService;
	
	@GetMapping("/{cName}/s")
	@ResponseBody public Flux<Data> getDatas(@PathVariable String cName) {
		return mongoService.getDatas(cName);
	}
	
	@GetMapping("/{cName}/{id}")
	@ResponseBody public Mono<Data> getData(@PathVariable String cName, @PathVariable String id) {
		return mongoService.getData(cName, id);
	}
	
	@PostMapping(value="/{cName}", consumes = "application/json")
	@ResponseBody public Mono<Data> addData(@PathVariable String cName, @RequestBody Mono<Data> data) {
		return mongoService.upsertData(cName, data);
	}
}
