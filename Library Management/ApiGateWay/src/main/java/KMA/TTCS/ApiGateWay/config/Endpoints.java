package KMA.TTCS.ApiGateWay.config;

public class Endpoints {

    // PUBLIC ENDPOINTS
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/v1/books/**" ,
            "/api/v1/books" ,

    };
    public static final String[] PUBLIC_POST_ENDPOINTS = {
    };
    public static final String[] PUBLIC_DELETE_ENDPOINTS = {
    };
    public static final String[] PUBLIC_PUT_ENDPOINTS = {
    };

    // ADMIN ENDPOINTS
    public static final String[] ADMIN_GET_ENDPOINTS = {
//            "/api/v1/employees/**","/api/v1/borrowing/**","/api/v1/users/** "
            "/api/v1/employees","/api/v1/borrowing","/api/v1/users "

    };
    public static final String[] ADMIN_POST_ENDPOINTS = {
//            "/api/v1/employees/**","/api/v1/borrowing/**","/api/v1/users/** "
            "/api/v1/employees","/api/v1/borrowing","/api/v1/users"

    };
    public static final String[] ADMIN_DELETE_ENDPOINTS = {
//            "/api/v1/employees/**","/api/v1/borrowing/**","/api/v1/users/** "
            "/api/v1/employees","/api/v1/borrowing","/api/v1/users "

    };
    public static final String[] ADMIN_PUT_ENDPOINTS = {
//            "/api/v1/employees/**","/api/v1/borrowing/**","/api/v1/users/** "
            "/api/v1/employees","/api/v1/borrowing","/api/v1/users","/api/v1/books"

    };

    // USER ENDPOINTS
    public static final String[] USER_GET_ENDPOINTS = {
//            "/api/v1/borrowing/**"
            "/api/v1/borrowing"
    };
    public static final String[] USER_POST_ENDPOINTS = {
//            "/api/v1/borrowing/**"
            "/api/v1/borrowing"
    };
    public static final String[] USER_PUT_ENDPOINTS = {
//            "/api/v1/borrowing/**"
            "/api/v1/borrowing"

    };
    public static final String[] USER_DELETE_ENDPOINTS = {
    };
}
