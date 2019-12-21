package utils;

import chatroom.domain.model.InstantMessage;
import chatroom.domain.model.InstantMessageBuilder;

public class SystemMessages {
    public static final InstantMessage welcome(String chatRoomId, String username) {
        return new InstantMessageBuilder()
                .newMessage()
                .withChatRoomId(chatRoomId)
                .systemMessage()
                .withText(username + " joined the room.");
    }

    public static final InstantMessage goodbye(String chatRoomId, String username) {
        return new InstantMessageBuilder()
                .newMessage()
                .withChatRoomId(chatRoomId)
                .systemMessage()
                .withText(username + " left the room");
    }
}