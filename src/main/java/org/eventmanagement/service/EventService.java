package org.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.eventmanagement.converter.ObjectConverter;
import org.eventmanagement.dto.BookingEventDetailsDto;
import org.eventmanagement.dto.EventDto;
import org.eventmanagement.dto.UserDetailsImpl;
import org.eventmanagement.enums.BookingStatus;
import org.eventmanagement.enums.EventState;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.model.Booking;
import org.eventmanagement.model.Event;
import org.eventmanagement.repository.BookingRepository;
import org.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.eventmanagement.exception.EventNotFoundException;
@Service
@Transactional
public class EventService {

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    public Optional<EventDto> createEvent(EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String createdBy = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            createdBy = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        }
        eventDto.setCreatedBy(createdBy);
        Event event = (Event) this.objectConverter.convert(eventDto, Event.class);
        Event savedEvent = this.eventRepository.save(event);
        EventDto returnedEvent = (EventDto) this.objectConverter.convert(savedEvent, EventDto.class);
        return Optional.of(returnedEvent);
    }


    public Page<EventDto> getEvents(Pageable pageable) {
        Page<Event> savedEvents = this.eventRepository.findAll(pageable);
        Page<EventDto> savedEventDTOPage = savedEvents.map(s -> (EventDto) this.objectConverter.convert(s,
                EventDto.class));
        return savedEventDTOPage;
    }

    public Optional<EventDto> getEventById(long eventId) {
        Optional<Event> savedEvent = this.eventRepository.findById(eventId);
        if (savedEvent.isPresent()) {
            EventDto returnedEventDTO = (EventDto) this.objectConverter.convert(savedEvent.get(), EventDto.class);
            return Optional.of(returnedEventDTO);
        }
        return Optional.empty();
    }

    public Optional<EventDto> updateEvent(long id, EventDto eventDto) {
        Optional<Event> savedEvent = this.eventRepository.findById(id);
        if (savedEvent.isPresent()) {
            savedEvent.get().setName(eventDto.getName());
            savedEvent.get().setVenue(eventDto.getName());
            savedEvent.get().setEventType(eventDto.getEventType());
            savedEvent.get().setAvailableTickets(eventDto.getAvailableTickets());
            savedEvent.get().setTicketPrice(eventDto.getTicketPrice());
            savedEvent.get().setEventDateTime(eventDto.getEventDateTime());
            
            // Set the attendance count from the DTO
            savedEvent.get().setAttendanceCount(eventDto.getAttendanceCount());
            
            Event updatedSavedEvent = this.eventRepository.saveAndFlush(savedEvent.get());
            EventDto savedEventDto = (EventDto) this.objectConverter.convert(updatedSavedEvent, EventDto.class);
            return Optional.of(savedEventDto);
        }
        return Optional.empty();
    }

    public void deleteEvent(long eventId) {
        Optional<Event> savedEvent = this.eventRepository.findById(eventId);
        if (savedEvent.isPresent()) {
            this.eventRepository.delete(savedEvent.get());
        }
    }

    public int calculateTicketsSold(long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.getTotalTickets() - event.getAvailableTickets();
        } else {
            throw new EventNotFoundException("Event not found with ID: " + eventId);
        }
    }

    public Double calculateEventRevenue(Long eventId) {
        Optional<Event> savedEvent = eventRepository.findById(eventId);
        if (savedEvent.isPresent()) {
            Event event = savedEvent.get();
            int ticketsSold = event.getTicketsSold();
            double ticketPrice = event.getTicketPrice();
            return ticketPrice * ticketsSold;
        } else {
            throw new EventNotFoundException("Event with ID " + eventId + " not found");
        }
    }

    public Optional<EventDto> cancelEvent(long id) throws BadRequestException {
        Optional<Event> savedEvent = this.eventRepository.findById(id);
        if (savedEvent.get().getEventState().equals(EventState.CANCELLED)) {
            throw new BadRequestException("Event is already cancelled.");
        }
        if (savedEvent.isPresent()) {
            savedEvent.get().setEventState(EventState.CANCELLED);
            //Initiate the Refunds.
            List<Booking> activeBookingAssociatedWithEvent =
                    this.bookingRepository.findAllByEventIdAndBookingStatus(id, BookingStatus.ACCEPTED);
            this.bookingService.cancelActiveBookingsIfEventCancelled(activeBookingAssociatedWithEvent,
                    (BookingEventDetailsDto) this.objectConverter.convert(savedEvent.get(), BookingEventDetailsDto.class));
            Event updatedSavedEvent = this.eventRepository.saveAndFlush(savedEvent.get());
            EventDto savedEventDto = (EventDto) this.objectConverter.convert(updatedSavedEvent, EventDto.class);
            return Optional.of(savedEventDto);
        }
        return Optional.empty();
    }


}
