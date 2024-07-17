package KMA.TTCS.UserService.Service.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> login(JsonNode jsonNode) throws JsonProcessingException;

}
