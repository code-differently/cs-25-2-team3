package com.cliapp.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AppConfigTest {
    @Test
    void testSettersAndGetters() {
        AppConfig config = new AppConfig();
        config.setName("App");
        config.setVersion("1.0");
        config.setDescription("desc");
        config.setAuthor("author");
        Map<String, Object> settings = new HashMap<>();
        settings.put("theme", "dark");
        config.setSettings(settings);
        assertEquals("App", config.getName());
        assertEquals("1.0", config.getVersion());
        assertEquals("desc", config.getDescription());
        assertEquals("author", config.getAuthor());
        assertEquals(settings, config.getSettings());
    }

    @Test
    void testSettingMethods() {
        AppConfig config = new AppConfig();
        config.setSetting("theme", "dark");
        assertEquals("dark", config.getSetting("theme"));
        config.setSetting("volume", 10);
        assertEquals(10, config.getSetting("volume"));
    }
}
