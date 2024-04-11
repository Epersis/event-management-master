export async function createOneTimePayment(totalAmount) {
    const stripe = require('stripe');
    const stripeGateway = stripe("sk_test_51Ot1e6P0edbtrzURjMHuMeT7O59FMpzCfrObVxw0q5sTNPCbnIHW33huqWHDK6FVPLSL2vNf7dzMWiCtji0XrrsZ00FIyn4HAh");

    const lineItems = [{
        price_data: {
            currency: 'sgd',
            product_data: {
                name: "Event Master Wallet Balance Top-up",
            },
            unit_amount: totalAmount * 100,
        },
        quantity: 1,
    }];

    // Create checkout session
    const session = await stripeGateway.checkout.sessions.create({
        payment_method_types: ['card'],
        mode: 'payment',
        success_url: `http://localhost:3000/success`,
        cancel_url: `http://localhost:3000/wallet`,
        line_items: lineItems,
    });

    return session.url;
}