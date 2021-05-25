package ru.specialist.db.Repository;

import java.io.IOException;
import java.io.IOException;

import ru.specialist.db.dal.*;

public class DbRepository {
    public static void main(String[] args) throws IOException {
        try (CourseRepository db = new CourseRepositoryJdbcImpl()) {
            for (Course c : db.findAll())
                System.out.printf("%-15s %d\n", c.getTitle(),
                        c.getLength());
        }
    }
}
