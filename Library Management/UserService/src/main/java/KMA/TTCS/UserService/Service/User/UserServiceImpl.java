package KMA.TTCS.UserService.Service.User;

import KMA.TTCS.UserService.Dao.AccountRepository;
import KMA.TTCS.UserService.Dao.RoleRepository;
import KMA.TTCS.UserService.Entity.Account;
import KMA.TTCS.UserService.Security.JwtResponse;
import KMA.TTCS.UserService.Security.LoginRequest;
import KMA.TTCS.UserService.Security.ThongBao;
import KMA.TTCS.UserService.Service.JWT.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;


    @Override
    public ResponseEntity<?> login(JsonNode jsonNode) {
        try {
            LoginRequest loginRequest = objectMapper.treeToValue(jsonNode, LoginRequest.class);
            Account nguoiDung = accountRepository.findAccountByUsername(loginRequest.getUsername());
            System.out.println(loginRequest.getUsername() + " "+loginRequest.getPassword());
            if (nguoiDung != null) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                if (authentication.isAuthenticated()) {
                    final String jwt = jwtService.generateToken(loginRequest.getUsername());
                    return ResponseEntity.ok(new JwtResponse(jwt));
                } else {
                    return ResponseEntity.badRequest().body(new ThongBao("Xác thực không thành công", "Đăng nhập lại"));
                }
            } else {
                return ResponseEntity.badRequest().body(new ThongBao("Người dùng không tồn tại", "Đăng Nhập Lại"));
            }
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new ThongBao("Lỗi xử lý dữ liệu đầu vào", "Vui lòng thử lại"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ThongBao("Tên đăng nhập hoặc mật khẩu không chính xác", "Đăng nhập lại"));
        } catch (LockedException | DisabledException e) {
            return ResponseEntity.badRequest().body(new ThongBao("Tài khoản đã bị khóa hoặc bị vô hiệu hóa", "Liên hệ quản trị viên"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new ThongBao("Xác Thực Không Thành Công", "Đăng Nhập Lại"));
        }
    }
}
