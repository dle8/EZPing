package chatroom.domain.service;

import chatroom.domain.model.ChatRoom;
import chatroom.domain.model.InstantMessage;
import chatroom.domain.repository.InstantMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CassandraInstantMessageService implements InstantMessageService {

    @Autowired
    private InstantMessageRepository instantMessageRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public void appendInstantMessageToConversations(InstantMessage instantMessage) {
        // If the message is public or from admin
        if (instantMessage.isFromAdmin() || instantMessage.isPublic()) {
            ChatRoom chatRoom = chatRoomService.findById(instantMessage.getChatRoomId());
            // Append the message for each connected user in the chat room
            chatRoom.getConnectedUsers().forEach(connectedUser -> {
                instantMessage.setUsername(connectedUser.getUsername());
                instantMessageRepository.save(instantMessage);
            });
        } else {
            // Append the message for only sender and receiver
            instantMessage.setUsername(instantMessage.getFromUser());
            instantMessageRepository.save(instantMessage);
            instantMessage.setUsername(instantMessage.getToUser());
            instantMessageRepository.save(instantMessage);
        }
    }

    @Override
    public List<InstantMessage> findAllInstantMessagesFor(String username, String chatRoomId) {
        return instantMessageRepository.findInstantMessageByUsernameAndChatRoomId(username, chatRoomId);
    }
}