package com.sprint1;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String view;
    private Map<String, Object> data = new HashMap<>();

    // Constructeur vide (déjà existant probablement)
    public ModelView() {}

    // AJOUTE CE CONSTRUCTEUR
    public ModelView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    // Méthode pratique (optionnelle mais cool)
    public ModelView add(String key, Object value) {
        data.put(key, value);
        return this;
    }
}