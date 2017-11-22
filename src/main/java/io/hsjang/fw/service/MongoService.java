package io.hsjang.fw.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import io.hsjang.fw.model.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MongoService {

	@Autowired
	ReactiveMongoTemplate mongo;
	
	public Flux<Data> getDatas(String cName) {
		return mongo.findAll(Data.class,cName);
	}
	
	public Mono<Data> getData(String cName, String id) {
		return mongo.findOne( new Query(Criteria.where("id").is(id)), Data.class, cName);
	}
	
	public Mono<Data> upsertData(String cName, Mono<Data> data) {
		return data.flatMap( d->
			mongo.exists( new Query(Criteria.where("id").is(d.getString("id"))), cName)
				.flatMap(yn->yn?mongo.updateFirst( new Query(Criteria.where("id").is(d.getString("id"))), BasicUpdate.fromDocument(new Document(d)), cName).map(ur->d):mongo.insert(d, cName)
				)
		);
	}
	
}
