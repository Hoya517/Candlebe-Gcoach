jQuery(function($) {
    $(".main-header__right-icon").click(function(){
        $('.search_container').stop().slideToggle('fast');
        $('.main-header__right-input').stop().slideToggle('fast');
    });
});

jQuery(function($) {
    $(".play-header__right-icon").click(function(){
        $('.search_container').stop().slideToggle('fast');
        $('.play-header__right-input').stop().slideToggle('fast');
    });
});