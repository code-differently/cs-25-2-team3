package com.cliapp.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.cliapp.domain.Question.Option;
import org.junit.jupiter.api.Test;

class QuestionOptionBranchesTest {
    @Test
    void testOptionSettersAndGetters() {
        Option option = new Option();
        option.setId("id1");
        option.setCommand("cmd1");
        assertEquals("id1", option.getId());
        assertEquals("cmd1", option.getCommand());
    }

    @Test
    void testAdapterMethods() {
        Option option = new Option();
        option.setKey("key1");
        option.setValue("val1");
        assertEquals("key1", option.getKey());
        assertEquals("val1", option.getValue());
    }

    @Test
    void testConstructor() {
        Option option = new Option("id2", "cmd2");
        assertEquals("id2", option.getId());
        assertEquals("cmd2", option.getCommand());
    }
}
