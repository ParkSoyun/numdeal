function deleteTimedeal(productId) {
    $.ajax({
        type: "DELETE",
        url: `/timedeal/` + productId,
        success: function (response) {
            const message = response.message;

            alert(message);

            window.location.href = '/mypage/seller';
        }
    })
}