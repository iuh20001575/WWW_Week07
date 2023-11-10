const productAmounts = document.querySelectorAll('.product-quantity');
const productPrices = document.querySelectorAll('.product-price');
const totalPrices = document.querySelectorAll('.total-price');

const calcTotalPrice = () => {
    let sum = 0;

    productPrices.forEach((productPrice, index) => {
        sum += +productPrice.innerText * +(productAmounts[index].value) || 0;
    })

    totalPrices.forEach(totalPrice => totalPrice.innerText = `$ ${sum}`)
}

calcTotalPrice();