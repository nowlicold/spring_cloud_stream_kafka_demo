package com.example.kafka_demo;

import dev.ai4j.openai4j.OpenAiHttpException;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
@RestController
public class ChatController {
    private String[] keys = new String[]{
            "sk-wQOUpHbFPv4cwws2F95cF5De9f3844488a54F1C129Ff9aF81-4",
            "sk-wQOUpHbFPv4cwws2F95cF5De9f3844488a54F1C129Ff9aF8-4",
            "sk-wQOUpHbFPv4cwws2F95cF5De9f3844488a54F1C129Ff9aF81-4"
    };

    @GetMapping("/chat")
    public SseEmitter chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        SseEmitter emitter = new SseEmitter();
        AtomicBoolean isCompleted = new AtomicBoolean(false);

        // 使用循环替代递归
        for (int keyIndex = 0; keyIndex < keys.length && !isCompleted.get(); keyIndex++) {
            String currentKey = keys[keyIndex];
            tryWithKey(emitter, currentKey, message, isCompleted);

            // 如果已完成，退出循环
            if (isCompleted.get()) {
                break;
            }
        }

        return emitter;
    }

    private void tryWithKey(SseEmitter emitter, String apiKey, String message, AtomicBoolean isCompleted) {

        System.out.println("进入了");
        StreamingChatLanguageModel streamingChatLanguageModel = OpenAiStreamingChatModel.builder()
                .baseUrl("https://one.felo.me/v1/")
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .timeout(Duration.ofSeconds(4000))
                .frequencyPenalty(0.0)
                .maxTokens(4000)
                .presencePenalty(0.0)
                .temperature(0.5)
                .topP(1.0)
                .seed(1)
                .user("")
                .responseFormat("text")
                .logRequests(true)
                .logResponses(true)
                .build();

        CountDownLatch latch = new CountDownLatch(1);

        StreamingResponseHandler<AiMessage> streamingResponseHandler = new StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String s) {
                try {
                    emitter.send(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                try {

                    emitter.send("[DONE]");
                    isCompleted.set(true);
                    emitter.complete();
                } catch (IOException e) {
                    emitter.completeWithError(e);
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    emitter.send("[ERROR]");
                } catch (IOException e) {
                    emitter.completeWithError(e);
                } finally {
                    latch.countDown();
                }
            }
        };

        try {
            streamingChatLanguageModel.generate(message, streamingResponseHandler);
            // 等待异步操作完成
            latch.await();
        } catch (OpenAiHttpException e) {
            try {
                emitter.send("[ERROR]");
            } catch (IOException ex) {
                emitter.completeWithError(ex);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            emitter.completeWithError(e);
        }
    }
}




