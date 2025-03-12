package kz.tildarmen.TildarMen.chatroom;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public String getChatRoomId(String senderId, String recipientId, boolean exists){
        ChatRoom room = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if(exists){
            return room.getId();
        }
        return createRoom(senderId, recipientId);
    }

    public String createRoom(String senderId, String recipientId){
        String chatId = String.format("%s-%s", senderId, recipientId);

        ChatRoom senderRecipient = new ChatRoom();
        senderRecipient.setChatId(chatId);
        senderRecipient.setSenderId(senderId);
        senderRecipient.setRecipientId(recipientId);

        ChatRoom recipientSender = new ChatRoom();
        recipientSender.setChatId(chatId);
        recipientSender.setSenderId(recipientId);
        recipientSender.setRecipientId(senderId);

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;

    }

}
