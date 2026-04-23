App = {
    init: function() {
        
    },

    

    changeTheme: function(theme, dark) {
        PrimeFaces.changeTheme(theme);

        if (dark)
            $('#homepage-intro').addClass('introduction-dark');
        else
            $('#homepage-intro').removeClass('introduction-dark');
    },

    updateInputStyle: function(value) {
        if (value === 'filled')
            this.wrapper.addClass('ui-input-filled');
        else
            this.wrapper.removeClass('ui-input-filled');
    },

    
}

App.init();
