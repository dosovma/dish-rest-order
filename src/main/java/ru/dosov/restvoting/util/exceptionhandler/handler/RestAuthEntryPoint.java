package ru.dosov.restvoting.util.exceptionhandler.handler;

import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Component
public class RestAuthEntryPoint implements AuthenticationEntryPoint {

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        String json = new JSONObject()
                .put("timestamp", new Timestamp(request.getSession().getCreationTime()))
                .put("status", HttpStatus.FORBIDDEN.value())
                .put("error", "Forbidden")
                .put("message", "Bad auth type")
                .toString();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(json);
    }
}