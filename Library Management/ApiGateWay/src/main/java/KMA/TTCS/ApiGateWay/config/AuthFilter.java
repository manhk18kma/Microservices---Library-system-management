package KMA.TTCS.ApiGateWay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config>{
	private final WebClient.Builder webClientBuilder;

	public AuthFilter(WebClient.Builder webClientBuilder) {
		super(Config.class);
		this.webClientBuilder = webClientBuilder;
	}
	@Autowired
	private JwtService jwtService;


	public static class Config {
		// empty class as I don't need any particular configuration
	}

	@Override
	public GatewayFilter apply(Config config ) {
		return (exchange, chain) -> {
//			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//				throw new RuntimeException("Missing authorization information");
//			}


//			Lay ra entpoint , Method
			String requestedEndpoint = exchange.getRequest().getURI().getPath();
			HttpMethod method = exchange.getRequest().getMethod();
//			Guese account
			if (Arrays.asList(Endpoints.PUBLIC_GET_ENDPOINTS).contains(requestedEndpoint)) {
				ServerHttpRequest request = exchange.getRequest().mutate().
						header("X-auth-username", "gueseAccount").
						build();
				return chain.filter(exchange.mutate().request(request).build());
			}else {
				// 			Can co token để xác thực logged in account
				//			Lay ra token
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				String[] parts = authHeader.split(" ");
				if (parts.length != 2 || !"Bearer".equals(parts[0])) {
					throw new RuntimeException("Incorrect authorization structure");
				}
				String username = jwtService.extractUsername(parts[1]);
				List<String> roles = jwtService.extractRoles(parts[1]);
				boolean authorized = checkAuthorization(roles , requestedEndpoint , method.toString());
				if ((username == null || username.equals("")) || !authorized) {
					System.out.println("END");
					throw new RuntimeException("Authorization error");
				}

				ServerHttpRequest request = exchange.getRequest().mutate().
						header("X-auth-username", username).
						build();
				return chain.filter(exchange.mutate().request(request).build());
			}



		};
	}

	private boolean checkAuthorization(List<String> roles, String requestedEndpoint, String method) {
		if (method.equals("GET")) {
			if (Arrays.asList(Endpoints.PUBLIC_GET_ENDPOINTS).contains(requestedEndpoint)) {
				return true;
			} else if (Arrays.asList(Endpoints.USER_GET_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("USER");
			} else if (Arrays.asList(Endpoints.ADMIN_GET_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("ADMIN");
			}
			return false;

		} else if (method.equals("POST")) {
			if (Arrays.asList(Endpoints.PUBLIC_POST_ENDPOINTS).contains(requestedEndpoint)) {
				return true;
			} else if (Arrays.asList(Endpoints.USER_POST_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("USER");
			} else if (Arrays.asList(Endpoints.ADMIN_POST_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("ADMIN");
			}
			return false;
		} else if (method.equals("PUT")) {
			if (Arrays.asList(Endpoints.PUBLIC_PUT_ENDPOINTS).contains(requestedEndpoint)) {
				return true;
			} else if (Arrays.asList(Endpoints.USER_PUT_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("USER");
			} else if (Arrays.asList(Endpoints.ADMIN_PUT_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("ADMIN");
			}
			return false;


		} else if (method.equals("DELETE")) {
			if (Arrays.asList(Endpoints.PUBLIC_DELETE_ENDPOINTS).contains(requestedEndpoint)) {
				return true;
			} else if (Arrays.asList(Endpoints.USER_DELETE_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("USER");
			} else if (Arrays.asList(Endpoints.ADMIN_DELETE_ENDPOINTS).contains(requestedEndpoint)) {
				return roles.contains("ADMIN");
			}
			return false;
		}
		return false;
	}

}
