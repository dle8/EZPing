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
        // Send message to "/queue/" + chatRoomId + ".private.messages";
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