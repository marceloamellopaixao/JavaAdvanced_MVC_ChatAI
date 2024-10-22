package br.com.fiap.chatai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chat;

    public ChatService(ChatClient.Builder chatBuilder){
        this.chat = chatBuilder
                .defaultSystem(
                        """
                            Você é um professor de ciências.
                            Responda com textos adequados para alunos de 8 anos.
                            Responda apenas perguntas sobre ciências.
                            Se não souber a resposta, diga que não sabe.
                        """)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    public String sendToAi(String message){
        return chat
                .prompt()
                .user(message)
                .call()
                .content();
    }
}
