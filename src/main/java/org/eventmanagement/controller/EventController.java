package org.eventmanagement.controller;

import java.io.OutputStreamWriter;
import jakarta.validation.Valid;
import java.util.List;

import java.util.Optional;

import org.eventmanagement.dto.EventDto;
import org.eventmanagement.dto.MessageResponse;
import org.eventmanagement.enums.EventState;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.service.EventService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/events")
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Input JSON is invalid."));
        }

        Optional<EventDto> savedEventDto = this.eventService.createEvent(eventDto);
        return new ResponseEntity<>(savedEventDto.get(), HttpStatus.CREATED);

    }

    @GetMapping
    @PreAuthorize("hasRole('EVENT_MANAGER') or hasRole('TICKET_OFFICER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> page = this.eventService.getEvents();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('EVENT_MANAGER') or hasRole('TICKET_OFFICER') or hasRole('CUSTOMER')")
    public ResponseEntity<?> getEventsById(@PathVariable long eventId) {
        Optional<EventDto> savedEvent = this.eventService.getEventById(eventId);
        if (savedEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(savedEvent.get(), HttpStatus.OK);
    }

    @PostMapping("/updateAll")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<?> updateAllStates() {

        
        Optional<List<EventDto>> events = this.eventService.updateStates();
        

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<?> updateEvent(@PathVariable long eventId, @RequestBody EventDto eventDto) {
        Optional<EventDto> updatedEvent = this.eventService.updateEvent(eventId, eventDto);
        if (updatedEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(updatedEvent.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable long eventId) {
        this.eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{eventId}/cancel")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<?> cancelEvent(@PathVariable long eventId) throws BadRequestException {
        Optional<EventDto> savedEventDto = this.eventService.cancelEvent(eventId);
        return new ResponseEntity<>(savedEventDto.get(), HttpStatus.OK);

    }

    @GetMapping("/{eventId}/tickets-sold")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<Integer> getTicketsSold(@PathVariable long eventId) {
        int ticketsSold = eventService.calculateTicketsSold(eventId);
        return ResponseEntity.ok(ticketsSold);
    }

    @GetMapping("/{eventId}/revenue")
    // @PreAuthorize("hasRole('EVENT_MANAGER') or hasRole('TICKET_OFFICER') or
    // hasRole('CUSTOMER')")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<Double> getEventRevenue(@PathVariable Long eventId) {
        Optional<EventDto> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EventDto eventDto = eventOptional.get();
        double ticketPrice = eventDto.getTicketPrice();
        int ticketsSold = eventService.calculateTicketsSold(eventId);
        double revenue = ticketPrice * ticketsSold;

        return ResponseEntity.ok(revenue);

    }

    // @GetMapping("/generate-report")
    // @PreAuthorize("hasRole('EVENT_MANAGER')")
    // public ResponseEntity<byte[]> generateReport() {
    //     try {
    //         // Fetch all events
    //         // Page<EventDto> eventsPage = eventService.getEvents(Pageable.unpaged());
    //         // List<EventDto> events = eventsPage.getContent();
    //         List<EventDto> events = eventService.getEvents();

    //         // Initialize variables to hold total ticket sales and revenue
    //         int totalTicketsSold = 0;
    //         double totalRevenue = 0.0;

    //         // Initialize StringBuilder to store CSV content
    //         StringBuilder csvContent = new StringBuilder();
    //         csvContent.append("Event Name,Total Tickets Sold,Total Revenue\n");

    //         // Iterate over each event to calculate revenue and append to CSV content
    //         for (EventDto event : events) {
    //             // Fetch revenue for the current event
    //             ResponseEntity<Double> revenueResponse = getEventRevenue(event.getId());
    //             if (revenueResponse.getStatusCode() == HttpStatus.OK) {
    //                 double revenue = revenueResponse.getBody();
    //                 int ticketsSold = event.getTicketsSold();
    //                 totalTicketsSold += ticketsSold;
    //                 totalRevenue += revenue;
    //                 csvContent.append(String.format("%s,%d,%.2f\n", event.getName(), ticketsSold, revenue));
    //             }
    //         }

    //         // Append total ticket sales and revenue to the CSV content
    //         csvContent.append(String.format("Total,%d,%.2f\n", totalTicketsSold, totalRevenue));

    //         // Convert CSV content to byte array
    //         byte[] csvBytes = csvContent.toString().getBytes();

    //         // Set response headers
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    //         headers.setContentDispositionFormData("filename", "event_report.csv");

    //         // Return CSV data as response
    //         return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    //     } catch (Exception e) {
    //         // Handle exceptions
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }

    @GetMapping("/generate-report")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<byte[]> generateReport() {
        try {
            // Fetch all events
            List<EventDto> events = eventService.getEvents();

            // Initialize variables to hold total ticket sales, revenue, and attendance count
            int totalTicketsSold = 0;
            double totalRevenue = 0.0;
            int totalAttendanceCount = 0;

            // Initialize StringBuilder to store CSV content
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Event Name,Total Tickets Sold,Total Revenue,Total Attendance Count\n");

            // Iterate over each event to calculate revenue and attendance count, and append to CSV content
            for (EventDto event : events) {
                // Fetch revenue for the current event
                ResponseEntity<Double> revenueResponse = getEventRevenue(event.getId());
                if (revenueResponse.getStatusCode() == HttpStatus.OK) {
                    double revenue = revenueResponse.getBody();
                    int ticketsSold = event.getTicketsSold();
                    totalTicketsSold += ticketsSold;
                    totalRevenue += revenue;
                    totalAttendanceCount += event.getAttendanceCount();
                    csvContent.append(String.format("%s,%d,%.2f,%d\n", event.getName(), ticketsSold, revenue, event.getAttendanceCount()));
                }
            }

            // Append total ticket sales, revenue, and attendance count to the CSV content
            csvContent.append(String.format("Total,%d,%.2f,%d\n", totalTicketsSold, totalRevenue, totalAttendanceCount));

            // Convert CSV content to byte array
            byte[] csvBytes = csvContent.toString().getBytes();

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("filename", "event_report.csv");

            // Return CSV data as response
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{eventId}/check-validity")
    @PreAuthorize("hasRole('EVENT_MANAGER') or hasRole('TICKET_OFFICER')")
    public ResponseEntity<Boolean> checkEventValidity(@PathVariable long eventId) {
        // Check if the event's date is today's date
        boolean isEventToday = eventService.isEventDateToday(eventId);
        return ResponseEntity.ok(isEventToday);
    }

    @GetMapping("/{eventId}/event-state")
    @PreAuthorize("hasRole('EVENT_MANAGER') or hasRole('TICKET_OFFICER')")
    public ResponseEntity<?> getEventState(@PathVariable long eventId) {
        Optional<EventDto> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        EventDto eventDto = eventOptional.get();
        EventState eventState = eventDto.getEventState();
    
        return ResponseEntity.ok(eventState);
    }
    

}
