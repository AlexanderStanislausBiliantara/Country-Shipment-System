# Customs Check Dashboard

## ️ Tech Stack
- **Frontend:** React, TypeScript, Vite, CSS
- **Backend:** Java, Spring Boot, Spring Data JPA
- **Database:** PostgreSQL
- **Infrastructure:** Docker, Docker Compose

##  Features Implemented
- **CRUD Operations:** Create and list customs checks via a clean, responsive dashboard.
- **Country Caching:** Country data (Region, Subregion) is fetched from the external REST Countries API and cached in the local database to minimize external calls.
- **Graceful Fallback:** If the external API is unreachable and the country is not cached, the system gracefully saves the check with an `UNKNOWN` tier instead of crashing.
- **Data Validation:** Input validation on both the frontend and backend to ensure only valid 2-letter ISO country codes are processed.
- **Tier Logic:** Automatically calculates `REGIONAL`, `CONTINENTAL`, or `INTERNATIONAL` tiers based on the origin and destination subregions/regions.

## Running the Project

### Option 1: Docker
The easiest way to run the entire stack (Frontend, Backend, and Database) is using Docker Compose.

1. Ensure Docker is installed and running on your machine.
2. Clone this repository and navigate to the root directory.
3. Run the following command:
   ```bash
   docker-compose up --build
4. Once the containers are running, open your browser and navigate to:
    Frontend: http://localhost:3000
    Backend API: http://localhost:8080

### Option 2: Local Development
**Prerequisites:** Java 17+, Maven, Node.js 18+, PostgreSQL

1. Ensure PostgreSQL is running on localhost:5432 and update credentials in `backend/src/main/resources/application.properties`.
2. `cd backend`
   `mvn spring-boot:run`
3. `cd frontend`
   `npm install`
   `npm run dev`

## Assumptions & Design Decisions
1. The external API used in this project is from REST Countries. The reason for this choice is due to the fact that the Nager.Date free API does not contain the subregion data for countries, meaning it is impossible to implement the tier rules for the custom checks.
2. The country region and subregion logic always implies that a subregion exists within region, and that a subregion of a region cannot exist in another region outside of its own. This means that if two countries have the same subregion, that means there is no possibility that they are of `INTERNATIONAL` tier, because they are within the same region. 
