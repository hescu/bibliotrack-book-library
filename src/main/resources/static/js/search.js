$(document).ready(function() {
    $('#addToWishlistButton').click(function() {
        var formData = $('#addToWishlistForm').serialize();
        $.ajax({
            type: 'POST',
            url: $('#addToWishlistForm').attr('action'),
            data: formData,
            success: function(response) {
                showNotification(response, 'success');
            },
            error: function(xhr, status, error) {
                var errorMessage = xhr.responseText || 'An error occurred.';
                showNotification(errorMessage, 'error');
            }
        });
    });
});

function showNotification(message, type) {
        var notificationDiv = $('#notification');
        notificationDiv.removeClass(); // Remove any existing classes
        notificationDiv.text(message); // Set the message text

        // Set the appropriate class for styling
        if (type === 'success') {
            notificationDiv.addClass('success');
        } else if (type === 'error') {
            notificationDiv.addClass('error');
        }

        // Show the notification
        notificationDiv.show();

        // Hide the notification after a certain time (e.g., 5 seconds)
        setTimeout(function() {
            notificationDiv.hide();
        }, 5000); // 5000 milliseconds = 5 seconds
    }