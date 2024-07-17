package KMA.TTCS.UserService.Service.Account;

import KMA.TTCS.UserService.Dao.AccountRepository;
import KMA.TTCS.UserService.Dao.RoleRepository;
import KMA.TTCS.UserService.Entity.Account;
import KMA.TTCS.UserService.Entity.Role;
import KMA.TTCS.UserService.Security.JwtResponse;
import KMA.TTCS.UserService.Security.LoginRequest;
import KMA.TTCS.UserService.Security.ThongBao;
import KMA.TTCS.UserService.Service.JWT.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account findByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }
    @Override
    public Account findById(int id) {
        return accountRepository.findAccountById(id);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if(account==null){
            throw  new UsernameNotFoundException("Tài Khoản Không Tồn Tại");
        }
        User user = new User(account.getUsername() , account.getPassword() ,rolesToAuthorities(account.getRoles()));
        return  user;
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> quyens) {
        return quyens.stream()
                .map(quyen -> new SimpleGrantedAuthority(quyen.getName()))
                .collect(Collectors.toList());
    }


}
