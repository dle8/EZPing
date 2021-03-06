package EZPing.chatroom.api;

import EZPing.chatroom.domain.model.ChatRoom;
import EZPing.chatroom.domain.model.ChatRoomUser;
import EZPing.chatroom.domain.model.InstantMessage;
import EZPing.chatroom.domain.service.ChatRoomService;
import EZPing.chatroom.domain.service.InstantMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private InstantMessageService instantMessageService;

    @Secured("ROLE_ADMIN") // only allowed user with role admin
    @RequestMapping(path = "/chatroom", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.save(chatRoom);
    }

    @RequestMapping("/chatroom/{chatRoomId}")
    public ModelAndView join(@PathVariable String chatRoomId, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("chatroom");
        modelAndView.addObject("chatroom", chatRoomService.findById(chatRoomId));
        return modelAndView;
    }

    // Fetch from Redis
    @SubscribeMapping("/connected.users")
    public List<ChatRoomUser> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        return chatRoomService.findById(chatRoomId).getConnectedUsers();
    }

    // Fetch from Cassandra
    @SubscribeMapping("/old.messages")
    public List<InstantMessage> listOldMessagesFromUserOnSubcribe(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        return instantMessageService.findAllInstantMessagesFor(principal.getName(), chatRoomId);
    }

    // Endpoint to receive messages from user.
    @MessageMapping("/send.message")
    public void sendMessage(@Payload InstantMessage instantMessage, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        instantMessage.setFromUser(principal.getName()); // Get message's owner
        instantMessage.setChatRoomId(chatRoomId); // Get chat room id

        // Send instantMessage object with sendPublicMessage or sendPrivateMessage with public/ private messages
        if (instantMessage.isPublic()) {
            chatRoomService.sendPublicMessage(instantMessage);
        } else {
            chatRoomService.sendPrivateMessage(instantMessage);
        }
    }
}