package com.btljava.GiaSu.controller;

import com.btljava.GiaSu.dto.GiaSuResponse;
import com.btljava.GiaSu.entity.YeuCauTimGiaSu;
import com.btljava.GiaSu.service.GiaSuService;
import com.btljava.GiaSu.service.YeuCauGiaSuService;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AIProxyController {

    private final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final RestTemplate restTemplate = new RestTemplate();

    private final GiaSuService giaSuService;
    private final YeuCauGiaSuService yeuCauGiaSuService;

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

        // SIMPLE RAG LOGIC
        StringBuilder context = new StringBuilder("\n\n[DỮ LIỆU THỰC TẾ TỪ HỆ THỐNG]:\n");
        String questionLower = userQuestion.toLowerCase();

        if (questionLower.contains("gia sư") || questionLower.contains("tutor") || questionLower.contains("tìm")) {
            List<GiaSuResponse> topTutors = giaSuService.timKiemGiaSu(null, null, null, null, null, null, 0, 5)
                    .getContent();
            String tutorInfo = topTutors.stream()
                    .map(gs -> String.format("- %s: Dạy %s, Giá %s, Đánh giá %s sao",
                            gs.getHoTen(), gs.getMonHoc(), gs.getHocPhiMoiGio(), gs.getDiemDanhGia()))
                    .collect(Collectors.joining("\n"));
            context.append("Danh sách gia sư tiêu biểu hiện có:\n").append(tutorInfo).append("\n");
        }

        if (questionLower.contains("lớp") || questionLower.contains("tìm việc") || questionLower.contains("yêu cầu")) {
            List<YeuCauTimGiaSu> classes = yeuCauGiaSuService.layTatCa();
            String classInfo = classes.stream().limit(5)
                    .map(cl -> String.format("- Lớp %s: Ngân sách %s-%s, Địa điểm %s",
                            cl.getMonHoc().getTenMon(), cl.getNganSachMin(), cl.getNganSachMax(), cl.getDiaDiem()))
                    .collect(Collectors.joining("\n"));
            context.append("Danh sách lớp mới nhất đang tìm gia sư:\n").append(classInfo).append("\n");
        }
        // -------------------------

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "llama3.2");
        payload.put("prompt", systemPrompt + context.toString() + "\n\nNgười dùng hỏi: " + userQuestion);
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
