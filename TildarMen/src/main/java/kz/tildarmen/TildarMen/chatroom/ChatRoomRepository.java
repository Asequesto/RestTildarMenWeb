package kz.tildarmen.TildarMen.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    ChatRoom findBySenderIdAndRecipientId(String senderId, String recipientId);
}
