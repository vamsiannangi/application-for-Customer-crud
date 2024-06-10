# Customer CRUD Application

This is a CRUD (Create, Read, Update, Delete) application for managing customer information. It includes backend APIs for handling customer data and a simple frontend for user interaction.

## Project Structure

The project is structured into three main components:

1. **Backend (Spring Boot):**
   - The backend is developed using the Spring Boot framework.
   - It provides RESTful APIs for performing CRUD operations on customer data.
   - JWT authentication is implemented for securing the APIs.
   - API endpoints:
     - Create a customer
     - Update a customer
     - Get a list of customers with pagination, sorting, and searching
     - Get a single customer based on ID
     - Delete a customer

2. **Frontend :**
   - The frontend is a basic UI for interacting with the customer data.
   - It includes screens for login, customer list, and adding a new customer.
   - The UI is minimalistic, focusing on functionality rather than aesthetics.

3. **Integration (Remote API Sync):**
   - In the second phase, a sync button is added to the customer list screen.
   - Clicking the sync button triggers a call to a remote API to fetch customer data.
   - The fetched data is then saved in the local database, updating existing customers.

## Authentication

Authentication is implemented using JWT (JSON Web Token). Users need to log in with valid credentials to access the application. JWT tokens are used for subsequent API calls.

## How to Run

1. Clone this repository:

   ```bash
   git clone https://github.com/vamsiannangi/application-for-Customer-crud.git
   cd customer-crud-app

## Usage

### Login:
1. Navigate to the login screen and enter credentials: 
   - Username: test@sunbasedata.com
   - Password: Test@123.
2. Successful login redirects to the customer list screen.

### Customer List:
- View a list of customers.
- Click the sync button to fetch and sync data from a remote API.

### Add a New Customer:
1. Navigate to the "Add a new customer" screen.
2. Fill in the customer details and submit the form.

## API Documentation

### Create Customer:
- **POST** `/api/customers`

### Update Customer:
- **PUT** `/api/customers/{id}`

### Get Customers:
- **GET** `/api/customers`
  - Query parameters: `page`, `size`, `search`, `sort`

### Get Single Customer:
- **GET** `/api/customers/{id}`

### Delete Customer:
- **DELETE** `/api/customers/{id}`

## Remote API Integration

### Authentication API:
- **POST** `https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp`

### Get Customer List API:
- **GET** `https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp`
  - Query parameter: `cmd=get_customer_list`

