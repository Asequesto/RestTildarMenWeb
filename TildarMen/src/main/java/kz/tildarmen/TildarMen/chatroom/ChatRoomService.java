package kz.tildarmen.TildarMen.chatroom;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public String getChatRoomId(String senderId, String recipientId){
        System.out.println("I am here");
        String chatRoomId;
        ChatRoom room = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if(room == null) chatRoomId = createRoom(senderId, recipientId);
        else chatRoomId = room.getChatId();
        return chatRoomId;
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

    public void deleteAllUserChatRooms(String userId){
        String regex1 = "^" + userId + "-";
        String regex2 = "-" + userId + "$";
        List<ChatRoom> rooms = chatRoomRepository.findUserChatRooms(regex1, regex2);
        chatRoomRepository.deleteAll(rooms);
    }

}
