package com.tencent.dingdang.tvs.message;

public class Header {
    private String namespace;
    private String name;

    public Header() {
        // For Jackson
    }

    public Header(String namespace, String name) {
        setNamespace(namespace);
        setName(name);
    }

    public final void setNamespace(String namespace) {
        if (namespace == null) {
            throw new IllegalArgumentException("Header namespace must not be null");
        }
        this.namespace = namespace;
    }

    public final void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Header name must not be null");
        }
        this.name = name;
    }

    public final String getNamespace() {
        return namespace;
    }

    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%1$s:%2$s", namespace, name);
    }
}
