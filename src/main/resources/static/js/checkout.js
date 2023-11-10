const prices = document.querySelectorAll('.price')
const totalPrices = document.querySelectorAll('.total-price')

const calcTotalPrice = () => {
    let sum = [...prices].reduce((prev, price) => prev + +price.innerText, 0);

    totalPrices.forEach(totalPrice => totalPrice.innerText = `$ ${sum}`)
}

calcTotalPrice();