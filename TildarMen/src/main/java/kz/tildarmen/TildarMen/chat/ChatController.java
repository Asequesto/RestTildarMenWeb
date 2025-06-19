package kz.tildarmen.TildarMen.chat;

import kz.tildarmen.TildarMen.chatroom.ChatRoom;
import kz.tildarmen.TildarMen.chatroom.ChatRoomService;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final UserMapper userMapper;

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

    @MessageMapping("/chat-sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage message){
        return message;
    }

    @PreAuthorize("isAuthenticated()")
    @MessageMapping("/getChats") // client sends to /app/getChats
    @SendTo("/topic/chats")      // server sends to /topic/chats
    public List<UserDto> getChats(@Header("simpUser") Principal principal) {
        long userId = Long.parseLong(principal.getName());

        List<ChatRoom> rooms = chatRoomService.findAllBySenderIdOrRecipientId(Long.toString(userId));

        List<Long> userIds = rooms.stream()
                .filter(room -> !room.getSenderId().equals(room.getRecipientId()))
                .map(room -> {
                    String sender = room.getSenderId();
                    String recipient = room.getRecipientId();
                    return sender.equals(Long.toString(userId)) ? Long.parseLong(recipient) : Long.parseLong(sender);
                })
                .distinct()
                .toList();

        List<User> users = userService.findByIdIn(userIds);
        return userMapper.toDtoList(users);
    }

}
