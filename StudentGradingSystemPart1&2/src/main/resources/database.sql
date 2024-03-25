create database if not exists student_grading_system;

use student_grading_system;

create table if not exists roles (
    id int auto_increment primary key,
    name varchar(90) not null unique
);


create table if not exists users (
    id int auto_increment primary key,
    email varchar(90) not null unique,
    password varchar(255) not null,
    first_name varchar(90) not null,
    last_name varchar(90) not null,
    role_id int not null,
    foreign key (role_id) references roles(id) ON DELETE CASCADE
);

create table if not exists courses (
    id int auto_increment primary key,
    name varchar(90) not null unique
);

create table if not exists course_instructors (
    id int auto_increment primary key,
    course_id int not null,
    instructor_id int not null,
    foreign key (course_id) references courses(id) ON DELETE CASCADE,
    foreign key (instructor_id) references users(id) ON DELETE CASCADE
);

create table if not exists enrollments (
    id int auto_increment primary key,
    course_id int not null,
    student_id int not null,
    instructor_id int not null,
    foreign key (student_id) references users(id) ON DELETE CASCADE,
    foreign key (course_id) references courses(id) ON DELETE CASCADE,
    foreign key (instructor_id) references users(id) ON DELETE CASCADE
);

create table if not exists assessments (
    id int auto_increment primary key,
    course_id int not null,
    name varchar(90) not null,
    type enum('QUIZ', 'EXAM', 'ASSIGNMENT', 'PROJECT') not null,
    foreign key (course_id) references courses(id) ON DELETE CASCADE
);

create table if not exists grades (
    id int auto_increment primary key,
    assessment_id int not null,
    student_id int not null,
    grade decimal(5, 2),
    foreign key (assessment_id) references assessments(id) ON DELETE CASCADE,
    foreign key (student_id) references users(id) ON DELETE CASCADE,
    unique (assessment_id, student_id)
);



-- Default data for roles and permissions

insert into roles (name) values ('ADMIN'), ('INSTRUCTOR'), ('STUDENT');

-- Inserting data into the 'users' table
insert into users (email, password, first_name, last_name, role_id) values ('admin@sts.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Admin', 'Admin', 1);

INSERT INTO users (email, password, first_name, last_name, role_id) VALUES
('inst@example.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Instructor', 'Doe', 2),
('ints2@sts.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Instructor', 'sAeed', 2),
('inst3@instructor.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Instructor', 'Three', 2),
('inst4@inst.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Instructor', 'Four', 2),
('inst5@inst.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Instructor', 'Five', 2),
('john.doe@example.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'John', 'Doe', 3),
('jane.doe@example.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Jane', 'Doe', 3),
('mohammad@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Mohammad', 'Ibrahim', 3),
('a@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'A', 'A', 3),
('student1@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Student', 'One', 3),
('student2@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Student', 'Two', 3),
('student3@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Student', 'Three', 3),
('student4@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Student', 'Four', 3),
('student5@gmail.com', 'FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', 'Student', 'Five', 3);


-- Inserting data into the 'courses' table
INSERT INTO courses (name) VALUES
('Mathematics'),
('Physics'),
('Chemistry'),
('Biology'),
('Social Studies'),
('Java'),
('English'),
('Art'),
('Music'),
('Physical Education'),
('History'),
('Geography'),
('Computer Science'),
('Economics'),
('Business Studies'),
('Accounting'),
('Design and Technology'),
('Drama'),
('French'),
('Spanish');



-- Inserting data into the 'course_instructors' table
INSERT INTO course_instructors (course_id, instructor_id) VALUES
(1, 2),
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(10, 3),
(11, 4),
(12, 4),
(13, 4),
(14, 4),
(15, 4),
(16, 5),
(17, 5),
(18, 5),
(19, 5),
(20, 5);

-- Inserting data into the 'enrollments' table
INSERT INTO enrollments (course_id, student_id, instructor_id) VALUES
(1, 11, 2),
(1, 12, 2),
(1, 13, 2),
(1, 14, 2),
(1, 15, 2),
(2, 11, 2),
(2, 12, 2),
(2, 13, 2),
(2, 14, 2),
(2, 15, 2),
(3, 11, 2),
(3, 12, 2),
(3, 13, 2),
(3, 14, 2),
(3, 15, 2),
(4, 11, 2),
(4, 12, 2),
(4, 13, 2),
(4, 14, 2),
(4, 15, 2);

-- Inserting data into the 'assessments' table
INSERT INTO assessments (course_id, name, type) VALUES
(1, 'Quiz 1', 'QUIZ'),
(1, 'Quiz 2', 'QUIZ'),
(1, 'Quiz 3', 'QUIZ'),
(1, 'Quiz 4', 'QUIZ'),
(1, 'Quiz 5', 'QUIZ'),
(2, 'Quiz 1', 'QUIZ'),
(2, 'Quiz 2', 'QUIZ'),
(2, 'Quiz 3', 'QUIZ'),
(2, 'Quiz 4', 'QUIZ'),
(2, 'Quiz 5', 'QUIZ'),
(3, 'Quiz 1', 'QUIZ'),
(3, 'Quiz 2', 'QUIZ'),
(3, 'Quiz 3', 'QUIZ'),
(3, 'Quiz 4', 'QUIZ'),
(3, 'Quiz 5', 'QUIZ'),
(4, 'Quiz 1', 'QUIZ'),
(4, 'Quiz 2', 'QUIZ'),
(4, 'Quiz 3', 'QUIZ'),
(4, 'Quiz 4', 'QUIZ'),
(4, 'Quiz 5', 'QUIZ');

-- Inserting data into the 'grades' table
INSERT INTO grades (assessment_id, student_id, grade) VALUES
(1, 11, 90),
(1, 12, 80),
(1, 13, 70),
(1, 14, 60),
(1, 15, 50),
(2, 11, 90),
(2, 12, 80),
(2, 13, 70),
(2, 7, 60),
(2, 15, 50),
(3, 11, 90),
(3, 12, 80),
(3, 13, 70),
(3, 14, 60),
(3, 15, 50),
(4, 11, 90),
(4, 12, 80),
(4, 13, 70),
(4, 14, 60),
(4, 15, 50);