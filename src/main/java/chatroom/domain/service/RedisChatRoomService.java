package chatroom.domain.service;

import chatroom.domain.model.ChatRoom;
import chatroom.domain.model.ChatRoomUser;
import chatroom.domain.model.InstantMessage;
import chatroom.domain.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import utils.Destinations;
import utils.SystemMessages;

import java.util.List;

@Service
public class RedisChatRoomService implements ChatRoomService {

    @Autowired
    private SimpMessagingTemplate webSocketMessagingTemplate;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private InstantMessageService instantMessageService;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom findById(String chatRoomId) {
        return chatRoomRepository.findOne(chatRoomId);
    }

    private void updateConnectedUsersViaWebsocket(ChatRoom chatRoom) {
        webSocketMessagingTemplate.convertAndSend(
                Destinations.ChatRoom.connectedUsers(chatRoom.getId()),
                chatRoom.getConnectedUsers()
        );
    }

    @Override
    public ChatRoom join(ChatRoomUser joiningUser, ChatRoom chatRoom) {
        chatRoom.addUser(joiningUser);
        chatRoomRepository.save(chatRoom);
        sendPublicMessage(SystemMessages.welcome(chatRoom.getId(), joiningUser.getUsername()));
        updateConnectedUsersViaWebsocket(chatRoom);
        return chatRoom;
    }

    @Override
    public ChatRoom leave(ChatRoomUser leavingUser, ChatRoom chatRoom) {
        sendPublicMessage(SystemMessages.goodbye(chatRoom.getId(), leavingUser.getUsername()));
        chatRoom.removeUser(leavingUser);
        chatRoomRepository.save(chatRoom);
        updateConnectedUsersViaWebsocket(chatRoom);
        return chatRoom;
    }

    @Override
    public void sendPublicMessage(InstantMessage instantMessage) {

    }

    @Override
    public void sendPrivateMessage(InstantMessage instantMessage) {

    }

    @Override
    public List<ChatRoom> findAll() {
        return null;
    }
}