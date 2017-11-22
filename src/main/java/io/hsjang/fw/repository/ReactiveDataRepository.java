package io.hsjang.fw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import io.hsjang.fw.model.Data;


@NoRepositoryBean
public interface ReactiveDataRepository extends ReactiveMongoRepository<Data, String> {

	//Mono<User> findById(String lastname);

/*	@Query("{ 'firstname': ?0, 'lastname': ?1}")
	Mono<User> findByFirstnameAndLastname(String firstname, String lastname);

	Flux<User> findByLastname(Mono<String> lastname);

	Mono<User> findByFirstnameAndLastname(Mono<String> firstname, String lastname);

	@Tailable
	Flux<User> findWithTailableCursorBy();*/
}