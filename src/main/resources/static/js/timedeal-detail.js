function order(productId, stock, strOpenTime, strCloseTime) {
    let openTime = new Date(strOpenTime).getTime();
    let closeTime = new Date(strCloseTime).getTime();
    let now = new Date().getTime();

    if(openTime > now) {
        alert("오픈 예정 타임딜입니다.");

        location.href = "/timedeal/" + productId;
    } else if(closeTime < now) {
        alert("종료된 타임딜입니다.");

        location.href = "/timedeal/" + productId;
    } else if(stock === 0) {
        alert("품절된 상품입니다.");

        location.href = "/timedeal/" + productId;
    } else {
        $.ajax({
            type: "POST",
            url: `/order/` + productId,
            success: function (response) {
                const message = response.message;

                alert(message);

                window.location.href = '/mypage/user';
            }
        })
    }
}

function deleteTimedeal(productId) {
    $.ajax({
        type: "DELETE",
        url: `/timedeal/` + productId,
        success: function (response) {
            const message = response.message;

            alert(message);

            window.location.href = '/timedeal';
        }
    })
}