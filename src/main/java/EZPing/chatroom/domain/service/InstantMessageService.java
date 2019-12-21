package EZPing.chatroom.domain.service;

import EZPing.chatroom.domain.model.InstantMessage;

import java.util.List;

public interface InstantMessageService {
    void appendInstantMessageToConversations(InstantMessage instantMessage);
    List<InstantMessage> findAllInstantMessagesFor(String username, String chatRoomId);
}