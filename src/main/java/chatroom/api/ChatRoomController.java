package chatroom.api;

import chatroom.domain.model.ChatRoom;
import chatroom.domain.model.ChatRoomUser;
import chatroom.domain.model.InstantMessage;
import chatroom.domain.service.ChatRoomService;
import chatroom.domain.service.InstantMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.awt.event.WindowStateListener;
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

    @SubscribeMapping("/connected.users")
    public List<ChatRoomUser> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        return chatRoomService.findById(chatRoomId).getConnectedUsers();
    }

    @SubscribeMapping("/old.messages")
    public List<InstantMessage> listOldMessagesFromUserOnSubcribe(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("chatRoomId").toString();
        return instantMessageService.findAllInstantMessagesFor(principal.getName(), chatRoomId);
    }
}