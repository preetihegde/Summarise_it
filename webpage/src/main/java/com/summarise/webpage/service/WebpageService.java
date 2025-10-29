package com.summarise.webpage.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.summarise.webpage.exceptions.GeminiException;
import com.summarise.webpage.exceptions.GeminiException.ErrorType;
import com.summarise.webpage.model.WebpageData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;


@Service
public class WebpageService {

    private static final Logger logger = LoggerFactory.getLogger(WebpageService.class);

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private WebClient webClient;

    public JsonNode processContent(WebpageData data) {
        //build the prompt
        String getPrompt = buildPrompt(data);
        //query the AI model API
        ObjectNode bodyNode = buildRequestData(getPrompt);
        //Parse the response
        JsonNode response = parseGeminiRequest(bodyNode);
        logger.debug(response.asText());

        return response;
    }

    private String buildPrompt(WebpageData data){
        StringBuilder prompt = new StringBuilder();
        switch(data.getOperation().toLowerCase()){
            case "summarise" :
                prompt.append("Provide a simple, clear and concise summary of the following text.");
                break;
            case  "suggest" :
                prompt.append("Based on the following content: Suggest a related topics and further reading. Format the response with clear headings and bullet Points along with the link of the source where to read the content from");
                break;
            default:
                throw  new IllegalArgumentException("Unknow Operation : " + data.getOperation());
        }
        prompt.append(data.getContent());
        return prompt.toString();
    }

    private ObjectNode buildRequestData(String prompt){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ArrayNode parts = mapper.createArrayNode();
        ObjectNode part = mapper.createObjectNode();
        part.put("text",prompt);
        parts.add(part);

        ArrayNode contents = mapper.createArrayNode();
        ObjectNode content = mapper.createObjectNode();
        content.set("parts", parts);
        contents.add(content);
        rootNode.set("contents", contents);
        logger.debug(rootNode.toString());
        return rootNode;
    }

    private JsonNode parseGeminiRequest(ObjectNode requestBody) {

        JsonNode response;

        try {
            response = webClient.post()
                    .uri(geminiApiUrl)
                    .header("x-goog-api-key", geminiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientRequestException ex) {
            throw new GeminiException(ErrorType.THIRD_PARTY_UNAVAILABLE, "Gemini API unreachable");
        } catch (Exception ex) {
            throw new GeminiException(ErrorType.PARSING_FAILED, "Failed to process Gemini response");
        }

        if (response == null) {
            throw new GeminiException(ErrorType.MODEL_OUTPUT_EMPTY, "Gemini returned empty response");
        }

        JsonNode textNode = response.at("/candidates/0/content/parts/0/text");

        if (textNode.isMissingNode() || textNode.isNull()) {
            throw new GeminiException(ErrorType.MODEL_OUTPUT_EMPTY, "Gemini response missing text");
        }

        return textNode;
    }
}

