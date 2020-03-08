package com.mitteloupe.notebook.widget;

import java.util.UUID;

public interface UuidProvider {
    UUID uuid();

    class Fake implements UuidProvider {
        @Override
        public UUID uuid() {
            return UUID.fromString("0000-00-00-00-000000");
        }
    }
}
