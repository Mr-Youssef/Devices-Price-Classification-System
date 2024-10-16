package com.example.AITask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PricePredictionService {

    @Value("${python.api.url}")
    private String pythonApiUrl;

    public int predictPrice(Device device) {
        RestTemplate restTemplate = new RestTemplate();
        String url = pythonApiUrl + "/predict";
        System.out.println("URL Testttttttt: " + url);
        return restTemplate.postForObject(url, device, Integer.class);
    }
}
