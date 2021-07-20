$(document).ready(function() {
    $(".active").removeClass("active");
    const pageURL = $(location).attr("href");
    if (pageURL.endsWith("subscribe")) {
        $("#subscribe-nav-link").addClass("active");
    } else if (pageURL.endsWith("account")) {
        $("#account-nav-link").addClass("active");
    } else if (pageURL.endsWith("authors")) {
        $("#authors-nav-link").addClass("active");
    }else if (pageURL.endsWith("admin")) {
        $("#admin-nav-link").addClass("active");
    } else {
        $("#home-nav-link").addClass("active");
    }
});