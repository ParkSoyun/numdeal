function signOut() {
    $.ajax({
        type: "POST",
        url: "/signout",
        success: function (response) {
            const message = response.message;

            alert(message);

            window.location.href = '/timedeal';
        }
    })
}