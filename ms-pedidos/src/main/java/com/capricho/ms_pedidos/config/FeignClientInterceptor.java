package com.capricho.ms_pedidos.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Obtenemos el contexto de la petición web actual
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // Extraemos el token JWT que mandó Postman/Angular
            String authorizationHeader = request.getHeader("Authorization");
            
            // Si hay un token, se lo inyectamos a la llamada de Feign
            if (authorizationHeader != null) {
                requestTemplate.header("Authorization", authorizationHeader);
            }
        }
    }
}