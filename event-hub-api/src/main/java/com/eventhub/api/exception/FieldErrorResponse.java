package com.eventhub.api.exception;

public record FieldErrorResponse(String field, String message) {
}