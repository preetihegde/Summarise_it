package com.summarise.webpage.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.summarise.webpage.exceptions.GeminiException;
import com.summarise.webpage.exceptions.GeminiException.ErrorType;
import com.summarise.webpage.service.WebpageService;
import com.summarise.webpage.model.WebpageData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summarise")
@CrossOrigin(origins = "*")  // allow accessing all the endpoints in the controller from any front end.. we can aslo add particular domain
@AllArgsConstructor
public class WebpageController {
    private final WebpageService webpageService;

    @PostMapping("/process")
    public ResponseEntity<JsonNode> contentProcessor(@RequestBody WebpageData data) {

        if (!data.getOperation().isEmpty() && !data.getContent().isEmpty()) {
            JsonNode response = webpageService.processContent(data);
            return  ResponseEntity.ok(response);

        }else{
            throw new GeminiException(ErrorType.INVALID_PROMPT, "Missing operation/content");
        }
    }
}
