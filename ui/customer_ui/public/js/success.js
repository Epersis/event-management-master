// Back to Homepage
document.getElementById('back').addEventListener('click', () => {
    window.location.href = 'customer_home.html'
})

// Variables
let user = {};
let payment_type = '';

//Data to pass in process_topup
let topup_amount = 0;

if (localStorage.getItem('customer_token') && localStorage.getItem('payment_type')) {
    
    // Order Data
    payment_type = (localStorage.getItem('payment_type'));
    customer_token = JSON.parse(localStorage.getItem('customer_token'));   
    topup_amount = JSON.parse(localStorage.getItem('topup_amount'));

    requestBody = {

    };

    console.log(payment_type)
    if (payment_type == 'topup') {
        process_topup()
    }

}

// Process topup
async function process_topup() {
    fetch('/process-topup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            topup_amount: topup_amount,
            user_id: user_id
        })
        })
        .then(response => {
            if (response.ok) {
                console.log('Top-up successfully');
                return response.json(); // Parse response body as JSON
            } else {
                console.error('Failed to Top-up:', response.statusText);
            }
        })
        .catch(error => {
            console.error('Error Topping-up:', error);
        });

    // Rmb to remove these data after the function is triggered so that it won't trigger again on page reload
    localStorage.removeItem('payment_type');
    localStorage.removeItem('topup_amount')
}


