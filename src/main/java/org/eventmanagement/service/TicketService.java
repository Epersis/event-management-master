package org.eventmanagement.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.Duration;
import java.time.Instant;

import org.eventmanagement.converter.ObjectConverter;
import org.eventmanagement.dto.EventDto;
import org.eventmanagement.dto.TicketDto;
import org.eventmanagement.dto.UserDetailsImpl;
import org.eventmanagement.enums.BookingStatus;
import org.eventmanagement.enums.TicketState;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.model.Booking;
import org.eventmanagement.model.Event;
import org.eventmanagement.model.Ticket;
import org.eventmanagement.repository.BookingRepository;
import org.eventmanagement.repository.EventRepository;
import org.eventmanagement.repository.TicketRepository;
import org.eventmanagement.repository.BookingRepository;
import org.eventmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.eventmanagement.exception.EventNotFoundException;
import org.eventmanagement.exception.BadRequestException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import org.eventmanagement.enums.EventState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    public Page<TicketDto> getTickets(Pageable pageable) {
        Page<Ticket> ticketsPage = ticketRepository.findAll(pageable);
        return ticketsPage.map(ticket -> (TicketDto) objectConverter.convert(ticket, TicketDto.class));
    }

    public Optional<TicketDto> getTicketById(long ticketId) {
        Optional<Ticket> savedTicket = ticketRepository.findById(ticketId);
        if (savedTicket.isPresent()) {
            TicketDto ticketDto = (TicketDto) objectConverter.convert(savedTicket.get(), TicketDto.class);
            return Optional.of(ticketDto);
        }
        return Optional.empty();
    }

    public String getTicketStatus(long ticketId) throws EntityDoesNotExistException {
        Optional<Ticket> savedTicket = ticketRepository.findById(ticketId);
        if (savedTicket.isPresent()) {
            return savedTicket.get().getTicketState().toString();
        }
        throw new EntityDoesNotExistException("Ticket with id " + ticketId + " does not exist");
    }

    public void changeTicketState(long ticketId, TicketState newState) throws EntityDoesNotExistException, BadRequestException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityDoesNotExistException("Ticket with id " + ticketId + " does not exist"));

        if (newState == TicketState.ACTIVE) {
            if (ticket.getTicketState() == TicketState.ACTIVE) {
                throw new BadRequestException("Ticket has already been admitted.");
            }
            ticket.setTicketState(TicketState.ACTIVE);
            incrementEventAttendanceCount(ticketId);
            ticketRepository.save(ticket);
        } else {
            throw new BadRequestException("Invalid ticket state provided for admission.");
        }
    }

    public void incrementEventAttendanceCount(long ticketId) throws EntityDoesNotExistException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityDoesNotExistException("Ticket with id " + ticketId + " does not exist"));
    
        UUID bookingId = ticket.getBookingId();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityDoesNotExistException("Booking with id " + bookingId + " does not exist"));
    
        Long eventId = booking.getEventId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityDoesNotExistException("Event with id " + eventId + " does not exist"));
    
        event.setAttendanceCount(event.getAttendanceCount() + 1);
        eventRepository.save(event);
    }

    public List<TicketDto> getTicketsByBookingId(UUID bookingId) {
        List<Ticket> tickets = ticketRepository.findByBookingBookingId(bookingId);
        return tickets.stream()
                .map(ticket -> (TicketDto) objectConverter.convert(ticket, TicketDto.class))
                .collect(Collectors.toList());
    }

    
    public boolean isTicketEventTimeOverdue(long ticketId) throws EntityDoesNotExistException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Get the ticket from the database
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            throw new EntityDoesNotExistException("Ticket not found with ID: " + ticketId);
        }
        // Get the booking from the ticket
        Booking booking = ticketOptional.get().getBooking();

        // Get the event from the booking using the eventId
        Optional<Event> eventOptional = eventRepository.findById(booking.getEventId());
        if (!eventOptional.isPresent()) {
            throw new EntityDoesNotExistException("Event not found with ID: " + booking.getEventId());
        }
        Event event = eventOptional.get();
        Date eventDate = event.getEventDateTime();


        // Convert Date to LocalDateTime
        LocalDateTime eventLocalDateTime = eventDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Check if the event's date and time is the same or after the current date and time
        return !eventLocalDateTime.isBefore(currentDateTime);
        
    }

    public boolean isTicketEventDateToday(long ticketId) throws EntityDoesNotExistException {

        // Get the current date and time
        LocalDate currentDate = LocalDate.now();

        // Get the ticket from the database
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            throw new EntityDoesNotExistException("Ticket not found with ID: " + ticketId);
        }

        // Get the booking from the ticket
        Booking booking = ticketOptional.get().getBooking();

        // Get the event from the booking using the eventId
        Optional<Event> eventOptional = eventRepository.findById(booking.getEventId());
        if (!eventOptional.isPresent()) {
            throw new EntityDoesNotExistException("Event not found with ID: " + booking.getEventId());
        }
        Event event = eventOptional.get();

        Date eventDate = event.getEventDateTime();

        // Convert the Date to Instant
        Instant instant = eventDate.toInstant();

        // Subtract 8 hours using Duration
        Instant updatedInstant = instant.minus(Duration.ofHours(8));

        // Convert the updated Instant back to Date
        Date updatedEventDate = Date.from(updatedInstant);

        // Convert Date to LocalDateTime
        LocalDate eventLocalDate = updatedEventDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LOGGER.info("event Date: {}", eventDate);
        LOGGER.info("current Date: {}", currentDate);
        LOGGER.info("event Date: {}", eventLocalDate);

        // Check if the event's date and time is the same or after the current date and time
        return currentDate.isEqual(eventLocalDate);
    }
    
    public boolean isEventCancelled(long ticketId) throws EntityDoesNotExistException {
        // Get the ticket from the database
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            throw new EntityDoesNotExistException("Ticket not found with ID: " + ticketId);
        }

        // Get the booking from the ticket
        Booking booking = ticketOptional.get().getBooking();

        // Get the event from the booking using the eventId
        Optional<Event> eventOptional = eventRepository.findById(booking.getEventId());
        if (!eventOptional.isPresent()) {
            throw new EntityDoesNotExistException("Event not found with ID: " + booking.getEventId());
        }
        Event event = eventOptional.get();

        return event.getEventState() == EventState.CANCELLED;
    
    }

    public boolean isEventCompleted(long ticketId) throws EntityDoesNotExistException {
        // Get the ticket from the database
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            throw new EntityDoesNotExistException("Ticket not found with ID: " + ticketId);
        }

        // Get the booking from the ticket
        Booking booking = ticketOptional.get().getBooking();

        // Get the event from the booking using the eventId
        Optional<Event> eventOptional = eventRepository.findById(booking.getEventId());
        if (!eventOptional.isPresent()) {
            throw new EntityDoesNotExistException("Event not found with ID: " + booking.getEventId());
        }
        Event event = eventOptional.get();

        return event.getEventState() == EventState.COMPLETED;
    
    }

    public boolean isBookingValid(long ticketId) throws BadRequestException, EntityDoesNotExistException{ 
        // Get the ticket from the database
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            throw new EntityDoesNotExistException("Ticket not found with ID: " + ticketId);
        }

        // Get the booking from the ticket
        Booking booking = ticketOptional.get().getBooking();

        return booking.getBookingStatus() == BookingStatus.ACCEPTED;

    }
}
