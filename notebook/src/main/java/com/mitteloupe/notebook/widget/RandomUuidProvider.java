package com.mitteloupe.notebook.widget;

import java.util.UUID;

public class RandomUuidProvider implements UuidProvider {
    @Override
    public UUID uuid() {
        return UUID.randomUUID();
    }
}
