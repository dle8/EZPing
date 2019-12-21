package EZPing.chat.api;

import chatroom.domain.model.ChatRoom;
import chatroom.domain.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class ChatController {
    @Autowired
    private ChatRoomService chatRoomService;

    @RequestMapping("/chat")
    public ModelAndView getRooms() {
        ModelAndView modelAndView = new ModelAndView("chat");
        List<ChatRoom> chatRooms = chatRoomService.findAll();
        modelAndView.addObject("chatRooms", chatRooms);
        return modelAndView;
    }
}