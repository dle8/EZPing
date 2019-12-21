package EZPing.chatroom.domain.service;

import EZPing.chatroom.domain.model.ChatRoom;
import EZPing.chatroom.domain.model.ChatRoomUser;
import EZPing.chatroom.domain.model.InstantMessage;
import EZPing.chatroom.domain.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import EZPing.utils.Destinations;
import EZPing.utils.SystemMessages;

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
    public void sendPublicMessage(InstantMessage instantMessage) {
        // Send message to destination: "/topic/" + chatRoomId + ".public.messages";
        webSocketMessagingTemplate.convertAndSend(
                Destinations.ChatRoom.publicMessages(instantMessage.getChatRoomId()),
                instantMessage
        );
        // Store message to user' conversations in Cassandra
        instantMessageService.appendInstantMessageToConversations(instantMessage);
    }

    @Override
    public void sendPrivateMessage(InstantMessage instantMessage) {
        // Send message to "/queue/" + chatRoomId + ".private.messages"
        // Spring also attaches to the above destination with receiver's WebSocket session ID to know who is the receiver
        // User subscribes to "/queue/" + chatRoomId + ".private.messages" + sessionId in stompSuccess
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
        // Store message to user' conversations in Cassandra
        instantMessageService.appendInstantMessageToConversations(instantMessage);
    }

    // Join a provided user to a provided chatroom in the parameters
    @Override
    public ChatRoom join(ChatRoomUser joiningUser, ChatRoom chatRoom) {
        chatRoom.addUser(joiningUser);
        chatRoomRepository.save(chatRoom);
        sendPublicMessage(SystemMessages.welcome(chatRoom.getId(), joiningUser.getUsername()));
        // Send a public message to "/topic/" + chatRoomId + ".connected.users" destination to update connected users.
        updateConnectedUsersViaWebsocket(chatRoom);
        return chatRoom;
    }

    // Leave a provided user from a provided chatroom in the parameters. Same flow as joining
    @Override
    public ChatRoom leave(ChatRoomUser leavingUser, ChatRoom chatRoom) {
        sendPublicMessage(SystemMessages.goodbye(chatRoom.getId(), leavingUser.getUsername()));
        chatRoom.removeUser(leavingUser);
        chatRoomRepository.save(chatRoom);
        updateConnectedUsersViaWebsocket(chatRoom);
        return chatRoom;
    }

    @Override
    public List<ChatRoom> findAll() {
        return (List<ChatRoom>) chatRoomRepository.findAll();
    }
}