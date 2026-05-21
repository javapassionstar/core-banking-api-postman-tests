# 🏦 Core Banking API - End-to-End Automated Test Suite & Java Mock Server

A portfolio project demonstrating advanced **Postman** API test automation techniques and the ability to build lightweight test environments (Mock Servers) using **Java**.

## 🚀 About the Project

This repository contains a comprehensive End-to-End (E2E) test suite simulating a retail banking customer onboarding process. To eliminate dependencies on external networks or unstable third-party environments, I developed a dedicated mock server in Java utilizing the built-in `HttpServer`.

### Technologies & Technical Standards:
* **Test Automation:** Postman (JavaScript, Collection Runner, Environments)
* **Backend Mock:** Java (SE 17), Maven, `com.sun.net.httpserver`
* **Dependencies:** `iban4j` (for generating and validating structurally correct IBAN numbers)
* **Architecture:** REST API (JSON, HTTP Status Codes: 200, 201, 401, 405), Clean Code practices, and the Single Responsibility Principle (SRP)

---

## 🗺️ Business Scenario (E2E Workflow)

The Postman collection automatically executes and verifies the following business flow:
1. `POST /api/v1/customers` (Registration) – Generates unique customer details. The server assigns a unique UUID.
2. `POST /api/v1/auth/login` (Login) – Authenticates using the newly created credentials and fetches a session token.
3. `POST /api/v1/accounts` (Open Account) – Assigns a new bank account (IBAN) to the authenticated customer (requires authorization).
4. `GET /api/v1/accounts/:accountNumber/balance` (Fetch Balance) – Retrieves the account balance using a dynamic Path Variable.
5. `POST /api/v1/accounts (Negative Check)` – A security test verifying that attempting to open an account without a token is blocked with a `401 Unauthorized` status.


---

## 🧠 Advanced Postman Mechanisms Applied

* **Dynamic Data Generation (Pre-request Scripts):** Implemented a custom JavaScript algorithm that calculates a mathematically valid Polish National Identification Number (PESEL) with a proper checksum based on a randomized gender, along with unique user profile data (cleaning Polish diacritics for email consistency).
* **Variable Extraction (Post-response Scripts):** Tests automatically parse JSON responses, extract key dynamic data (e.g., dynamic Bearer tokens, customer IDs, IBANs), and save them securely as `Environment Variables`.
* **Authorization Inheritance:** Instead of hardcoding the Bearer token into each individual request, it is configured once globally at the scenario folder level.
* **Bulletproof Scripting:** Test scripts are designed defensively so that potential server structure issues or raw failures do not crash the entire test runner execution.

---

## 🛠️ Java Server Architecture

The backend mock code follows strictly object-oriented design and clean coding principles:
* **`LocalMockServer`** – The entry point (Orchestrator) initializing the clean test environment.
* **`ServerBuilder`** – Manages the networking layer, routing, HTTP header validation, and HTTP method verification (`GET`/`POST`).
* **`BankDataGenerator`** – A decoupled utility class (featuring a private constructor and static methods) handling core business logic, such as UUID generation, `iban4j` library integration, and monetary balance rounding.

---

## 📈 How to Run and Test

1. Clone this repository and run the `LocalMockServer` class in your preferred IDE (the server will start on port `8080`).
2. Import the collection and environment `.json` files from the `/postman` directory into Postman.
3. Right-click the `Scenario: New Customer Onboarding` folder and select **Run folder**.
4. The entire scenario will execute automatically, and all test assertions will pass successfully.
