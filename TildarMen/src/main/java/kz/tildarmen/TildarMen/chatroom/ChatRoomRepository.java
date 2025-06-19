package kz.tildarmen.TildarMen.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    ChatRoom findBySenderIdAndRecipientId(String senderId, String recipientId);

    List<ChatRoom> findAllBySenderIdOrRecipientId(String senderId, String recipientId);

    @Query("{ $or: [ { 'chatId': { $regex: ?0 } }, { 'chatId': { $regex: ?1 } } ] }")
    List<ChatRoom> findUserChatRooms(String senderRegex, String recipientRegex);

    void deleteAllChatRooms(List<ChatRoom> chatRooms);

}
