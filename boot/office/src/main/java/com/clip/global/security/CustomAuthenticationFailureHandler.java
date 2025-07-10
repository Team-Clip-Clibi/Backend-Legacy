//package com.clip.global.security;
//
//import com.clip.global.security.util.DistributeLockService;
//import com.clip.global.security.util.LoginAttemptManager;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    private final LoginAttemptManager loginAttemptManager;
//    private final DistributeLockService distributeLockService;
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//        String requestId = request.getParameter("username");
//
//        distributeLockService.executeWithLock(requestId, () -> loginAttemptManager.increaseFailCount(requestId));
//
//        if (loginAttemptManager.isBlockedUserId(requestId)) {
//            response.sendRedirect("/office/admin/login?locked=true");
//            return;
//        }
//        response.sendRedirect("/office/admin/login?error=true");
//    }
//}
