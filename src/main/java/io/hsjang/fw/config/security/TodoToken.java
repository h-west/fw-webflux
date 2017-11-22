package io.hsjang.fw.config.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.hsjang.fw.model.Data;

public class TodoToken implements Authentication, CredentialsContainer {

	private static final long serialVersionUID = 1L;
	private static final String SECRET = "rainT2&%";
	
	private String credentials;
	private String principal;
	private Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	private Data details;
	private boolean authenticated = false;
	
	private static ObjectMapper mapper;
	private static Collection<String> userAuth;
	static {
		mapper = new ObjectMapper();
		userAuth = new HashSet<String>();
		userAuth.add("USER");
	}
	
	public TodoToken() {
	}
	
	public static String toToken(Data user) {
		try {
			return  JwtHelper.encode(mapper.writeValueAsString( 
					new Data("id",user.getString("id"))
					.add("name", user.getString("name"))
					.add("image", user.getString("image"))
					.add("createdAt", new Date().getTime())
					.add("roles", userAuth) ), new MacSigner(SECRET)).getEncoded();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public TodoToken(String token) {
		Jwt jwt = JwtHelper.decodeAndVerify(token, new MacSigner(SECRET));
		
		try {
			details = mapper.readValue(jwt.getClaims(), Data.class);
			principal = details.getString("id");
			//((List<String>) details.get("roles")).stream().forEach(v-> authorities.add(()->{return "ROLE_"+v.toUpperCase();}));
			for(String role : (List<String>) details.get("roles")) {
				authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));
			}
			this.credentials = token;
			this.authenticated = true;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getName() {
		return details.getString("name");
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Object getDetails() {
		return this.details;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}


	@Override
	public void eraseCredentials() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}


	@Override
	public Object getPrincipal() {
		return this.principal;
	}


	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}


}