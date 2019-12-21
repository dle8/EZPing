package chatroom.api;

import chatroom.domain.model.ChatRoom;
import chatroom.domain.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}