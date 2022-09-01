package com.chaoticchaotic.ipchecker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "proxycheck.io",
        url = "http://proxycheck.io/v2/"
)
public interface ProxyCheckClient {

    @GetMapping("{ip}?key=mt8r0b-12e301-936519-2264i6&asn=1&vpn=3")
    ResponseEntity<Object> getCheck(@PathVariable(value = "ip") String ip);
}
