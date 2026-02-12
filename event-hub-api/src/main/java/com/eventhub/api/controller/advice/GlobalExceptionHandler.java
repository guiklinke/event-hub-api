package com.eventhub.api.controller.advice;

import com.eventhub.api.exception.ErrorResponse;
import com.eventhub.api.exception.FieldErrorResponse;
import com.eventhub.api.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldErrorResponse> errors = extractValidationErrors(e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validação", request, errors);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException e, HttpServletRequest request) {
        String message = "O campo '" + e.getPropertyName() + "' não existe para ordenação.";
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        List<FieldErrorResponse> errors = extractJacksonErrors(e);
        String message = (errors != null && !errors.isEmpty()) ? "Erro de conversão de dados." : "JSON mal formatado ou tipo de dado inválido.";

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request, null);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> handleNegocioException(NegocioException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncaughtException(Exception e, HttpServletRequest request) {
        log.error("ERRO CRÍTICO NÃO TRATADO: {} - URI: {}", e.getMessage(), request.getRequestURI(), e);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado. Tente novamente mais tarde.",
                request, null
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<FieldErrorResponse> errors) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.status(status).body(error);
    }

    private List<FieldErrorResponse> extractValidationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(f -> new FieldErrorResponse(f.getField(), f.getDefaultMessage()))
                .toList();
    }

    private List<FieldErrorResponse> extractJacksonErrors(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException cause && cause.getPath() != null && !cause.getPath().isEmpty()) {
            String fieldName = cause.getPath().stream()
                    .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : String.valueOf(ref.getIndex()))
                    .collect(Collectors.joining("."));

            return List.of(new FieldErrorResponse(
                    fieldName,
                    "Valor inválido: '" + cause.getValue() + "'. Verifique o formato esperado."
            ));
        }
        return Collections.emptyList();
    }

}
