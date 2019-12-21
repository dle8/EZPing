package EZPing.chatroom.domain.repository;

import EZPing.chatroom.domain.model.ChatRoom;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {

}