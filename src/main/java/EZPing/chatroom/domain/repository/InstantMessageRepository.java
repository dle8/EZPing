package EZPing.chatroom.domain.repository;

import EZPing.chatroom.domain.model.InstantMessage;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface InstantMessageRepository extends CassandraRepository<InstantMessage> {
    List<InstantMessage> findInstantMessageByUsernameAndChatRoomId(String username, String chatRoomId);
}