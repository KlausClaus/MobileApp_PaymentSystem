package com.example.payment.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.payment.entity.User;
import com.example.payment.service.IUserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Utility class for managing JSON Web Tokens (JWT).
 * Provides methods for generating tokens and retrieving current user information from tokens.
 */
public class TokenUtils {

    /**
     * Static reference to the {@link IUserService} for retrieving user information.
     */
    private static IUserService staticUserService;

    /**
     * Non-static {@link IUserService} reference for dependency injection.
     */
    @Resource
    private IUserService userService;

    /**
     * Sets the static {@link IUserService} reference after dependency injection.
     * Ensures the static methods can access the service layer.
     */
    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param userId The user ID to include in the token payload.
     * @param sign   The secret key used to sign the token.
     * @return A JWT token as a string.
     */
    public static String genToken(String userId, String sign) {
        return JWT.create()
                .withAudience(userId) // Save user ID in the token payload
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // Token expires in 2 hours
                .sign(Algorithm.HMAC256(sign)); // Use the secret key to sign the token
    }

    /**
     * Retrieves the currently logged-in user's information based on the token.
     *
     * @return The {@link User} object representing the current user, or {@code null} if no user is found.
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0); // Extract user ID from the token payload
                return staticUserService.getById(Integer.valueOf(userId)); // Retrieve the user by ID
            }
        } catch (Exception e) {
            return null; // Return null if any exception occurs
        }
        return null;
    }
}
