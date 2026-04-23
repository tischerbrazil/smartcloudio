package org.example.kickoff.view;




import org.primefaces.context.PrimeApplicationContext;


import java.io.Serializable;
import java.util.Collections;
import java.util.Locale;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;

@Named
@SessionScoped
public class App implements Serializable {

    @Inject private Themes themes;

    private String theme = "saga-blue";
    private boolean darkMode = false;
    private String inputStyle = "outlined";
    private Country locale = new Country(0, Locale.US);
    
    @PostConstruct
    public void init() {
        // Retrieve theme from cookie on application start or session creation
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Cookie cookie = (Cookie) facesContext.getExternalContext().getRequestCookieMap().get("userTheme");
        this.darkMode = false;
        if (cookie != null && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
            this.theme = cookie.getValue();
            if (this.theme.equals("arya-blue")) this.darkMode = true;
            
        } else {
            // Default theme if no cookie is found
            this.theme = "saga-blue"; 
        }
    }

    public String getTheme() {
        return theme;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getInputStyle() {
        return inputStyle;
    }

    public void setInputStyle(String inputStyle) {
        this.inputStyle = inputStyle;
    }

    public String getInputStyleClass() {
        return "filled".equals(this.inputStyle) ? "ui-input-filled" : "";
    }

    public Country getLocale() {
        return locale;
    }

    public void setLocale(Country locale) {
        this.locale = locale;
    }

    
    
    public void changeTheme(String theme, Boolean dark) {
    	int expiryDays = 30;
    	int maxAgeInSeconds = expiryDays * 24 * 60 * 60; // 30 days in seconds

    	Cookie userCookie = new Cookie("userTheme", theme);
    	userCookie.setMaxAge(maxAgeInSeconds);
    	userCookie.setPath("/"); // Optional: make it available across the app

    	FacesContext.getCurrentInstance().getExternalContext().addResponseCookie(
    		    userCookie.getName(), 
    		    userCookie.getValue(), 
    		    // Additional Map configuration for path, secure, etc. if needed
    		    Collections.singletonMap("path", "/") 
    		);
    	
    	
        
    	
    	
        this.theme = theme;
        this.darkMode = dark;
    }
    
    public void changeTheme(Themes.Theme theme) {
        this.theme = theme.getId();
        this.darkMode = theme.isDark();
    }

    public String getThemeName() {
        for (Themes.Theme theme : themes.getThemes()) {
            if (theme.getId().equals(this.getTheme())) {
                return theme.getName();
            }
        }
        return null;
    }

    public String getThemeImage() {
        for (Themes.Theme theme : themes.getThemes()) {
            if (theme.getId().equals(this.getTheme())) {
                return theme.getImage();
            }
        }
        return null;
    }

    public String getPrimeFacesVersion() {
        return PrimeApplicationContext.getCurrentInstance(FacesContext.getCurrentInstance()).getEnvironment().getBuildVersion();
    }
}