# MyLibrary Project

## Overview

MyLibrary is a web application designed for book enthusiasts, allowing users to search for books, add them to their wishlist, and manage their collection. The application provides a user-friendly interface for exploring book details and managing personal wishlists with ease.

## Features

- **User Authentication**: Secure login and registration system for users.
- **Book Search**: Users can search for books using a comprehensive search feature.
- **Wishlist Management**: Users can add books to their wishlist and view their wishlist at any time.
- **Authority Management**: Admin users can manage user roles and permissions.
- **User Management**: Admin users can delete user accounts, with safety checks in place to prevent self-deletion.

## Technologies Used

- **Spring Boot**: For creating the RESTful backend.
- **Thymeleaf**: For server-side rendering of HTML.
- **JavaScript & jQuery**: For handling AJAX requests and enhancing the frontend interactivity.
- **MyBatis & Spring Data JPA**: For database interaction.
- **MySQL**: As the database system.

## Getting Started

### Prerequisites

- Java 11 or later
- MySQL 5.7 or later
- Gradle

### Setup

1. Clone the repository:
   git clone https://github.com/hescu/bibliotrack.git
2. Navigate to the project directory:
   cd mylibrary
3. Update `src/main/resources/application.properties` with your MySQL user and password.
4. Build the project with Gradle:
   ./gradlew build
5. Run the application:
   java -jar target/mylibrary-0.0.1-SNAPSHOT.jar
6. Access the application at `http://localhost:8080`.

## Usage

- **Login/Register**: Start by registering a new account or logging in if you already have one.
- **Search for Books**: Use the search feature to find books you're interested in.
- **Add to Wishlist**: From the search results, add books to your wishlist using the provided button.
- **Manage Wishlist**: View and manage your wishlist from the MyLibrary page.
- **Admin Features**: If you're an admin, you can access user management features from the Admin Dashboard.

## Development Notes

- AJAX is used for adding books to the wishlist to ensure the page does not reload and the user experience remains seamless.
- Safety checks are in place to prevent admin users from deleting their own accounts.
- All user interactions with the database are handled through JpaRepositories and MyBatis mappers.

## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue for further discussion.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.

