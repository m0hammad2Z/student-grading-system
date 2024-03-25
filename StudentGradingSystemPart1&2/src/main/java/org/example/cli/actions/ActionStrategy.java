package org.example.cli.actions;

import org.example.cli.util.Message;

public interface ActionStrategy{
    Message execute(Message message);
    Message getPreparedData();
}
