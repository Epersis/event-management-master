package org.eventmanagement.exception;

import org.eventmanagement.controller.AuthController;
import org.eventmanagement.controller.BookingController;
import org.eventmanagement.controller.EventController;
import org.eventmanagement.controller.UserController;
import org.eventmanagement.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {AuthController.class, BookingController.class, EventController.class
        , UserController.class})
@ResponseBody
public class ControllerExceptionHandler {


    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected MessageResponse userAlreadyExistWithThisEmailId(final UserAlreadyExistException ex) {
        return new MessageResponse(ex.getMessage());
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected MessageResponse entityDoesNotExistException(final EntityDoesNotExistException ex) {
        return new MessageResponse(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected MessageResponse badRequestException(final BadRequestException ex) {
        return new MessageResponse(ex.getMessage());
    }

}
