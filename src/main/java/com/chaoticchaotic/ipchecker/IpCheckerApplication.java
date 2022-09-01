package com.chaoticchaotic.ipchecker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;


import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EnableFeignClients
@SpringBootApplication
public class IpCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpCheckerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ProxyCheckClient client) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("some.txt"));


		StringBuffer adv = new StringBuffer();

		List<List<String>> collect = reader.lines().distinct()
				.map(client::getCheck)
				.map(HttpEntity::getBody)
				.filter(Objects::nonNull)
				.map(Object::toString)
				.map(s -> s.substring(12))
				.map(s -> Arrays.stream(s.split(",")).filter(substring -> substring.contains("asn") || substring.contains("country") || substring.contains("region=")
								|| substring.contains("city") || substring.contains("vpn") || substring.contains("proxy"))
						.map(substring -> substring.replace("{", ""))
						.map(substring -> substring.replace("}", ""))
						.collect(Collectors.toList()).stream().map(string -> {
							if(string.contains("asn")) {
								adv.append(string, 0, string.indexOf("="));
								adv.append(" - ");
							}
							if(string.contains("country")){
								adv.append(string, string.indexOf("=") + 1, string.length());
								adv.append(",");
							}
							return string;
						})
						.peek(string -> {
							if(string.contains("city")){
								adv.append(string, string.indexOf("=") + 1, string.length());
								adv.append(" - ");
							}
							if(string.contains("region=")){
								adv.append(string, string.indexOf("=") + 1, string.length());
								adv.append(",");
							}
						})
						.peek(string -> {
							if(string.contains("vpn")){
								adv.append(string);
								adv.append(" ");
							}
							if(string.contains("proxy")){
								adv.append(string);
							}

						})
						.collect(Collectors.toList()))
				.peek(strings -> adv.append("\n"))
				.collect(Collectors.toList());
		System.out.println(adv);
		return null;
	}

}
