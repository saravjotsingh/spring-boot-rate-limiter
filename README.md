# Spring Boot Rate Limiter

A robust and scalable rate limiting implementation built with Spring Boot and Redis. This rate limiter helps control API request rates using a sliding window algorithm, making it suitable for distributed systems and microservices architectures.

## Features

- Sliding Window Algorithm for precise rate limiting
- Redis-based distributed rate limiting
- Configurable rate limits and time windows
- Path-based exclusion support
- IP and User-Agent based rate limiting
- Standard rate limit headers support
- Easy integration with Spring Boot applications

## Technical Stack

- Java 21
- Spring Boot 3.x
- Redis
- Maven

## Quick Start

### Prerequisites

- JDK 21 or higher
- Redis Server
- Maven

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/rate-limiter.git
cd rate-limiter
```

2. Configure Redis connection in `application.properties`:
```properties
spring.redis.host=localhost
spring.redis.port=6379
```

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

## Configuration

The rate limiter can be configured through `application.properties`:

```properties
# Rate Limiter Configuration
rate-limit.enabled=true                    # Enable/disable rate limiting
rate-limit.max-requests=100                # Maximum requests allowed in the window
rate-limit.window-seconds=60               # Time window in seconds
rate-limit.algorithm=sliding-window        # Rate limiting algorithm
rate-limit.exclude-paths=/health           # Paths to exclude from rate limiting
```

## How It Works

### Sliding Window Algorithm

The implementation uses a sliding window algorithm with Redis for distributed rate limiting:

1. Each request is tracked using a combination of client IP and User-Agent
2. Requests are stored in Redis using sorted sets (ZSet)
3. Old requests outside the current window are automatically removed
4. New requests are checked against the configured limit
5. Rate limit headers are included in responses

### Rate Limit Headers

The API returns standard rate limit headers:

- `X-RateLimit-Limit`: Maximum number of requests allowed
- `X-RateLimit-Reset`: Time when the current window expires

### Response Codes

- `200 OK`: Successful request within limits
- `429 Too Many Requests`: Rate limit exceeded

## API Context Path

All API endpoints are served under the `/api` context path.

## Example Usage

```java
// The rate limiter is automatically applied to all endpoints except excluded paths
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @GetMapping
    public ResponseEntity<?> getResource() {
        // Your API logic here
        return ResponseEntity.ok("Resource data");
    }
}
```

## Error Response

When rate limit is exceeded:
```json
{
  "message": "API Limit Exceed."
}
```