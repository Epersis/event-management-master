// Check login
if (!localStorage.getItem("customer_token")) {
    window.location.href = "../../login.html"
    alert("Please login")
}

// Back to Homepage
document.getElementById('back').addEventListener('click', () => {
    window.location.href = 'customer_ui/public/customer_home.html'
})

// Variables
let walletId = 0;
let payment_type = '';

//Data to pass in process_topup
let topup_amount = 0;

if (localStorage.getItem('customer_token') && localStorage.getItem('payment_type')) {
    
    // Order Data
    payment_type = (localStorage.getItem('payment_type'));
    customer_token = localStorage.getItem('customer_token');   
    topup_amount = parseInt(JSON.parse(localStorage.getItem('topup_amount')));
    
    walletId = JSON.parse(localStorage.getItem('walletId'));
    console.log(customer_token)

    console.log(payment_type)
    if (payment_type == 'topup') {
        process_topup(walletId, topup_amount, customer_token);
    }

}

// Process topup
async function process_topup(walletId, topup_amount, customer_token) {
    // Create headers object
    const headers = new Headers();
    headers.append('Authorization', `Bearer ${customer_token}`);
    fetch(`http://localhost:8081/api/v1/wallet/${walletId}/add/${topup_amount}`, {
        method: 'PUT',
        headers: headers
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


