function cal(id, closeTime) {
    let now = new Date().getTime();

    let gap = closeTime - now;
    let gapToSecond = Math.floor(gap / 1000);

    if(gapToSecond <= 10800) {
        if(!document.getElementById('time' + id).classList.contains('time-red')) {
            document.getElementById('time' + id).classList.add('time-red');
        }

        if(!document.getElementById('clock' + id).classList.contains('bi-red')) {
            document.getElementById('clock' + id).classList.add('bi-red');
        }
    }

    if(gapToSecond < 0) {
        document.getElementById('time' + id).innerText = "00:00:00:00";

        return 0;
    }

    let gapDay = String(Math.floor(gap / (1000 * 60 * 60 * 24))).padStart(2, "0");
    let gapHour = String(Math.floor((gap % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))).padStart(2, "0");
    let gapMin = String(Math.floor((gap % (1000 * 60 * 60)) / (1000 * 60))).padStart(2, "0");
    let gapSec = String(Math.floor((gap % (1000 * 60)) / 1000)).padStart(2, "0");

    document.getElementById('time' + id).innerText = gapDay + ":" + gapHour + ":" + gapMin + ":" + gapSec;
}