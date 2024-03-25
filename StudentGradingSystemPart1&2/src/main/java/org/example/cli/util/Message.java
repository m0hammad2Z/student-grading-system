package org.example.cli.util;

import java.util.*;

public class Message implements java.io.Serializable {
        private final Map<String, MessageData> data;

        private Message(Builder builder) {
            this.data = builder.data;
        }

        public Map<String, MessageData> getData() {
            return data;
        }

        public void setData(Map<String, MessageData> data) {
            this.data.putAll(data);
        }

        public static class Builder {
            private final Map<String, MessageData> data = new LinkedHashMap<>();

            public Builder withData(String key, MessageData data) {
                this.data.put(key, data);
                return this;
            }

            public Builder withData(Map<String, MessageData> data) {
                this.data.putAll(data);
                return this;
            }

            public Message build() {
                if (data.isEmpty()) {
                    throw new IllegalStateException("No data");
                }
                return new Message(this);
            }
        }
    }

