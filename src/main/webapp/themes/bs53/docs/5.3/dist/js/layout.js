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

    isMenuButton: function(element) {
        return $.contains(this.menuButton.get(0), element) || this.menuButton.is(element);
    },

    restoreMenu: function() {
        var activeRouteLink = this.menuLinks.filter('[href^="' + window.location.pathname + '"]');
        if (activeRouteLink.length) {
            activeRouteLink.addClass('router-link-active');
        }

        var activeSubmenus = sessionStorage.getItem('active_submenus');
        if (activeSubmenus) {
            this.activeSubmenus = activeSubmenus.split(',');
            this.activeSubmenus.forEach(function(id) {
                $('#' + id).addClass('submenu-link-active').next().show();
            });
        }

        var scrollPosition = sessionStorage.getItem('scroll_position');
        if (scrollPosition) {
            this.menu.scrollTop(parseInt(scrollPosition));
        }
    },

    onSearchClick: function(event, id) {
        if (id && this.activeSubmenus.indexOf(id) === -1) {
            this.activeSubmenus.push(id);
            $('#' + id).next().show();
            sessionStorage.setItem('active_submenus', this.activeSubmenus.join(','));
        }
    },
    
    _bindNews: function() {
        if (this.news && this.news.length > 0) {
            var $this = this;
            var closeButton = this.news.find('.layout-news-close');
            closeButton.off('click.news').on('click.news', function() {
                $this.wrapper.removeClass('layout-news-active');
                $this.news.hide();
                
                Storage.saveSettings(false);
            });
        }
    },
    
    changeNews: function(active) {
        if (this.news && this.news.length > 0) {
            if (active)
                this.wrapper.addClass('layout-news-active');
            else 
                this.wrapper.removeClass('layout-news-active');
        }
    }
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
