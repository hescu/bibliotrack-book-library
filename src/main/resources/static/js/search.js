$(document).ready(function() {
    $('#addToWishlistButton').click(function() {
        var formData = $('#addToWishlistForm').serialize();
        $.ajax({
            type: 'POST',
            url: $('#addToWishlistForm').attr('action'),
            data: formData,
            success: function(response) {
                // Hier kannst du die Rückgabedaten verarbeiten, wenn die Anfrage erfolgreich war
                // Zum Beispiel könntest du eine Benachrichtigung anzeigen oder die Suchergebnisse aktualisieren
                console.log('Buch erfolgreich zur Wunschliste hinzugefügt.');
            },
            error: function(xhr, status, error) {
                // Hier kannst du Fehlerbehandlung durchführen, wenn die Anfrage fehlschlägt
                console.error('Fehler beim Hinzufügen des Buches zur Wunschliste:', error);
            }
        });
    });
});