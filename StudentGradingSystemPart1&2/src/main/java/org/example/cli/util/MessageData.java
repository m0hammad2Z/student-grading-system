package org.example.cli.util;

public class MessageData implements java.io.Serializable {
    private Object content;
    private DataMode status;
    private boolean isRequired;

    public MessageData(Object content, DataMode status, boolean isRequired) {
        this.content = content;
        this.status = status;
        this.isRequired = isRequired;
    }

    public Object getContent() {
        return content;
    }

    public DataMode getStatus() {
        return status;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setStatus(DataMode status) {
        this.status = status;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }
}