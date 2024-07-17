package KMA.TTCS.UserService.Controller;

import KMA.TTCS.UserService.Service.Account.AccountServiceImpl;
import KMA.TTCS.UserService.Service.User.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class AccountController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping
    public String updateBook() {
        System.out.println("test");
        return "test";
    }

    @PostMapping
    public ResponseEntity<?> dangNhapJsonNode(@RequestBody JsonNode jsonNode) throws JsonProcessingException {
        ResponseEntity response = userService.login(jsonNode);
        return response;
    }

}
