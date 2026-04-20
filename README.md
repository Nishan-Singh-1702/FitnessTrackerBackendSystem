## Overview
A Fitness-Monolith application built with Java and Spring Boot, 
covering core features including User Registration, User Management, Activity Management, 
and Recommendation Management.

## Architecture
Follows a clean layered structure:
- **Models** — each with a dedicated DTO class to avoid exposing 
  database structure directly to the frontend
- **Repositories** — separate repository for each model
- **Service Layer** — business logic
- **Controller Layer** — REST API endpoints

## Features
- Pagination and sorting on all applicable endpoints
- Global Exception Handler for consistent error responses
- JWT Authentication with HttpOnly Cookies for secure login
  (JavaScript cannot access HttpOnly cookies, preventing XSS attacks)
- Role-based access control with three roles:
  ROLE_USER and ROLE_ADMIN
