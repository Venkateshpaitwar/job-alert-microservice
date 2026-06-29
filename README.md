# Job Alert Microservice 🚀

A production-grade Spring Boot microservice that automatically fetches remote job listings and delivers personalized email alerts to subscribed users based on keyword preferences — secured with JWT, powered by RabbitMQ async messaging, Redis caching, and fully containerized with Docker Compose.

## Features
- JWT secured REST APIs with register and login
- Fetches real job listings from Remotive API
- Keyword-based job matching engine
- Email alerts via Gmail SMTP with HTML templates
- Async alert processing via RabbitMQ
- Redis caching to reduce redundant database queries
- Duplicate prevention using a 3-table MySQL schema
- Auto-scheduled job fetch every 6 hours
- Global exception handler with clean error responses
- Swagger UI API documentation
- Fully containerized with Docker Compose

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Database | MySQL + Spring Data JPA |
| Authentication | JWT + Spring Security |
| Messaging | RabbitMQ |
| Caching | Redis |
| Email | JavaMailSender + Gmail SMTP |
| HTTP Client | WebClient (WebFlux) |
| Scheduler | Spring @Scheduled |
| Documentation | Swagger / OpenAPI |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |

## Architecture

```
User → REST API (JWT secured)
         │
         ├── UserService → MySQL
         │
         ├── JobFetcherService → Remotive API
         │        └── JobPostingService → Redis Cache → MySQL
         │
         └── Scheduler → RabbitMQ (Producer)
                              │
                         RabbitMQ (Consumer)
                              │
                         AlertService → EmailService → Gmail
```

## API Endpoints

| Method | URL | Auth | Description |
|---|---|---|---|
| POST | /api/auth/register | ❌ | Register with email + keywords |
| POST | /api/auth/login | ❌ | Login and get JWT token |
| GET | /api/users | ✅ | Get all subscribers |
| DELETE | /api/users/unsubscribe/{email} | ✅ | Unsubscribe by email |
| POST | /api/jobs/fetch/{keyword} | ✅ | Manually fetch jobs from API |
| GET | /api/jobs | ✅ | Get all saved jobs (Redis cached) |
| POST | /api/jobs/trigger-alerts | ✅ | Manually trigger alerts via RabbitMQ |

## Getting Started

### Prerequisites
- Docker Desktop

### Run with Docker Compose

1. Clone the repo
```bash
git clone https://github.com/Venkateshpaitwar/job-alert-microservice.git
cd job-alert-microservice/job-alert-service
```

2. Create `.env` file
```
DB_URL=jdbc:mysql://mysql:3306/jobalert_db
MYSQL_USERNAME=root
MYSQL_PASSWORD=yourpassword
MAIL_USERNAME=yourgmail@gmail.com
MAIL_PASSWORD=your_app_password
JWT_SECRET=your-secret-key-minimum-32-characters-long
REDIS_HOST=redis
```

3. Start the stack
```bash
docker-compose up --build
```

4. Open Swagger UI at `http://localhost:8080/swagger-ui.html`

### Local Development (IntelliJ)

Add these to **Edit Configurations → Environment Variables**:
```
DB_URL=jdbc:mysql://localhost:3306/jobalert_db
MYSQL_USERNAME=root
MYSQL_PASSWORD=yourpassword
MAIL_USERNAME=yourgmail@gmail.com
MAIL_PASSWORD=your_app_password
JWT_SECRET=your-secret-key
REDIS_HOST=localhost
SPRING_RABBITMQ_HOST=localhost
```

Start RabbitMQ and Redis:
```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -d --name redis -p 6379:6379 redis:7-alpine
```

Then run from IntelliJ normally.

## How It Works

1. User registers with email, keywords and alert frequency
2. Scheduler triggers every 6 hours fetching jobs from Remotive API
3. New jobs are saved to MySQL — duplicates skipped automatically
4. A message is published to RabbitMQ queue
5. RabbitMQ consumer picks up the message and runs alert processing
6. AlertService matches job titles against each user's keywords
7. Matching unalerted jobs are emailed to the user
8. `GET /api/jobs` results are cached in Redis for 1 hour reducing DB load