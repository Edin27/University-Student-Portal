# University Self-Service Portal

*A modular, fully-tested academic portal built with Java, Maven, and IntelliJ IDEA*

## Overview

This project is a full implementation of an interactive **university self-service portal**, created as part of a software engineering coursework. It simulates a kiosk-based text interface that allows students, guests, teaching staff, and admin staff to interact with a centralised academic system.

The portal integrates functionality across:

* Authentication
* Course browsing and management
* FAQ navigation and maintenance
* Inquiry (support ticket) management
* Timetable construction and conflict checking
* Logging
* Robust unit and system testing

Designed with clean architecture, extensive testing, and an emphasis on maintainability, this project demonstrates engineering practices suitable for professional environments and internship applications.

---

## Features

### Multi-Role Access

* **Guest users**: explore FAQs and courses
* **Students**: manage timetable, choose activities, contact staff
* **Admin staff**: manage courses, FAQs, and staff inquiries
* **Teaching staff**: review and answer course-related inquiries

### Course System

* Add and remove courses
* View full course information
* Automated email notifications
* Activity types include lectures, tutorials, and labs
* Validation for course codes and required fields

### FAQ Management

* Hierarchical FAQ with nested topics
* Optional course-tagged questions
* Admin functionality to create and remove question–answer pairs
* Automatic topic cleanup when a topic becomes empty

### Timetable Manager

* Detects conflicts across weekly schedules
* Handles recorded vs. unrecorded lectures
* Validation of required tutorials and labs
* Clear success, warning, and error messages

### Inquiry / Support Ticket System

* Students and guests can submit inquiries
* Auto-routing to course teaching staff when applicable
* Admin reassignment functionality
* Persistent metadata for tracking inquiries

### Testing

* JUnit 5 unit tests for service and utility classes
* System-level tests that simulate end-to-end use cases
* High test coverage and method-level scenarios

---

## Architecture and Design

This project follows object-oriented design principles with a layered structure that separates:

* **Controllers** (interaction and input flow)
* **Model classes** (Course, FAQ, Timetable, etc.)
* **Service abstractions** (AuthenticationService, EmailService)
* **View layer** (via a text-based implementation)

Key design choices include:

* Singleton-based logging mechanism
* Interface-driven service design
* Clear separation of concerns
* Reusable, modular course and activity structures
* UML class and sequence diagrams guiding implementation choices

---

## Tech Stack

| Component | Technology    |
| --------- | ------------- |
| Language  | Java 14       |
| Build     | Maven         |
| IDE       | IntelliJ IDEA |
| Testing   | JUnit 5       |
| Mocking   | Mockito       |
| JSON      | json-simple   |
| Logging   | tinylog       |

---

## Project Structure

```
src/
 ├── main/
 │    ├── java/
 │    │     ├── controller/
 │    │     ├── model/
 │    │     ├── services/
 │    │     ├── view/
 │    │     └── Main.java
 │    └── resources/
 └── test/
      ├── unit/
      └── system/
```

---

## Testing Strategy

### Unit Tests

Focus areas include:

* Authentication logic
* Email validation
* Course and timetable validation methods

Tests include:

* Typical inputs
* Invalid inputs
* Boundary cases
* Error-handling behaviour

Each method is tested with a dedicated test case and clear naming conventions.

### System Tests

Each use case (e.g., adding a course, consulting the FAQ, or constructing a timetable) is tested through simulated user interaction.
These tests ensure:

* Correct flow across controllers
* Correct error/warning/success messaging
* State transitions are consistent and reliable
* No test relies on execution order

---

## Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/University-Student-Portal.git
```
### 2. Open and Run in IntelliJ IDEA

### OR

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/University-Student-Portal.git
```
### 2. Build the project 

```bash
cd University-Student-Portal
mvn clean compile
```

### 3. Run all tests

```bash
mvn test
```

### 4. Run the application

```bash
mvn exec:java -Dexec.mainClass="Main"
```

---

## Login
Usernames, passwords, emails and roles are given in the 
src\main\resources\MockUserData.json file.

---

## Notable Engineering Decisions

* Replaced custom logging with **tinylog** for reliability and readability
* Added assertions in internal methods during development to catch invalid states
* Refactored controllers to remove duplicated logic and enhance clarity
* Followed Google Java Style Guide conventions for consistency
* Designed course/activity parsing to support future extensibility


