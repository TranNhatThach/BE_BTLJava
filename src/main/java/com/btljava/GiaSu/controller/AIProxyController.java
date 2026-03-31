package com.btljava.GiaSu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
public class AIProxyController {

    private final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("classpath:ai_prompt.txt")
    private Resource aiPromptResource;

    private String getSystemPrompt() {
        try {
            return StreamUtils.copyToString(aiPromptResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Bạn là Trợ lý ảo TutorConnect hỗ trợ hệ thống kết nối Gia sư và Học viên.";
        }
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askAI(@RequestBody Map<String, String> request) {
        String userQuestion = request.get("question");

        String systemPrompt = getSystemPrompt();

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "llama3.2");
        payload.put("prompt", systemPrompt + "\n\nNgười dùng hỏi: " + userQuestion);
        payload.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {

            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    OLLAMA_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> response = responseEntity.getBody();
            String aiAnswer = (String) response.get("response");

            Map<String, String> result = new HashMap<>();
            result.put("answer", aiAnswer);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("answer", "Xin lỗi, trợ lý AI đang bận.");
            return ResponseEntity.ok(error);
        }
    }
}
