# Software Requirements Specification

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification (SRS) defines the functional and non-functional requirements for the `social-media-backend` project. The system is a Spring Boot based REST backend for a social media application that manages users, posts, comments, likes, friendships, direct messages, notifications, and groups.

### 1.2 Scope
The backend provides API endpoints for:

- user management
- post creation and retrieval
- comment management
- like management
- friendship request workflows
- direct messaging between users
- notification management
- group management

The system persists application data using JPA/Hibernate and a relational database, with MySQL configured as the primary runtime database.

### 1.3 Intended Audience
This document is intended for:

- developers implementing or maintaining the backend
- testers validating API behavior
- project reviewers and faculty
- frontend developers integrating with the backend APIs

### 1.4 Definitions and Abbreviations

- `API`: Application Programming Interface
- `REST`: Representational State Transfer
- `SRS`: Software Requirements Specification
- `JPA`: Jakarta Persistence API
- `CRUD`: Create, Read, Update, Delete
- `UML`: Unified Modeling Language

## 2. Overall Description

### 2.1 Product Perspective
The application is a standalone backend service developed using Spring Boot 3.x and Java 21. It follows a layered architecture:

- `controller` layer for REST endpoints
- `service` layer for business logic
- `repository` layer for persistence
- `model` layer for domain entities
- `exception` layer for centralized error handling

### 2.2 Product Functions
At a high level, the backend supports the following features:

- create, update, delete, and retrieve users
- create, update, delete, and retrieve posts
- create, update, delete, and retrieve comments
- create, update, delete, and retrieve likes
- send, accept, and inspect friend requests
- send, update, delete, and retrieve direct messages
- create, update, delete, and retrieve notifications
- create, update, and retrieve groups
- handle missing-resource errors through a global exception handler

### 2.3 User Classes and Characteristics
The current backend does not implement explicit authentication or role-based access control. Conceptually, the system serves:

- `Application User`: creates content and interacts with other users
- `Administrator or Reviewer`: may inspect records during testing or demonstration
- `Frontend Client`: consumes the REST API on behalf of users

### 2.4 Operating Environment

- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL database
- Swagger/OpenAPI UI dependency present
- Maven build system

### 2.5 Design and Implementation Constraints

- backend framework is Spring Boot
- persistence is implemented through JPA repositories
- primary configured database is MySQL
- server port is configured as `8090`
- entity relationships rely on JPA annotations
- several endpoints currently use `POST` for update and delete operations, except where otherwise implemented
- no authentication, authorization, or JWT-based security is currently implemented

### 2.6 Assumptions and Dependencies

- a reachable relational database is available at runtime
- the frontend or client sends valid request payloads containing required nested object identifiers where needed
- Swagger UI and OpenAPI dependencies may be used for API exploration
- the backend is expected to run in a local development or academic project environment

## 3. System Features and Functional Requirements

### 3.1 User Management
The system shall provide user management operations.

Functional requirements:

- The system shall create a new user record.
- The system shall retrieve all users.
- The system shall retrieve a user by unique identifier.
- The system shall retrieve users by username.
- The system shall retrieve a user by email.
- The system shall update an existing user.
- The system shall delete a user by identifier.
- The system shall retrieve the owner of a given post.
- The system shall retrieve the owner of a given comment.

Primary classes:

- `UserController`
- `UserService`
- `UserRepository`
- `User`

### 3.2 Post Management
The system shall manage user posts.

Functional requirements:

- The system shall create a post for an existing user.
- The system shall retrieve all posts ordered by newest first.
- The system shall retrieve a post by identifier.
- The system shall retrieve posts created by a specific user.
- The system shall update post content.
- The system shall delete a post by identifier.

Primary classes:

- `PostController`
- `PostService`
- `PostRepository`
- `Post`

### 3.3 Comment Management
The system shall manage comments attached to posts.

Functional requirements:

- The system shall create a comment for an existing user and post.
- The system shall retrieve all comments.
- The system shall retrieve a comment by identifier.
- The system shall retrieve comments for a given post.
- The system shall retrieve comments written by a given user.
- The system shall sort comments by timestamp direction.
- The system shall update comment text.
- The system shall delete a comment by identifier.

Primary classes:

- `CommentController`
- `CommentService`
- `CommentRepository`
- `Comment`

### 3.4 Like Management
The system shall manage likes on posts.

Functional requirements:

- The system shall create a like record.
- The system shall retrieve all likes.
- The system shall retrieve a like by identifier.
- The system shall retrieve likes made by a specific user.
- The system shall retrieve likes for a specific post.
- The system shall update an existing like record.
- The system shall delete a like by identifier.

Primary classes:

- `LikeController`
- `LikeService`
- `LikeRepository`
- `Like`

### 3.5 Friendship Management
The system shall support friendship request workflows.

Functional requirements:

- The system shall send a friend request from one user to another.
- The system shall prevent a user from sending a friend request to themselves.
- The system shall avoid creating duplicate friendship records when a request already exists in either direction.
- The system shall retrieve all friendship records.
- The system shall retrieve a friendship by identifier.
- The system shall retrieve pending requests received by a user.
- The system shall retrieve pending requests sent by a user.
- The system shall allow accepting a friend request.
- The system shall allow unsending a pending friend request.
- The system shall allow removing an existing friendship record.
- The system shall determine whether two users are currently friends.

Primary classes:

- `FriendController`
- `FriendService`
- `FriendRepository`
- `Friend`

### 3.6 Messaging
The system shall support direct messages between users.

Functional requirements:

- The system shall send a message from a sender to a receiver.
- The system shall require both sender and receiver to be present in the request.
- The system shall retrieve all messages.
- The system shall retrieve a message by identifier.
- The system shall support retrieving a message through path or query parameter based lookup.
- The system shall update message text and optionally sender or receiver references.
- The system shall delete a message by identifier.
- The system shall retrieve messages by sender.
- The system shall retrieve messages by receiver.
- The system shall retrieve the conversation between two users.

Primary classes:

- `MessageController`
- `MessageService`
- `MessageRepository`
- `Message`

### 3.7 Notifications
The system shall manage notifications for users.

Functional requirements:

- The system shall create a notification.
- The system shall retrieve all notifications.
- The system shall retrieve a notification by identifier.
- The system shall retrieve notifications for a specific user.
- The system shall update notification content and ownership.
- The system shall delete a notification by identifier.

Primary classes:

- `NotificationController`
- `NotificationService`
- `NotificationRepository`
- `Notification`

### 3.8 Group Management
The system shall manage user-created groups.

Functional requirements:

- The system shall create a group for an existing admin user.
- The system shall enforce unique group names ignoring case.
- The system shall retrieve all groups.
- The system shall retrieve a group by identifier.
- The system shall retrieve groups by partial group name search.
- The system shall retrieve groups by admin user.
- The system shall update group name.
- The system shall optionally update the admin of a group.

Primary classes:

- `GroupController`
- `GroupService`
- `GroupRepository`
- `Group`

### 3.9 Error Handling
The system shall provide centralized exception handling for API failures.

Functional requirements:

- The system shall return a `404` response body when a requested resource is not found and a `ResourceNotFoundException` is thrown.
- The system shall return a `500` response body for unhandled exceptions.
- The system shall include timestamp, message, and status in handled error responses.

Primary classes:

- `GlobalExceptionHandler`
- `ResourceNotFoundException`

## 4. External Interface Requirements

### 4.1 User Interfaces
This backend does not provide a dedicated graphical user interface. It is intended to be consumed by:

- a frontend web client
- API testing tools such as Postman
- Swagger/OpenAPI UI

### 4.2 Hardware Interfaces
No specialized hardware interfaces are required.

### 4.3 Software Interfaces

- MySQL database for main persistence
- H2 database for testing
- HTTP clients consuming REST endpoints
- Maven for build lifecycle

### 4.4 Communications Interfaces

- Protocol: HTTP
- Data format: JSON
- Default configured port: `8090`
- Some controllers explicitly allow cross-origin access from `http://localhost:8080`

## 5. Data Requirements

### 5.1 Main Entities

- `User`
- `Post`
- `Comment`
- `Like`
- `Friend`
- `Message`
- `Notification`
- `Group`

### 5.2 Key Relationships

- One user can create many posts.
- One user can create many comments.
- One post can have many comments.
- One post can have many likes.
- One user can create many likes.
- One user can receive many notifications.
- One user can administer many groups.
- One user can send many messages.
- One user can receive many messages.
- One friendship record connects two users.

### 5.3 Persistence Rules

- entity identifiers use auto-generated integer primary keys
- schema updates are configured with `spring.jpa.hibernate.ddl-auto=update`
- timestamps are stored for posts, comments, likes, messages, and notifications

## 6. Non-Functional Requirements

### 6.1 Performance

- The system should provide normal CRUD response times for academic or small-scale social media usage.
- Repository-based retrieval should support efficient access for common lookups such as posts by user and messages between users.

### 6.2 Reliability

- The system should return structured error responses for missing resources and unhandled exceptions.
- The service layer should validate referenced entities before creating dependent records where implemented.

### 6.3 Maintainability

- The system should keep business logic in service classes and persistence logic in repositories.
- The project should remain modular by package separation.
- Lombok is used to reduce boilerplate in model classes.

### 6.4 Portability

- The application should run on any environment capable of Java 21 and Maven.
- Database portability is partially supported through JPA, though the current runtime configuration targets MySQL.

### 6.5 Security

- No authentication or authorization is currently implemented.
- Passwords appear to be stored as plain fields in the `User` entity.
- Sensitive runtime credentials are currently configuration-dependent and should be externalized in production.

## 7. Business Rules

- A user cannot send a friend request to themselves.
- A duplicate friendship request should not create a second record if a matching request already exists in either direction.
- A group name must be unique ignoring case.
- A post must belong to an existing user.
- A comment must belong to an existing user and an existing post.
- A message must include both sender and receiver.

## 8. Limitations and Observed Gaps

This section reflects the current implementation as analyzed from the codebase.

- Authentication and authorization are not implemented.
- Some service methods return `null` or generic runtime exceptions instead of consistent domain-specific exceptions.
- Delete support for groups is currently commented out.
- There is no visible file upload or media storage support.
- There is no pagination for list endpoints.
- Validation annotations on request models are minimal.
- Some controller methods use `POST` for update and delete instead of conventional REST verbs.
- CORS is configured at controller level for only some controllers; no central config class was found in the current source tree.

## 9. Future Enhancement Recommendations

- add authentication and authorization
- encrypt and safely manage user passwords
- add pagination and filtering for list endpoints
- standardize response formats across all controllers
- introduce DTOs instead of exposing entities directly
- add validation constraints and request validation handling
- implement group deletion if needed by the product scope
- externalize secrets and database credentials
- improve test coverage across service and controller layers

## 10. Traceability Summary

The implementation is primarily organized around these feature-to-class mappings:

- User Management -> `UserController`, `UserService`, `UserRepository`, `User`
- Post Management -> `PostController`, `PostService`, `PostRepository`, `Post`
- Comment Management -> `CommentController`, `CommentService`, `CommentRepository`, `Comment`
- Like Management -> `LikeController`, `LikeService`, `LikeRepository`, `Like`
- Friendship Management -> `FriendController`, `FriendService`, `FriendRepository`, `Friend`
- Messaging -> `MessageController`, `MessageService`, `MessageRepository`, `Message`
- Notifications -> `NotificationController`, `NotificationService`, `NotificationRepository`, `Notification`
- Group Management -> `GroupController`, `GroupService`, `GroupRepository`, `Group`
- Error Handling -> `GlobalExceptionHandler`, `ResourceNotFoundException`

## 11. Conclusion

The `social-media-backend` project implements a functional REST backend for a social media platform using Spring Boot and JPA. The current system is well suited for academic demonstration or foundational product development, with clear domain modules and layered architecture. The most important next steps for production readiness would be security, validation, pagination, and stronger API consistency.
