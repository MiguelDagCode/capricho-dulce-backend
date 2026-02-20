package com.capricho.ms_usuarios.util;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ValidationHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errores=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                false, 
                "Error en la validación de los datos enviados", 
                LocalDateTime.now(), 
                errores
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ModeloNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleModeloNotFoundException(ModeloNotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
	
	// 3. Manejo del Error de Login (Spring Security) -> HTTP 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse<Void> response = new ApiResponse<>(false, "Credenciales incorrectas. Verifique su correo o contraseña.", LocalDateTime.now(), null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
	
	//ERROR 500 internal server error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
		ApiResponse<String> response =
                new ApiResponse<>(false, "Error interno del servidor",LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	//Manejo de errores de negocio 409
		@ExceptionHandler(BusinessException.class)
	    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
	        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), LocalDateTime.now(), null);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
}

