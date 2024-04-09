import express from 'express';
import dotenv from 'dotenv';
import stripe from 'stripe';

// Load variables
dotenv.config();

// Start Server
const app = express();

app.use(express.static('public'));
app.use(express.json());

// Stripe
let stripeGateway = stripe("sk_test_51Ot1e6P0edbtrzURjMHuMeT7O59FMpzCfrObVxw0q5sTNPCbnIHW33huqWHDK6FVPLSL2vNf7dzMWiCtji0XrrsZ00FIyn4HAh");

let DOMAIN = process.env.DOMAIN;

// Home Route
app.get('/', (req, res) => {
    res.sendFile("customer_home.html", {root: "public"});
})

//Bookings Route
app.get('/bookings', (req, res) => {
    res.sendFile("customer_booking.html", {root: "public"})
})

//Wallet Route
app.get('/wallet', (req, res) => {
    res.sendFile("customer_wallet.html", {root: "public"})
})
//Success Route
app.get('/success', (req, res) => {
    res.sendFile("success.html", {root: "public"})
})

//One-time payment
app.post('/one-time-payment', async (req, res) => {
    const total_amount = req.body.total_amount
    console.log(req.body)
    const lineItems = [{
            price_data: {
                currency: 'sgd',
                product_data: {
                    name: "Event Master Wallet Balance Top-up",
                },
                unit_amount: total_amount*100,
            },
            quantity: 1,
    }]

    // Create checkout session
    const session = await stripeGateway.checkout.sessions.create({
        payment_method_types: ['card'],
        mode: 'payment',
        success_url: `http://localhost:3000/success`,
        cancel_url: `http://localhost:3000/wallet`,
        line_items: lineItems,
    })
    res.json(session.url);
});

app.listen(3000, '0.0.0.0', () => {
    console.log('listening on port 3000;');
});
