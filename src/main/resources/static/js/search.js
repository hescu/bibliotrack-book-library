 document.querySelectorAll('.addToWishlistButton').forEach(button => {
        button.addEventListener('click', addToWishlist);
    });

function addToWishlist(event) {
    const isbn = event.target.dataset.isbn;

    fetch('/my-library/wishlist/add', isbn, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {

        } else {

        }
    })
    .catch(error => {

    });
}