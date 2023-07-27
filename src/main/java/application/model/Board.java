package application.model;

import java.time.LocalDate;

public class Board {
    private String writer;
    private LocalDate createdAt;
    private String title;
    private String contents;

    public Board(String writer, LocalDate createdAt, String title, String contents) {
        this.writer = writer;
        this.createdAt = createdAt;
        this.title = title;
        this.contents = contents;
    }

    public String getWriter() {
        return writer;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
