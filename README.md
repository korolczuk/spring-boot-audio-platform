# Audio Streaming Platform Backend

A robust, domain-driven backend system simulating a comprehensive audio streaming platform (akin to Spotify or Apple Music). 

**🎓 Academic Context:** This repository contains the implementation phase of the final project for the **Systems Modeling and Analysis (MAS)** course. The development was preceded by a rigorous analytical phase, including use-case definitions, activity diagrams, and comprehensive conceptual modeling using UML. 

The primary educational value of this project lies in solving the **Object-Relational Impedance Mismatch**. It demonstrates how to successfully translate highly complex Object-Oriented paradigms (like overlapping inheritance, tight composition, and dynamic state machines) into a Relational Database structure using Spring Data JPA and Hibernate.

---

## 🛠️ Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.x/4.x (Data JPA, Validation, Web) |
| **Database & ORM** | H2 Database (In-Memory), Hibernate |
| **Build & Boilerplate** | Maven, Lombok |
| **User Interface** | Java Swing |

---

## 🧠 Architectural Decisions & ORM Solutions

To maintain database normalization and data integrity, several advanced ORM strategies were implemented to bridge the gap between Java's object model and SQL tables:

* **Inheritance Mapping Strategies:** * **JOINED Strategy:** Applied to major hierarchies (`SystemUser` and `AudioContent`). Since subclasses participate in highly specific associations and contain numerous attributes, this prevents massive, unnormalized tables filled with `NULL` values.
  * **Overlapping Inheritance Workaround:** Java does not support multiple inheritance, which posed a challenge for a `MusicCreator` who is both a `Vocalist` and an `Instrumentalist`. This was solved using an `EnumSet<MusicCreatorRole>` flattened into the database, coupled with JPA lifecycle callbacks (`@PrePersist`, `@PreUpdate`) to validate role-specific attributes strictly.
* **Dynamic State Machine (AudioContent Lifecycle):** Initial analysis assumed a simple `changeStatus()` method. Dynamic analysis revealed this to be a security flaw. The system now utilizes a strict, closed state machine with six dedicated action methods (`approve()`, `reject()`, `ban()`, etc.). Each method internally verifies valid graph transitions and enforces `isAdmin` authorization, throwing `SecurityException` upon violation.
* **Global Parameterization (`SystemConfig`):** Instead of hardcoding base prices, the system uses a database-singleton pattern (`SystemConfig`). Subscription models calculate their dynamic costs by receiving this configuration as an argument, allowing immediate global price updates without touching the source code.
* **Composition & Transient Attributes:** Entities bound strictly to a parent's lifecycle (like `Address`) are injected directly as columns using `@Embedded`. Derived attributes (e.g., age verification `isMinor()` via `java.time.Period` or dynamic skip limits) are calculated on-the-fly using `@Transient` methods, ensuring zero data redundancy.

---

## 🏗️ Domain Model & Core Features

* **User Management:** Handles personal data, encrypted passwords, and dynamic age verification.
* **Polymorphic Subscriptions:** Features distinct pricing and validation logic for `Standard`, `Student` (university verification), and `Family` plans.
* **Content Abstraction:** Manages generic `AudioContent` shared features alongside concrete implementations for `Song` (genres, labels) and `PodcastEpisode` (tags).
* **Playlist Management:** A complex Many-to-Many relationship utilizing an association class (`PlaylistEntry`) to track exact timestamps of when a track was added to a user's playlist.
* **Strict Attribute Validation:** Extensive integration of Jakarta Validation. Custom constraints are combined with standard annotations (e.g., RegEx for names, `@PastOrPresent` for dates) to guarantee data integrity.
* **Minor Protection System:** An aggressive backend mechanism that blocks underage users from interacting with explicit content, throwing specific domain exceptions.

---

## 🚀 Initialization & GUI Demonstration

To facilitate testing and showcase the business rules without relying on external API calls, the project boots up with a pre-configured environment and a visual interface.

* **Automated Database Seeding:** Upon startup, a Spring `CommandLineRunner` populates the in-memory H2 database with a rich dataset (various users, distinct subscriptions, and real-world-inspired artists).
* **Desktop Interface (`PlaylistManagerWindow`):** Operates directly within the Spring Boot context. This Java Swing GUI allows users to browse playlists and interact with the system.
* **Real-time Feedback:** Business rule violations (such as a minor attempting to add an explicit track) are immediately caught by the backend and displayed via interactive `JOptionPane` alerts.

---

## ⚙️ How to Run

1. Clone the repository to your local machine:
   ```bash
   git clone [https://github.com/korolczuk/spring-boot-audio-platform.git](https://github.com/korolczuk/spring-boot-audio-platform.git)
   ```
2. Navigate to the project directory:
   ```bash
   cd spring-boot-audio-platform
   ```
3. Run the application using the included Maven Wrapper (no local Maven installation required):
   * On **Windows**:
     ```cmd
     mvnw.cmd spring-boot:run
     ```
   * On **macOS / Linux**:
     ```bash
     ./mvnw spring-boot:run
     ```
4. The H2 database will seed automatically, and the Java Swing GUI will appear, ready for interaction.
