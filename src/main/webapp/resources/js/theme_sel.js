App = {
    init: function() {

        Storage.restoreSettings();
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

var Storage = {
    storageKey: 'primefaces',
    saveSettings: function(newsActive) {
        var now = new Date();
        var item = {
            settings: {
                newsActive: newsActive
            },
            expiry: now.getTime() + 604800000
        }
        localStorage.setItem(this.storageKey, JSON.stringify(item));
    },
    restoreSettings: function() {
        var itemString = localStorage.getItem(this.storageKey);
        if (itemString) {
            var item = JSON.parse(itemString);
            if (!this.isStorageExpired()) {
                // News
                App.changeNews(item.settings.newsActive);
            }
        }
    },
    isStorageExpired: function() {
        var itemString = localStorage.getItem(this.storageKey);
        if (!itemString) {
            return true;
        }
        var item = JSON.parse(itemString);
        var now = new Date();

        if (now.getTime() > item.expiry) {
            localStorage.removeItem(this.storageKey);
            return true;
        }

        return false;
    }
}

App.init();