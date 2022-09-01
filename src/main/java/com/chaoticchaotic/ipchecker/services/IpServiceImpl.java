package com.chaoticchaotic.ipchecker.services;

import com.chaoticchaotic.ipchecker.client.ProxyCheckClient;
import com.chaoticchaotic.ipchecker.utils.FileUtil;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IpServiceImpl implements IpService {

    private final FileUtil util;
    private final ProxyCheckClient client;

    public IpServiceImpl(FileUtil util, ProxyCheckClient client) {
        this.util = util;
        this.client = client;
    }

    @Override
    public String getCheck(MultipartFile file) {
        File toCheck;
        StringBuffer adv = new StringBuffer();
        try {
            toCheck = util.uploadImage(file);
        }
        catch (Exception e) {
            throw new RuntimeException("something went wrong");
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(toCheck))) {
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
        }
        catch (Exception e) {
            throw new RuntimeException("something went wrong2");
        }
        toCheck.delete();
        return adv.toString();
    }
}
