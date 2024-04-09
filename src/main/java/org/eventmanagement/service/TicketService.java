package org.eventmanagement.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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



@Service
@Transactional
public class TicketService {

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

    
    // public void changeTicketState(long ticketId, TicketState newState) throws EntityDoesNotExistException, BadRequestException {
    //     Ticket ticket = ticketRepository.findById(ticketId)
    //             .orElseThrow(() -> new EntityDoesNotExistException("Ticket with id " + ticketId + " does not exist"));

    //     if (newState == TicketState.ACTIVE) {
    //         ticket.setTicketState(TicketState.ACTIVE);
    //         ticketRepository.save(ticket);
    //     } else {
    //         throw new BadRequestException("Invalid ticket state provided for admission.");
    //     }
    // }

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

}
