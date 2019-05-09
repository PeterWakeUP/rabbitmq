package com.shw.common.DTO;

import java.io.Serializable;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String messageId;  //消息发送的唯一标识

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
