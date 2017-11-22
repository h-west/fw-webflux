package io.hsjang.fw.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
// @EnableReactiveMethodSecurity
public class TodoSecurityConfig implements ReactiveAuthenticationManager, ServerSecurityContextRepository {

	public static final String TODO_SECURITY_CONTEXT = "TodoSecurityContext";

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {

		AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(this);
		authenticationFilter.setServerSecurityContextRepository(this);
		authenticationFilter.setAuthenticationConverter(ex -> {
			String authorization;
			HttpCookie tt = ex.getRequest().getCookies().getFirst("Todo-X-Auth");
			authorization = tt != null ? tt.getValue() : ex.getRequest().getHeaders().getFirst("Todo-X-Auth");
			return Mono.just(
					authorization == null || authorization.isEmpty() ? new TodoToken() : new TodoToken(authorization));
		});

		return http
				.authenticationManager(this)
				.securityContextRepository(this)
				.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.authorizeExchange().pathMatchers("/*", "/api/public/**", "/resources/**").permitAll().and()
				.authorizeExchange().pathMatchers("/admin", "/api/admin/**").hasRole("ADMIN").and()
				.authorizeExchange().anyExchange().hasRole("USER").and()
				.csrf().disable()
				.exceptionHandling().serverAuthenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/login")).and()
			.build();
	}

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.just(authentication);
	}

	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		exchange.getAttributes().put(TODO_SECURITY_CONTEXT, context);
		return Mono.empty();
	}

	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		return Mono.just((SecurityContext) exchange.getAttributes().get(TODO_SECURITY_CONTEXT));
	}
}