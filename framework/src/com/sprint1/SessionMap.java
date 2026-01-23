package com.sprint1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SessionMap implements Map<String, Object> {
    
    private HttpServletRequest request;
    private Map<String, Object> changes = new HashMap<>(); // Pour suivre les modifications
    
    public SessionMap(HttpServletRequest request) {
        this.request = request;
    }
    
    private HttpSession getSession() {
        return request.getSession(true); // Crée la session si elle n'existe pas
    }
    
    private HttpSession getSessionIfExists() {
        return request.getSession(false); // Retourne null si pas de session
    }
    
    // === OPÉRATIONS DE BASE ===
    
    @Override
    public int size() {
        HttpSession session = getSessionIfExists();
        if (session == null) return 0;
        
        int count = 0;
        java.util.Enumeration<String> attrNames = session.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            attrNames.nextElement();
            count++;
        }
        return count;
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) return false;
        
        HttpSession session = getSessionIfExists();
        if (session == null) return false;
        
        return session.getAttribute((String) key) != null;
    }
    
    @Override
    public boolean containsValue(Object value) {
        HttpSession session = getSessionIfExists();
        if (session == null) return false;
        
        java.util.Enumeration<String> attrNames = session.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = attrNames.nextElement();
            if (session.getAttribute(attrName).equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Object get(Object key) {
        if (!(key instanceof String)) return null;
        
        HttpSession session = getSessionIfExists();
        if (session == null) return null;
        
        return session.getAttribute((String) key);
    }
    
    @Override
    public Object put(String key, Object value) {
        HttpSession session = getSession();
        Object oldValue = session.getAttribute(key);
        session.setAttribute(key, value);
        changes.put(key, value);
        return oldValue;
    }
    
    @Override
    public Object remove(Object key) {
        if (!(key instanceof String)) return null;
        
        HttpSession session = getSessionIfExists();
        if (session == null) return null;
        
        Object oldValue = session.getAttribute((String) key);
        session.removeAttribute((String) key);
        changes.remove(key);
        return oldValue;
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        HttpSession session = getSession();
        for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
            changes.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        HttpSession session = getSessionIfExists();
        if (session != null) {
            // On ne peut pas clear directement, on doit supprimer un par un
            java.util.Enumeration<String> attrNames = session.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = attrNames.nextElement();
                session.removeAttribute(attrName);
            }
        }
        changes.clear();
    }
    
    @Override
    public Set<String> keySet() {
        HttpSession session = getSessionIfExists();
        Set<String> keys = new java.util.HashSet<>();
        
        if (session != null) {
            java.util.Enumeration<String> attrNames = session.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                keys.add(attrNames.nextElement());
            }
        }
        
        return keys;
    }
    
    @Override
    public Collection<Object> values() {
        HttpSession session = getSessionIfExists();
        Collection<Object> values = new java.util.ArrayList<>();
        
        if (session != null) {
            java.util.Enumeration<String> attrNames = session.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = attrNames.nextElement();
                values.add(session.getAttribute(attrName));
            }
        }
        
        return values;
    }
    
    @Override
    public Set<Entry<String, Object>> entrySet() {
        HttpSession session = getSessionIfExists();
        Set<Entry<String, Object>> entries = new java.util.HashSet<>();
        
        if (session != null) {
            java.util.Enumeration<String> attrNames = session.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attrName = attrNames.nextElement();
                Object attrValue = session.getAttribute(attrName);
                entries.add(new java.util.AbstractMap.SimpleEntry<>(attrName, attrValue));
            }
        }
        
        return entries;
    }
    
    // === MÉTHODES UTILITAIRES ===
    
    public String getString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }
    
    public String getString(String key, String defaultValue) {
        Object value = get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    public Integer getInt(String key) {
        Object value = get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public Integer getInt(String key, int defaultValue) {
        Integer value = getInt(key);
        return value != null ? value : defaultValue;
    }
    
    public void increment(String key) {
        Integer current = getInt(key, 0);
        put(key, current + 1);
    }
    
    public void decrement(String key) {
        Integer current = getInt(key, 0);
        put(key, current - 1);
    }
    
    public boolean hasChanged(String key) {
        return changes.containsKey(key);
    }
    
    public Map<String, Object> getChanges() {
        return new HashMap<>(changes);
    }
    
    public void invalidate() {
        HttpSession session = getSessionIfExists();
        if (session != null) {
            session.invalidate();
        }
        changes.clear();
    }
    
    public String getId() {
        HttpSession session = getSessionIfExists();
        return session != null ? session.getId() : null;
    }
    
    public boolean isNew() {
        HttpSession session = getSessionIfExists();
        return session != null && session.isNew();
    }
    
    @Override
    public String toString() {
        return "SessionMap{size=" + size() + ", keys=" + keySet() + "}";
    }
}