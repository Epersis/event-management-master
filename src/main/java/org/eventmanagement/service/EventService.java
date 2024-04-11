package org.eventmanagement.service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

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
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

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

    public List<EventDto> getEvents() {
        List<Event> savedEvents = this.eventRepository.findAll();
        List<EventDto> savedEventDTOPage = savedEvents.stream().map(s -> (EventDto) this.objectConverter.convert(s,
                EventDto.class)).collect(Collectors.toList());
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
            savedEvent.get().setVenue(eventDto.getVenue());
            savedEvent.get().setEventType(eventDto.getEventType());
            savedEvent.get().setAvailableTickets(eventDto.getAvailableTickets());
            savedEvent.get().setTicketPrice(eventDto.getTicketPrice());
            savedEvent.get().setEventDateTime(eventDto.getEventDateTime());
            savedEvent.get().setTotalTickets(eventDto.getTotalTickets());

            // Set the attendance count from the DTO
            savedEvent.get().setAttendanceCount(eventDto.getAttendanceCount());

            //set cancellation fee
            savedEvent.get().setCancellationFee(eventDto.getCancellationFee());
            savedEvent.get().setLastModifiedDateTime(new Date());

            Event updatedSavedEvent = this.eventRepository.saveAndFlush(savedEvent.get());
            EventDto savedEventDto = (EventDto) this.objectConverter.convert(updatedSavedEvent, EventDto.class);
            return Optional.of(savedEventDto);
        }
        return Optional.empty();
    }

    public Optional<List<EventDto>> updateStates() {

        List<Event> savedEvents = this.eventRepository.findAll();
        Date currentDate = new Date();

        for (long i=1; i<savedEvents.size() + 1; i++) {
            Optional<Event> savedEvent = this.eventRepository.findById(i);
            if (savedEvent.isPresent()) {
                if ((savedEvent.get().getEventDateTime().getTime() < currentDate.getTime()) && !(savedEvent.get().getEventState().equals(EventState.CANCELLED))) {
                    savedEvent.get().setEventState(EventState.COMPLETED);
                    Event updatedSavedEvent = this.eventRepository.saveAndFlush(savedEvent.get());
                }
            }
        }

        List<EventDto> savedEventDTOPage = savedEvents.stream().map(s -> (EventDto) this.objectConverter.convert(s,
                EventDto.class)).collect(Collectors.toList());

        return Optional.of(savedEventDTOPage);
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
        if (savedEvent.get().getEventState().equals(EventState.COMPLETED)) {
            throw new BadRequestException("Event has ended.");
        }
        if (savedEvent.isPresent()) {
            savedEvent.get().setEventState(EventState.CANCELLED);
            // Initiate the Refunds.
            List<Booking> activeBookingAssociatedWithEvent = this.bookingRepository.findAllByEventIdAndBookingStatus(id,
                    BookingStatus.ACCEPTED);
            this.bookingService.cancelActiveBookingsIfEventCancelled(activeBookingAssociatedWithEvent,
                    (BookingEventDetailsDto) this.objectConverter.convert(savedEvent.get(),
                            BookingEventDetailsDto.class));
            Event updatedSavedEvent = this.eventRepository.saveAndFlush(savedEvent.get());
            EventDto savedEventDto = (EventDto) this.objectConverter.convert(updatedSavedEvent, EventDto.class);
            return Optional.of(savedEventDto);
        }
        return Optional.empty();
    }

    public boolean isEventDateToday(long eventId) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Get the event from the database
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            Date eventDate = event.getEventDateTime();

            // Convert Date to LocalDate
            LocalDate eventLocalDate = eventDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Check if the event's date is the same as the current date
            return currentDate.isEqual(eventLocalDate);
        } else {
            throw new EventNotFoundException("Event not found with ID: " + eventId);
        }
    }

    public EventState getEventState(long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.getEventState();
        }
        throw new EventNotFoundException("Event not found with ID: " + eventId);
    }
    

}
