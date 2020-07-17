package ru.otus.spring.course.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class User {
    private final String name;
    private final String surname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                surname.equals(user.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }
}
