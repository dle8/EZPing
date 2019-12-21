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

    // join a provided user to a provided chatroom in the parameters
    @Override
    public ChatRoom join(ChatRoomUser joiningUser, ChatRoom chatRoom) {
        chatRoom.addUser(joiningUser);
        chatRoomRepository.save(chatRoom);
        sendPublicMessage(SystemMessages.welcome(chatRoom.getId(), joiningUser.getUsername()));
        updateConnectedUsersViaWebsocket(chatRoom);
        return chatRoom;
    }

    // leave a provided user from a provided chatroom in the parameters
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
        webSocketMessagingTemplate.convertAndSend(
                Destinations.ChatRoom.publicMessages(instantMessage.getChatRoomId()),
                instantMessage
        );
        instantMessageService.appendInstantMessageToConversations(instantMessage);
    }

    @Override
    public void sendPrivateMessage(InstantMessage instantMessage) {
        webSocketMessagingTemplate.convertAndSendToUser(
                instantMessage.getToUser(),
                Destinations.ChatRoom.privateMessages(instantMessage.getChatRoomId()),
                instantMessage
        );
        webSocketMessagingTemplate.convertAndSendToUser(
                instantMessage.getFromUser(),
                Destinations.ChatRoom.privateMessages(instantMessage.getChatRoomId()),
                instantMessage
        );
        instantMessageService.appendInstantMessageToConversations(instantMessage);
    }

    @Override
    public List<ChatRoom> findAll() {
        return (List<ChatRoom>) chatRoomRepository.findAll();
    }
}