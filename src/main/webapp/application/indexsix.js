const publicKey = document.getElementById("mercado-pago-public-key").value;
const mercadopago = new MercadoPago(publicKey, {
  locale: 'pt-BR',
});	

function loadCardForm() {
    const productCost = document.getElementById('amount').value;

    const productDescription = document.getElementById('description').value;
    const payButton = document.getElementById("form-checkout__submit");
    const validationErrorMessages= document.getElementById('validation-error-messages');
	const userEmail = document.getElementById('useremail').value;
    const form = {
        id: "form-checkout",
        cardholderName: {
            id: "form-checkout__cardholderName",
            placeholder: "Titular do Cartão",
        },
      
        cardNumber: {
            id: "form-checkout__cardNumber",
            placeholder: "Número do Cartão",
            style: {
                fontSize: "1rem"
            },
        },
        expirationDate: {
            id: "form-checkout__expirationDate",
            placeholder: "MM/YYYY",
            style: {
                fontSize: "1rem"
            },
        },
        securityCode: {
            id: "form-checkout__securityCode",
            placeholder: "Código CNS",
            style: {
                fontSize: "1rem"
            },
        },
        installments: {
            id: "form-checkout__installments", 
            placeholder: "Parcelas",
        },
        identificationType: {
            id: "form-checkout__identificationType",
        },
        identificationNumber: {
            id: "form-checkout__identificationNumber",
            placeholder: "Identification number",
        },
        issuer: {
            id: "form-checkout__issuer",
            placeholder: "Issuer",
        },
    };

    const cardForm = mercadopago.cardForm({
        amount: productCost,
        iframe: true,
        form,
        callbacks: {
            onFormMounted: error => {
                if (error)
                    return console.warn("Form Mounted handling error: ", error);
                console.log("Form mounted");
            },
            onSubmit: event => {
                event.preventDefault();
                document.getElementById("loading-message").style.display = "block";

                const {
                    paymentMethodId,
                    issuerId,

                    amount,
                    token,
                    installments,
                    identificationNumber,
                    identificationType,
                } = cardForm.getCardFormData();

                fetch("/app/api/oauth2/card_payment", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        token,
                        issuerId,
                        paymentMethodId,
                        transactionAmount: Number(amount),
                        installments: Number(installments),
                        description: productDescription,
                        payer: {
                           	email: userEmail,
                            identification: {
                                type: identificationType,
                                number: identificationNumber,
                            },
                        },
                    }),
                })
                    .then(response => {
                        return response.json();
                    })
                    .then(result => {
                        if(!result.hasOwnProperty("error_message")) {
							
							window.location.assign('/app/application/index?gcmid=185&id=' + result.id);
	
                           // document.getElementById("success-response").style.display = "block";
                           // document.getElementById("payment-id").innerText = result.id;
                           // document.getElementById("payment-status").innerText = result.status;
                           // document.getElementById("payment-detail").innerText = result.detail;
                        } else {
                            document.getElementById("error-message").textContent = result.error_message;
                            document.getElementById("fail-response").style.display = "block";
                        }

                        $('.container__payment').fadeOut(500);
                        setTimeout(() => { $('.container__result').show(500).fadeIn(); }, 500);
                    })
                    .catch(error => {
                        alert("Unexpected error\n"+JSON.stringify(error));
                    });
            },
            onFetching: (resource) => {
                console.log("Fetching resource: ", resource);
                payButton.setAttribute('disabled', true);
                return () => {
                    payButton.removeAttribute("disabled");
                };
            },
            onCardTokenReceived: (errorData, token) => {
                if (errorData && errorData.error.fieldErrors.length !== 0) {
                    errorData.error.fieldErrors.forEach(errorMessage => {
                        alert(errorMessage);
                    });
                }

                return token;
            },
            onValidityChange: (error, field) => {
                //const input = document.getElementById(form[field].id);
                //removeFieldErrorMessages(input, validationErrorMessages);
                //addFieldErrorMessages(input, validationErrorMessages, error);
                //enableOrDisablePayButton(validationErrorMessages, payButton);
            }
        },
    });
};

function removeFieldErrorMessages(input, validationErrorMessages) {
    Array.from(validationErrorMessages.children).forEach(child => {
        const shouldRemoveChild = child.id.includes(input.id);
        if (shouldRemoveChild) {
            validationErrorMessages.removeChild(child);
        }
    });
}

function addFieldErrorMessages(input, validationErrorMessages, error) {
    if (error) {
        input.classList.add('validation-error');
        error.forEach((e, index) => {
            const p = document.createElement('p');
            p.id = `${input.id}-${index}`;
            p.innerText = e.message;
            validationErrorMessages.appendChild(p);
        });
    } else {
        input.classList.remove('validation-error');
    }
}

function enableOrDisablePayButton(validationErrorMessages, payButton) {
    if (validationErrorMessages.children.length > 0) {
        payButton.setAttribute('disabled', true);
    } else {
        payButton.removeAttribute('disabled');
    }
}

// Handle transitions
document.getElementById('checkout-btn').addEventListener('click', function(){
    $('.container__cart').fadeOut(500);
    setTimeout(() => {
        loadCardForm();
        $('.container__payment').show(500).fadeIn();
    }, 500);
});

document.getElementById('go-back').addEventListener('click', function(){
    $('.container__payment').fadeOut(500);
    setTimeout(() => { $('.container__cart').show(500).fadeIn(); }, 500);
});


//pix


// Handle transitions
document.getElementById('pix-btn').addEventListener('click', function(){
    $('.container__cart').fadeOut(500);
    sendPayment();
    setTimeout(() => {
       $('.container__result').show(500).fadeIn();
      
       
    }, 500);
});




function sendPayment() {
    const productCost = document.getElementById("amount").value;
    

 const productDescription = document.getElementById('description').value;
   
    const payerEmail = "webmaster@smartcloudio.com.br";
    
   
    
     const identificationType = document.getElementById("form-checkout__identificationType").value;
    const identificationNumber = document.getElementById("form-checkout__identificationNumber").value;

          
          
          

    fetch("/app/api/oauth2/pix_payment", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            transactionAmount: Number(productCost),
            description: productDescription,
            payer: {
                email: payerEmail,
             
                identification: {
                    type: identificationType,
                    number: identificationNumber,
                }
            },
        }),
    })
        .then(response => {
            return response.json();
        })
        .then(result => {
            if(!result.hasOwnProperty("error_message")) {
                document.getElementById("payment-id").innerText = result.id;
                document.getElementById("payment-status").innerText = result.status;
                document.getElementById("payment-detail").innerText = result.detail;

                document.getElementById("qr-code-image").src = `data:image/jpeg;base64,${result.qrCodeBase64}`;
                document.getElementById("pix-code").innerText = result.qrCode;

               // document.getElementById("success-response").style.display = "block";
               // document.getElementById("pix-code-section").style.display = "block";
            } else {
               // document.getElementById("error-message").innerText = result.error_message;
               // document.getElementById("fail-response").style.display = "block";
            }

            $('.container__payment').fadeOut(500);
            setTimeout(() => { $('.container__result').show(500).fadeIn(); }, 500);
        })
        .catch(error => {
            alert("Unexpected error\n"+JSON.stringify(error));
        });
};
          
          
 
    
    
    


