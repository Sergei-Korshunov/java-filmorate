package ru.yandex.practicum.filmorate.storage.db.repository;

public enum FriendshipStatus {
    ACCEPTED("accepted"),
    NOT_ACCEPTED("not-accepted");

    private final String textView;

    FriendshipStatus(String textView) {
        this.textView = textView;
    }

    public String getTextView() {
        return textView;
    }
}
