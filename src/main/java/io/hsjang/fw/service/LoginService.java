package io.hsjang.fw.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.hsjang.fw.exception.AuthException;
import io.hsjang.fw.model.Data;
import reactor.core.publisher.Mono;


@Service
public class LoginService implements InitializingBean{

	/**GOOGLE*/
	GoogleIdTokenVerifier googleVerifier;
	JacksonFactory jacksonFactory;
	String GOOGLE_CLIENT_ID = "375628126693-5vpf7j7bm35h47qg1jt30ejdbq5ja09b.apps.googleusercontent.com";
	String GOOGLE_INDICATOR = "G-";
	
	public Mono<Data> getUserFromGoogleAuth(Mono<Data> authResponse) {
		return authResponse.map(auth->{
			String idToken = auth.getString("id_token");
			if(!"".equals(idToken)) {
				try {
					GoogleIdToken token = GoogleIdToken.parse(jacksonFactory, idToken);
					if(googleVerifier.verify(token)) {
						//{"at_hash":"klZ1bSbJc52ZwQUB6mrVtg","aud":"375628126693-5vpf7j7bm35h47qg1jt30ejdbq5ja09b.apps.googleusercontent.com","azp":"375628126693-5vpf7j7bm35h47qg1jt30ejdbq5ja09b.apps.googleusercontent.com","email":"aphs738@gmail.com","email_verified":true,"exp":1510912690,"iat":1510909090,"iss":"accounts.google.com","jti":"c192e0b0767bb26ef1f09672925f079374af12ed","sub":"116528260298992149037","name":"Jang hyunseo","picture":"https://lh4.googleusercontent.com/-NCtxHgyqKr8/AAAAAAAAAAI/AAAAAAAAAAA/ANQ0kf6XzDpMXZU5dKV0JloTRtRPrB1sJw/s96-c/photo.jpg","given_name":"Hyunseo","family_name":"Jang","locale":"ko"}
						Payload payload = token.getPayload();
						Data user = new  Data();
						user.put("id", GOOGLE_INDICATOR + payload.getSubject());
						user.put("name", payload.get("name"));
						user.put("image", payload.get("picture"));
						user.put("authResponse", auth);
						return user;
					}else {
						Mono.error(new AuthException("invalid token"));
					}
				} catch (GeneralSecurityException | IOException e) {
					Mono.error(new AuthException("invalid token"));
					e.printStackTrace();
				}
			}
			return null;
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		jacksonFactory = new JacksonFactory();
		googleVerifier = new GoogleIdTokenVerifier
				.Builder(new NetHttpTransport(), jacksonFactory)
				.setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
				.build();
	}
}
