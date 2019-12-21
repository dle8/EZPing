package EZPing.chatroom.websocket;

import EZPing.chatroom.domain.model.ChatRoomUser;
import EZPing.chatroom.domain.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEvents {
    @Autowired
    private ChatRoomService chatRoomService;

    // When a WebSokcet connection is established, shoot a system message to all connected user in the chatroom
    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        // Get chatRoomId value from CONNECT frame headers and store in WebSocket session as an attr
        String chatRoomId = headers.getNativeHeader("chatRoomId").get(0);
        headers.getSessionAttributes().put("chatRoomId", chatRoomId);
        ChatRoomUser joiningUser = new ChatRoomUser(event.getUser().getName());
        chatRoomService.join(joiningUser, chatRoomService.findById(chatRoomId));
    }

    // When a WebSokcet connection is disconnected, shoot a system message to all connected user in the chatroom
    @EventListener
    private void handleSessionDisconnected(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        // Get chatRoomId value from WebSocket session
        String chatRoomId = headers.getSessionAttributes().get("chatRoomId").toString();
        ChatRoomUser leavingUser = new ChatRoomUser(event.getUser().getName());
        chatRoomService.leave(leavingUser, chatRoomService.findById(chatRoomId));
    }
}