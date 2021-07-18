$('body').on('mouseup', '.song-box', function (e) {
    let elementsWithId = document.querySelectorAll("#" + e.currentTarget.id);
    document.getElementById("logo-music-player").setAttribute("src", elementsWithId[1].src);
    document.getElementById("title-music-player").innerHTML = elementsWithId[2].innerHTML;
    document.getElementById("musicians-music-player").innerHTML = elementsWithId[3].innerHTML;
    if (document.getElementById("has-subscriber-role-input") !== null) {
        document.getElementById("duration-music-player").innerHTML = elementsWithId[4].innerHTML;
    } else {
        document.getElementById("duration-music-player").innerHTML = "0:30";
    }
    $("#music-player-navbar").removeClass("invisible");
});