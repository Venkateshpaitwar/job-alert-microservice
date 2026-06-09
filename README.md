# Job Alert Microservice 

A Spring Boot microservice that automatically fetches remote job listings and sends personalized email alerts to subscribed users based on their keyword preferences.

## Features
- Subscribe/unsubscribe via REST API
- Fetches real job listings from Remotive API
- Keyword-based job matching
- Email alerts via Gmail SMTP
- Duplicate prevention using sent_alerts table
- Auto-scheduled every 6 hours

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Database | MySQL + Spring Data JPA |
| Email | JavaMailSender + Gmail SMTP |
| HTTP Client | WebClient (WebFlux) |
| Scheduler | Spring @Scheduled |
| Build Tool | Maven |

## Architecture
```
User → REST API → UserService → MySQL
                    ↓
Scheduler → JobFetcherService → Remotive API
                    ↓
            AlertService → EmailService → Gmail
```

### Setup

1. Clone the repo
```bash
git clone https://github.com/yourusername/job-alert-microservice.git
```

2. Create the database
```sql
CREATE DATABASE jobalert_db;
```

3. Configure environment variables
```
DB_URL=jdbc:mysql://localhost:3306/jobalert_db
DB_USERNAME=root
DB_PASSWORD=yourpassword
MAIL_USERNAME=yourgmail@gmail.com
MAIL_PASSWORD=your_app_password
```

4. Run the app
```bash
mvn spring-boot:run
```

5. Test the API at `http://localhost:8080`

## Upcoming Enhancements await