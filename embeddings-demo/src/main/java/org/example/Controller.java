package org.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;

@RestController
@RequestMapping("/gamerules")
public class Controller {
    record RulesQuestionInput(String question) {}

    private final ChatClient aiClient;
    private final VectorStore vectorStore;

    public Controller(ChatClient chatClient, VectorStore vectorStore) {
        this.aiClient = chatClient;
        this.vectorStore = vectorStore;
    }

    /**
     * Ask about our custom game's rules, uses vector store to find answers.
     * @param rulesQuestion
     * @return
     */
    @PostMapping(produces = "text/plain")
    public String askAboutGameRules(@RequestBody RulesQuestionInput rulesQuestion) {
        return aiClient.prompt()
                .user(rulesQuestion.question())
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();
    }
}
