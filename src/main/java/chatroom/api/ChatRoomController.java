package chatroom.api;

import chatroom.domain.model.ChatRoom;
import chatroom.domain.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

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
}