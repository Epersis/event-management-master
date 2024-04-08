package org.eventmanagement.service;

import java.time.LocalDate;
import java.time.ZoneId;
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
            ticket.setTicketState(TicketState.ACTIVE);
            ticketRepository.save(ticket);
        } else {
            throw new BadRequestException("Invalid ticket state provided for admission.");
        }
    }


}
