package KMA.TTCS.UserService.Service.Account;


import KMA.TTCS.UserService.Entity.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    public Account findByUsername(String username);
    public Account findById(int id);


}
