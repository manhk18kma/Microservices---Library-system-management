package KMA.TTCS.UserService.Service.JWT;

import KMA.TTCS.UserService.Dao.AccountRepository;
import KMA.TTCS.UserService.Entity.Account;
import KMA.TTCS.UserService.Entity.Role;
import KMA.TTCS.UserService.Service.Account.AccountServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtService {
    @Autowired
    private AccountServiceImpl accountService;
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";



    // Tạo JWT dựa trên tên đăng nhập
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        Account account = accountService.findByUsername(username);


        List<String> roles = new ArrayList<>();
        if (account != null && account.getRoles().size() > 0) {
            List<Role> list = account.getRoles();
            for (Role r : list) {
                roles.add(r.getName());
            }
        }
        claims.put("idUser", account.getId());
        claims.put("username", account.getUsername());
        claims.put("roles", roles);



        return createToken(claims, username);
    }

    // Tạo JWT với các claim đã chọn
    private String createToken(Map<String, Object> claims, String tenDangNhap) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(tenDangNhap)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
//                .setExpiration(new Date(System.currentTimeMillis() + 10000))

                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    // Lấy secret key
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
    }

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Kiểm tra thời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra thời gian hết hạn từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra cái JWT đã hết hạn
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String tenDangNhap = extractUsername(token);
        return (tenDangNhap.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
