package io.hsjang.fw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.ipc.netty.http.server.HttpServer;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		//startRedirectServer();
	}
	
	public static void startRedirectServer() {
		HttpServer.create(80).start((in,out)->{
			out.status(HttpResponseStatus.TEMPORARY_REDIRECT);
			out.addHeader("Location", "https://"+in.requestHeaders().get("Host")+in.uri());
			out.addHeader("Connection", "close");
			return out.send();
		});
	}
}