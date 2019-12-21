package EZPing.chatroom.domain.service;

import EZPing.chatroom.domain.model.ChatRoom;
import EZPing.chatroom.domain.model.ChatRoomUser;
import EZPing.chatroom.domain.model.InstantMessage;

import java.util.List;

public interface ChatRoomService {
    ChatRoom save(ChatRoom chatRoom);
    ChatRoom findById(String chatRoomId);
    ChatRoom join(ChatRoomUser joiningUser, ChatRoom chatRoom);
    ChatRoom leave(ChatRoomUser leavingUser, ChatRoom chatRoom);
    void sendPublicMessage(InstantMessage instantMessage);
    void sendPrivateMessage(InstantMessage instantMessage);
    List<ChatRoom> findAll();
}