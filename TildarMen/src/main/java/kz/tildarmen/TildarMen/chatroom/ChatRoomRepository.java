package kz.tildarmen.TildarMen.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    ChatRoom findBySenderIdAndRecipientId(String senderId, String recipientId);

    List<ChatRoom> findAllBySenderIdOrRecipientId(String senderId, String recipientId);
}
