function moveToPage(page) {
    const regex = /page=[0-9]{1,}/g;
    var url = window.location.href;
    var changed = url.match(regex);

    url = createUrl("page", page, changed, url);

    window.location.href = url;
}

function createUrl(key, value, changed, url){
    if(!url.includes("?")) {
        url += "?status=진행중&" + key + "=" + value;
    } else {
        url = url.replace(changed, key + "=" + value);
    }

    return url;
}