# Student Grading System

This project implements a Student Grading System in stages, showcasing an evolution in backend development practices.

## Functionality

The system offers features for managing students, courses, instructors, enrollments, assessments, and grades. Users can interact through various interfaces depending on the implementation stage.

## Stages

The project progresses through three distinct stages:

1. **Command-Line / Sockets and JDBC Backend:** This initial stage implements a command-line application for user interaction. Sockets enable communication, and JDBC facilitates database interaction using a layered architecture with DAO, Service, and Presentation layers.

2. **Web application using traditional MVC Servlets and JSPs:** The second stage transitions to a web application using Servlets and JSPs for the presentation layer. It leverages the service and data access layers from the previous stage, promoting code reuse. Security measures like password hashing, prepared statements, and session management are implemented.

3. **Web application using Spring MVC and Spring REST:** The final stage utilizes Spring MVC and Spring REST for a more robust web application. The layered architecture is maintained, but the Spring Framework streamlines development and enhances code maintainability. Spring Security handles user authentication and authorization, and Spring Data JPA simplifies database operations.

## Data Model

A consistent relational database model underpins all stages, encompassing tables for users, courses, instructors, enrollments, assessments, grades, and roles.

## Getting Started

The specific instructions for running the application will vary depending on the chosen implementation stage. Refer to the relevant project documentation for detailed setup guides.

## Technologies

* Java
* JDBC
* MySQL Database
* Sockets (Part 1)
* Servlets & JSPs (Part 2)
* Spring MVC & Spring REST (Part 3)
* Spring Security (Part 3)
* Spring Data JPA (Part 3)

## Conclusion

This project demonstrates the implementation of a Student Grading System using various technologies and architectural approaches. It highlights the progression from basic command-line interaction to a full-fledged web application with security considerations and data access abstraction.

## License
MIT License [Link to MIT License](https://opensource.org/licenses/MIT)
