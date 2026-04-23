package org.example.kickoff.view;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class Themes {

    private List<Category> categories = new ArrayList<>();
    private List<Theme> themes = new ArrayList<>();

    @PostConstruct
    public void init() {
        Category primeOne = new Category("PrimeOne");
        primeOne.getThemes().add(
                new Theme("saga-blue", "Saga Blue", "images/themes/saga.png", false));
        primeOne.getThemes().add(
                new Theme("vela-blue", "Vela Blue", "images/themes/vela.png", true));
        primeOne.getThemes().add(
                new Theme("arya-blue", "Arya Blue", "images/themes/arya.png", true));
        categories.add(primeOne);

        

        categories.forEach(category -> themes.addAll(category.getThemes()));
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public static class Category implements Serializable {

        private String name;
        private List<Theme> themes = new ArrayList<>();

        public Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Theme> getThemes() {
            return themes;
        }

        public void setThemes(List<Theme> themes) {
            this.themes = themes;
        }
    }

    public static class Theme implements Serializable {

        private String id;
        private String name;
        private String image;
        private boolean dark;

        public Theme(String id, String name, String image, boolean dark) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.dark = dark;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public boolean isDark() {
            return dark;
        }

        public void setDark(boolean dark) {
            this.dark = dark;
        }
    }
}