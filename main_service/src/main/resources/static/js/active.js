$(document).ready(function() {
    $(".active").removeClass("active");
    const pageURL = $(location).attr("href");
    if (pageURL.endsWith("account")) {
        $("#account-nav-link").addClass("active");
    } else {
        $("#home-nav-link").addClass("active");
    }
});