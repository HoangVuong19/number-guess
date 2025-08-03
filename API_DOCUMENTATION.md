# Number Guessing Game API Documentation

## Overview

This API provides endpoints for a number guessing game where users can guess numbers from 1-5 with a 5% win probability.

## Authentication

All endpoints require JWT authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Endpoints

### 1. POST /api/guess

Make a guess for a number between 1-5.

**Request Body:**

```json
{
  "guess": 3,
  "updatedAt": "2025-08-03T14:24:47.821"
}
```

**Response:**

```json
{
  "message": "Congratulations! You guessed correctly!",
  "isCorrect": true,
  "correctNumber": 3,
  "remainingTurns": 4,
  "currentScore": 1,
  "updatedAt": "2025-08-03T14:50:23.278"
}
```

**Game Logic:**

- Server generates a random number from 1-5
- 5% probability that the user's guess becomes the correct number
- Each guess deducts 1 turn from the user's remaining turns
- Correct guesses add 1 point to the user's score
- Users can only guess if they have turns remaining (turns > 0)

**Error Responses:**

- `400 Bad Request`: Invalid guess (must be between 1-5)
- `400 Bad Request`: No turns remaining
- `404 Not Found`: User not found
- `409 Data Conflict`: Conflict guess

### 2. GET /api/me

Get the current user's profile information.

**Response:**

```json
{
  "email": "exam@gmail.com",
  "score": 4,
  "turns": 3,
  "updatedAt": "2025-08-03T15:28:35.413"
}
```

### 3. GET /api/leaderboard

Pay top 10 people with most correct guesses.

**Response:**

```json
[
  {
    "username": "exam1@gmail.com",
    "score": 5
  },
  {
    "username": "exam2@gmail.com",
    "score": 4
  },
  {
    "username": "exam3@gmail.com",
    "score": 4
  }
]
```

### 4. POST /api/auth/register

Register a new user account.

**Request Body:**

```json
{
  "username": "newuser",
  "password": "password123"
}
```

**Response:**

```json
{
  "message": "Registration successful"
}
```

### 5. POST /api/auth/login

Login to get a JWT token.

**Request Body:**

```json
{
  "username": "username",
  "password": "password"
}
```

**Response:**

```json
{
  "message": "Login successful",
  "token": "jwt-token-here"
}
```