# Welcome to Event Management Project

This event management project is spring boot based implementation project which is aiming to cover the event management functionality.

This functionality aims three personas:

1. Customer
2. Event Manager
3. Ticket officer

## How to setup or run the project on local system.
1. Backend
- Download the Java 17 and configured.
- Download the Apache Maven from https://maven.apache.org/download.cgi site.
- Unzip the Apache Maven in any folder.
- Run the `mvn clean install` command from the directory where this branch code has been checked out.
- Go to `target` directory.
- There should be a jar `event-management-0.0.1-SNAPSHOT.jar` in `target` directory.
- Run the command `java -jar event-management-0.0.1-SNAPSHOT.jar` command to start the project the embedded server.

2. Users' Interfaces


### Testing the APIs

## Booking Flow Step By Step

We are going to cover step by steps APIs available to book the event.

1. Sign Up and Sign In of the Customer

> First sign up using the  POST [**/api/v1/auth/signup**] (http://localhost:8081/api/v1/auth/signup)  API with below payload. It will create the user with email id and role as CUSTOMER.

    {
        {
            "email": "persis114@gmail.com",
            "firstName": "persis",
            "lastName": "e",
            "password": "Temp123",
            "confirmPassword": "Temp123",
            "mobileNumber":12345,
            "role": "ROLE_CUSTOMER"
        }
    }



> Now Sign in using this user email and password and get the JWT token using
> POST  [**/api/v1/auth/signin**] (http://localhost:8081/api/v1/auth/signin) API

    {
    	"email": "persis114@gmail.com",
    	"password": "Temp123"
    }

This will result in a JWT token in response like below.

    {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZXJzaXMxMTRAZ21haWwuY29tIiwiaWF0IjoxNzExNTIxODM3LCJleHAiOjE3MTE2MDgyMzd9.TB27vV6hTXSR-Vmtc405XUaxYZ9pk_zd9VxHFRMn-ms",
        "role": "ROLE_CUSTOMER"
    }

2. Sign Up and Sign In of the Event Manager

> Signup and Sign In using the same API with different Email ID and role as ROLE_EVENT_MANAGER

    {
    	"email": "eventmanager1@gmail.com",
    	"firstName": "Event",
    	"lastName": "Manager",
    	"password": "Temp123",
    	"confirmPassword": "Temp123",
    	"mobileNumber": "123456789",
    	"role": "ROLE_EVENT_MANAGER"
    }

Then Sign In using /api/v1/auth/signin API using the event manager email id.

    {"email":"eventmanager1@gmail.com","password":"Temp123"}

This will result in another JWT Token

    {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJldmVudG1hbmFnZXIxQGdtYWlsLmNvbSIsImlhdCI6MTcxMTUyMTkxNCwiZXhwIjoxNzExNjA4MzE0fQ._DhLo8zPoH_VLz9JrIC84JEcMEqAJ0SGMC9zeqL9CxA",
        "role": "ROLE_EVENT_MANAGER"
    }

Now we will use these 2 tokens for further API invocation linked with User and Event Manager.


## Managing  the Events
As managing the events is the responsibility of Event Manager, hence we will use the Event Manager JWT Token to create the event first.

1. Create the Event


> Invoke the POST [**/api/v1/events**] (http://localhost:8081/api/v1/events) API to create the event with event manager  token, We have to pass this token in **Authorization** header with **Bearer** prefix.


    {    
        "name": "Cutural Show",
        "venue": "Singapore",
        "eventDateTime": "2024-05-20T13:48:18Z",
        "ticketPrice": 200.0,
        "totalTickets":120,
        "availableTickets": 120,
        "eventType": "MUSIC_CONCERT",
        "eventState": "ACTIVE"
    }

This will create the Event with event ID.

    {
        "id": 4,
        "name": "Cutural Show",
        "venue": "Singapore",
        "eventDateTime": "2024-05-20T13:48:18.000+00:00",
        "ticketPrice": 200.0,
        "totalTickets": 120,
        "availableTickets": 120,
        "eventType": "MUSIC_CONCERT",
        "eventState": "ACTIVE",
        "createdBy": "eventmanager1@gmail.com",
        "createdDateTime": "2024-03-27T15:21:26.000+00:00",
        "lastModifiedDateTime": "2024-03-27T15:21:26.000+00:00"
    }

> Get all events invoking GET [**/api/v1/events**] (http://localhost:8081/api/v1/events) API to create the event with event manager  token, We have to pass this token in **Authorization** header with **Bearer** prefix.

    {
        "content": [
            {
                "id": 1,
                "name": "Cutural Show",
                "venue": "Singapore",
                "eventDateTime": "2024-05-20T13:48:18.000+00:00",
                "ticketPrice": 200.0,
                "totalTickets": 120,
                "availableTickets": 120,
                "eventType": "MUSIC_CONCERT",
                "eventState": "ACTIVE",
                "createdBy": "eventmanager1@gmail.com",
                "createdDateTime": "2024-04-08T02:56:45.000+00:00",
                "lastModifiedDateTime": "2024-04-08T02:56:45.000+00:00",
                "ticketsSold": 0
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 20,
            "sort": {
                "empty": true,
                "unsorted": true,
                "sorted": false
            },
            "offset": 0,
            "unpaged": false,
            "paged": true
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 1,
        "first": true,
        "size": 20,
        "number": 0,
        "sort": {
            "empty": true,
            "unsorted": true,
            "sorted": false
        },
        "numberOfElements": 1,
        "empty": false
    }

## Make a Booking of this Event as Customer

> A user profile can be get using the API - GET  [**/api/v1/users/profile**]
(http://localhost:8081/api/v1/users/profile) 
Have to pass in the relevant token in **Authorization** header with **Bearer** prefix.

By default, any user signed up will have 1000 in the account wallet.

    {
        "id": 7,
        "firstName": "persis",
        "lastName": "e",
        "mobileNumber": 12345,
        "email": "persis114@gmail.com",
        "role": "ROLE_CUSTOMER",
        "walletDetails": {
            "walletId": 54,
            "balance": 1000
        }
    }

2. Make the Booking for the event ID 4 which has been created above using the API

> POST [**/api/v1/bookings**]
(http://localhost:8081/api/v1/bookings)

    {
    	"bookingUserEmail": "persis114@gmail.com",
    	"bookingMobileNumber": 992352,
    	"eventId": 4,
    	"numberOfTickets": 2,
    	"transactionDetail": {
    		"paymentMethod": "WALLET"
    	}
    }

This will give the response of booking with booking ID and transaction details like below.

    {
        "bookingId": "4082519f-f378-4f21-931f-204924be29af",
        "bookingUserEmail": "persis114@gmail.com",
        "bookingMobileNumber": 992352,
        "bookedBy": "persis114@gmail.com",
        "bookingStatus": "ACCEPTED",
        "eventId": 4,
        "numberOfTickets": 2,
        "bookingDateTime": "2024-03-27T07:23:12.832+00:00",
        "eventDetails": {
            "name": "Cutural Show",
            "venue": "Singapore",
            "eventDateTime": "2024-05-20T13:48:18.000+00:00",
            "eventType": "MUSIC_CONCERT"
        },
        "transactionDetail": {
            "transactionId": "56ed020a-a7fa-42bd-9373-0cbc9fd6ce6a",
            "amount": 400.0,
            "transactionStatus": "COMPLETED",
            "paymentMethod": "WALLET",
            "transactionDateTime": "2024-03-27T07:23:12.828+00:00",
            "transactionUpdateDateTime": "2024-03-27T07:23:12.838+00:00"
        },
        "createdDateTime": "2024-03-27T07:23:12.832+00:00",
        "lastModifiedDateTime": "2024-03-27T07:23:12.832+00:00"
    }

Using this way, booking will be confirmed for the event.

Get the Event with /api/v1/events/4 to get the event available tickets (availableTickets) field, it will display remaining available tickets. 
Pass in the relevant token of the event manager into the **Authorization** header with **Bearer** prefix.

    {
        "id": 4,
        "name": "Cutural Show",
        "venue": "Singapore",
        "eventDateTime": "2024-05-20T13:48:18.000+00:00",
        "ticketPrice": 200.0,
        "totalTickets": 120,
        "availableTickets": 118,
        "eventType": "MUSIC_CONCERT",
        "eventState": "ACTIVE",
        "createdBy": "eventmanager1@gmail.com",
        "createdDateTime": "2024-03-27T15:21:26.000+00:00",
        "lastModifiedDateTime": "2024-03-27T15:23:12.000+00:00"
    }

Users can view their Booking history 
This can can be fetched using the GET API [**/api/v1/users/bookings**] (http://localhost:8081/api/v1/users/bookings).

This will give the response of booking with booking details like below.

[
    {
        "bookingId": "4082519f-f378-4f21-931f-204924be29af",
        "bookingUserEmail": "persis114@gmail.com",
        "bookingMobileNumber": 992352,
        "bookedBy": "persis114@gmail.com",
        "bookingStatus": "ACCEPTED",
        "eventId": 4,
        "numberOfTickets": 2,
        "bookingDateTime": "2024-03-27T07:23:12.832+00:00",
        "transactionDetail": {
            "transactionId": "56ed020a-a7fa-42bd-9373-0cbc9fd6ce6a",
            "amount": 400.0,
            "transactionStatus": "COMPLETED",
            "paymentMethod": "WALLET",
            "transactionDateTime": "2024-03-27T15:23:12.000+00:00",
            "transactionUpdateDateTime": "2024-03-27T15:23:12.000+00:00"
        },
        "createdDateTime": "2024-03-27T15:23:12.000+00:00",
        "lastModifiedDateTime": "2024-03-27T15:23:12.000+00:00"
    }
]

On Ticket confirmation email will be also sent on bookingUserEmail if SMTP is configured properly.

An email will be sent with booking confirmation
![alt text] src/main/resources/templates/booking-confirmation.PNG

## To Verify the Booking ID
Need to pass in the relevant token for the event manager
Booking ID can be verified after invoking the GET [**/api/v1/bookings/{bookingId}**]
(http://localhost:8081/api/v1/bookings/4082519f-f378-4f21-931f-204924be29af)  API.

This will give the response of booking with for the booking id like below.

    {
        "bookingId": "4082519f-f378-4f21-931f-204924be29af",
        "bookingUserEmail": "persis114@gmail.com",
        "bookingMobileNumber": 992352,
        "bookedBy": "persis114@gmail.com",
        "bookingStatus": "ACCEPTED",
        "eventId": 4,
        "numberOfTickets": 2,
        "bookingDateTime": "2024-03-27T07:23:12.832+00:00",
        "transactionDetail": {
            "transactionId": "56ed020a-a7fa-42bd-9373-0cbc9fd6ce6a",
            "amount": 400.0,
            "transactionStatus": "COMPLETED",
            "paymentMethod": "WALLET",
            "transactionDateTime": "2024-03-27T15:23:12.000+00:00",
            "transactionUpdateDateTime": "2024-03-27T15:23:12.000+00:00"
        },
        "createdDateTime": "2024-03-27T15:23:12.000+00:00",
        "lastModifiedDateTime": "2024-03-27T15:23:12.000+00:00"
    }


## Cancel the Booking
To cancel a booking which is booked by you, use the below API.

- If booking is already cancelled, then you can't cancelled it again.
- Booking can be cancelled only before 48 hours.
- Pass in the relevant token of the customer into the **Authorization** header with **Bearer** prefix.

> POST [**/api/v1/bookings/{bookingId}/cancel**] 
(http://localhost:8081/api/v1/bookings/4082519f-f378-4f21-931f-204924be29af/cancel) 

This will give the response of booking cancellation with booking status like below.
    
    {
        "bookingId": "4082519f-f378-4f21-931f-204924be29af",
        "bookingUserEmail": "persis114@gmail.com",
        "bookingMobileNumber": 992352,
        "bookedBy": "persis114@gmail.com",
        "bookingStatus": "CANCELLED",
        "eventId": 4,
        "numberOfTickets": 2,
        "bookingDateTime": "2024-03-27T07:23:12.832+00:00",
        "eventDetails": {
            "name": "Cutural Show",
            "venue": "Singapore",
            "eventDateTime": "2024-05-20T13:48:18.000+00:00",
            "eventType": "MUSIC_CONCERT"
        },
        "transactionDetail": {
            "transactionId": "56ed020a-a7fa-42bd-9373-0cbc9fd6ce6a",
            "amount": 400.0,
            "transactionStatus": "REFUNDED",
            "paymentMethod": "WALLET",
            "transactionDateTime": "2024-03-27T15:23:12.000+00:00",
            "transactionUpdateDateTime": "2024-03-27T13:47:36.350+00:00"
        },
        "createdDateTime": "2024-03-27T15:23:12.000+00:00",
        "lastModifiedDateTime": "2024-03-27T13:47:36.348+00:00"
    }



> This will refund the booking amount back to your wallet and can be verified using
> [**/api/v1/users/profile**] (http://localhost:8081/api/v1/users/profile) GET API.

This will give the response of user profile with updated amount in wallet like below.
    {
        "id": 7,
        "firstName": "persis",
        "lastName": "e",
        "mobileNumber": 12345,
        "email": "persis114@gmail.com",
        "role": "ROLE_CUSTOMER",
        "walletDetails": {
            "walletId": 54,
            "balance": 1000
        }
    }

An email will be sent with cancellation confirmation on booking id.
![alt text] src/main/resources/templates/booking-cancel.png



## Cancel the Event
To cancel an event, invoke the [**POST /api/v1/events/{eventId}/cancel**]
e.g (http://localhost:8081/api/v1/events/7/cancel) API.

This will cancel the event and all associated booking will be refunded if payment is done by WALLET.
Refund will be initiated to Wallets which are associated with booking.

This will give response of event cancellation
    {
        "id": 7,
        "name": "Cutural Show4",
        "venue": "Singapore",
        "eventDateTime": "2024-06-20T13:48:18.000+00:00",
        "ticketPrice": 200.0,
        "totalTickets": 120,
        "availableTickets": 120,
        "eventType": "MUSIC_CONCERT",
        "eventState": "CANCELLED",
        "createdBy": "eventmanager1@gmail.com",
        "createdDateTime": "2024-03-27T22:23:54.000+00:00",
        "lastModifiedDateTime": "2024-03-27T22:25:23.000+00:00"
    }

If a customer has booked for the event, a mail will be sent to them informing them of the cancellation

![alt text] src/main/resources/templates/event-cancel.png




