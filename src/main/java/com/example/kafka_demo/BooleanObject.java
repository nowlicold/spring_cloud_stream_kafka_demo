package com.example.kafka_demo;

import java.io.Serializable;

public class BooleanObject implements Serializable {
    private static final long serialVersionUID = -1117316519954926945L;
    private boolean value;

    public BooleanObject(boolean value) {
        this.value = value;
    }

    public void setBoolean(boolean value) {
        this.value = value;
    }

    public boolean booleanValue() {
        return this.value;
    }

}
