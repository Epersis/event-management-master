<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Event Booking Confirmation</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-image: url('img.png'); /* Replace 'ticket_background.jpg' with your actual image file */
            background-size: cover;
            font-family: Arial, sans-serif;
            color: #ffffff;
        }
        .container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
            background-color: rgba(0, 0, 0, 0.7); /* Background color with transparency */
        }
        h1, p {
            text-align: center;
        }
        p {
            font-size: 18px;
            line-height: 1.6;
        }
        strong {
            color:blueviolet;
        }
    </style>
</head>
<body>
<h1>Event Booking Confirmation</h1>

<p>Dear,</p>

<p>We're thrilled to confirm your booking for the upcoming event:</p>

<p><strong>Event Name:</strong> ${eventname}</p>
<p><strong>Date:</strong> ${eventdate}</p>
<p><strong>Location:</strong> ${eventvenue}</p>

<p>Please find the details of your booking below:</p>

<p><strong>Booking ID:</strong> ${bookingid}</p>
<p><strong>Number of Tickets:</strong> ${bookingNumTickets}</p>
<p><strong>Ticket IDs:</strong> ${ticketIds}</p>

<p>Thank you for choosing to attend our event. We look forward to seeing you there!</p>

<p>Best regards,</p>
<p>The Event Management Team</p>
</body>
</html>
