# Car Wash Service Application

A full-stack on-demand car wash service application with role-based authentication for customers and service providers.

## Tech Stack

- **Frontend**: Angular 17 (Standalone Components)
- **Backend**: Spring Boot 3.2 Microservices
- **Service Discovery**: Eureka Server
- **API Gateway**: Spring Cloud Gateway
- **Database**: MySQL (separate DBs per service)
- **Authentication**: JWT
- **Testing**: JUnit 5, Jasmine/Karma
- **Code Quality**: SonarQube

## Features

### Authentication
- User registration with role selection (Customer/Service Provider)
- JWT-based authentication
- Role-based access control

### Customer Features
- Dashboard with service information
- Book car wash services (Basic, Premium, Full Detail)
- Track booking status
- View service history

### Service Provider Features
- Dashboard with booking statistics
- Manage incoming bookings (Accept/Reject)
- Track earnings and payments
- View customer feedback

## Setup Instructions

### Backend Setup (Microservices)
1. Navigate to backend directory: `cd backend`
2. Start all services: `start-all-services.bat`
   - Eureka Server (8761)
   - API Gateway (8080)
   - Auth Service (8081)
   - User Service (8082)
   - Booking Service (8083)
3. Configure MySQL password in application.yml files: `Sumit@56`

### Frontend Setup
1. Navigate to frontend directory: `cd frontend`
2. Install dependencies: `npm install`
3. Start development server: `npm start`
4. Access application at `http://localhost:4200`

### Database Setup
Databases are auto-created:
- carwash_auth (Auth Service)
- carwash_users (User Service)
- carwash_bookings (Booking Service)

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Code Quality
```bash
cd backend
mvn sonar:sonar
```

## Microservices Architecture

```
Car Wash App/
├── backend/
│   ├── eureka-server/      # Service Discovery (8761)
│   ├── api-gateway/        # API Gateway (8080)
│   ├── auth-service/       # Authentication (8081)
│   ├── user-service/       # User Management (8082)
│   ├── booking-service/    # Booking Management (8083)
│   └── start-all-services.bat
├── frontend/
│   └── src/app/
│       ├── components/     # Angular components
│       ├── services/       # Angular services
│       └── guards/         # Route guards
└── README.md
```

## Default Users

After running the application, you can create users through the signup page or use these test credentials:

**Customer Account:**
- Email: customer@test.com
- Password: password123

**Service Provider Account:**
- Email: provider@test.com
- Password: password123

## Contributing

1. Follow TDD approach
2. Maintain code coverage above 80%
3. Run SonarQube analysis before commits
4. Use conventional commit messages