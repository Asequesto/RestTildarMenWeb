package kz.tildarmen.TildarMen.chat;

import kz.tildarmen.TildarMen.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<ApiResponse> findChatMessages(@PathVariable("senderId") String senderId,
                                                        @PathVariable("recipientId") String recipientId) {
        List<ChatMessage> chatMessages = chatMessageService.findChatMessages(senderId, recipientId);
        return ResponseEntity.ok(new ApiResponse("Success", chatMessages));
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
        ChatNotification notification = new ChatNotification();
        savedMessage.setId(savedMessage.getId());
        notification.setSenderId(savedMessage.getSenderId());
        notification.setRecipientId(savedMessage.getRecipientId());
        notification.setContent(savedMessage.getContent());
        messagingTemplate.convertAndSendToUser(savedMessage.getRecipientId(), "/queue/messages", notification);
    }

}
