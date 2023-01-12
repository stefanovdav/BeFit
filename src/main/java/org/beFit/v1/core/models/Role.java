package org.beFit.v1.core.models;

public enum Role {
    USER(1), ADMIN(2);

    public final int id;

    Role(int id) {
        this.id = id;
    }

    static Role fromID(int id) {
        return switch (id) {
            case 1 -> USER;
            case 2 -> ADMIN;
            default -> throw new RuntimeException("invalid role id");
        };
    }
}
