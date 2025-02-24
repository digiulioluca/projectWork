package com.progettoSpring.sistemaUtenti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe GlobalExceptionHandler funge da gestore globale delle eccezioni per l'applicazione.
 * È annotata con @ControllerAdvice, il che consente a Spring di applicarla a tutti i controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Questo metodo gestisce le eccezioni di tipo MethodArgumentNotValidException,
     * che vengono sollevate quando la validazione degli argomenti (ad esempio con @Valid) fallisce.
     *
     * @param ex l'eccezione sollevata durante la validazione degli argomenti
     * @return un oggetto ResponseEntity contenente una mappa di errori e lo status HTTP BAD_REQUEST (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Crea una mappa per raccogliere i messaggi di errore, dove la chiave è il nome del campo e il valore è il messaggio d'errore.
        Map<String, String> errors = new HashMap<>();

        // Per ogni errore presente nel risultato della validazione, estrai il nome del campo e il messaggio d'errore.
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // Effettua il cast a FieldError per accedere al nome del campo
            String fieldName = ((FieldError) error).getField();
            // Ottieni il messaggio d'errore associato al campo
            String errorMessage = error.getDefaultMessage();
            // Inserisci nella mappa il campo e il relativo messaggio d'errore
            errors.put(fieldName, errorMessage);
        });

        // Restituisce una ResponseEntity contenente la mappa degli errori e lo status HTTP 400 (Bad Request)
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}