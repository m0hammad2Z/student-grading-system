package org.example.util;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    private final List<Runnable> validationTasks = new ArrayList<>();

    List<String> errors = new ArrayList<>();


    public Validator validEmail(String email) {
        validationTasks.add(() -> {
            if(email == null || email.isEmpty()) {
                errors.add("Email cannot be null");
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add("Invalid email");
            }
        });
        return this;
    }

    public Validator validPassword(String password) {
        validationTasks.add(() -> {
            if(password == null || password.isEmpty()) {
                errors.add("Password cannot be null");
            }
            if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                errors.add("Invalid password");
            }
        });
        return this;
    }

    public Validator validName(String name, String message) {
        validationTasks.add(() -> {
            if(name == null || name.isEmpty()) {
                errors.add("Name cannot be null");
            }
            if (name.length() < 2) {
               errors.add("Invalid name");
            }
        });
        return this;
    }

    public Validator validName(String name) {
        validationTasks.add(() -> {
            if(name == null || name.isEmpty()) {
                errors.add("Name cannot be null");
            }
            if (name.length() < 2) {
                errors.add("Invalid name");
            }
        });
        return this;
    }

    public Validator validNumber(int number, int min, int max, String message) {
        validationTasks.add(() -> {
            if (!(number >= min && number <= max)) {
                errors.add(message);
            }
        });
        return this;
    }

    public Validator validNumber(int number, int min, int max) {
        validationTasks.add(() -> {
            if (!(number >= min && number <= max)) {
                errors.add("Invalid number");
            }
        });
        return this;
    }


    public Validator validObject(Object object, String message) {
        validationTasks.add(() -> {
            if (object == null) {
                errors.add(message);
            }
        });
        return this;
    }

    public Validator validObject(Object object) {
        validationTasks.add(() -> {
            if (object == null) {
                errors.add("Object cannot be null");
            }
        });
        return this;
    }

    public Validator validId(int id, String message) {
        validationTasks.add(() -> {
            if (id <= 0) {
                errors.add(message);
            }
        });
        return this;
    }

    public Validator validId(int id) {
        validationTasks.add(() -> {
            if (id <= 0) {
                errors.add("Invalid id");
            }
        });
        return this;
    }
    public Validator validEnum(Enum<?> value, String message) {
        validationTasks.add(() -> {
            if (value == null) {
                errors.add(message);
            }
        });
        return this;
    }


    public void validate(){
        for (Runnable task : validationTasks) {
            task.run();
        }

        if(!errors.isEmpty()){
            String errorMessage = String.join(", ", errors);
            validationTasks.clear();
            errors.clear();
            throw new IllegalArgumentException(errorMessage);
        }

        validationTasks.clear();
        errors.clear();
    }
}