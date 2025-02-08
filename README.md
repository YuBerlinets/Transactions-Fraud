# Transaction Fraud Detections
### Real-time analysis of banking transactions to detect fraudulent ones

## Overview
This project is a real-time fraud detection system for banking transactions. It utilizes Apache Kafka for event streaming, a machine learning model trained with DecisionTreeClassifier from sklearn, and a Spring Boot backend to process transactions. Fraudulent transactions are stored in MongoDB, while legitimate ones (in a real-world scenario) would be forwarded to an internal banking system.

## Architecture
![Architecture Scheme](https://github.com/user-attachments/assets/8a438fcb-6e7e-4ef4-b6e4-bdb537993a29)

### Transaction Producer (Python)
- Generates 100 transactions per minute for testing.
- Sends transactions to the Kafka topic transactions.
### Backend (Java Spring Boot)
- Consumes transactions from Kafka.
- Calls an ML-based fraud detection model (DecisionTreeClassifier).
- If fraudulent, stores the transaction in MongoDB and sends to "fraud-alert" Kafka topic
- If legitimate, in a real-world case, forwards the transaction to a banking system.
- Sends transaction updates via WebSocket to the frontend.
  
### Frontend (React + TypeScript)
- Real-time dashboard displaying: Transactions per 3 minutes, Blocked Transactions, Approved Transactions, Fraud Alerts
- HTTP server configured using Nginx

### Transactions Streaming (Kafka)
- Has two topics with events (fraud-alerts, transactions)

### ML Model (Python + FastAPI + SkLearn)
- Trained on datasets with transaction samples based on the Decision Tree Classifier
- Has endpoints to send and analyze transactions and evaluate a model

## Tech Stack
- **Backend**: Java, Spring Boot, Kafka Streams, MongoDB, FastAPI, WEBSocket
- **Machine Learning**: Python, Scikit-learn
- **Frontend**: React, TypeScript, Vite, SockJS, Nginx
- **Event Streaming**: Apache Kafka

## Client Layout
**Dashboard**
![Dashboard Light](https://github.com/user-attachments/assets/7a507fe6-de1f-4b07-8c00-9bda76e5a544)  
![Dashboard Dark](https://github.com/user-attachments/assets/a80b4f1d-9fee-46ba-8326-0d07debc796b)  


**Stored Fraudulent Transactions**
![DB Light](https://github.com/user-attachments/assets/d530af8b-1fd5-4c39-b0e3-2362523442de)
![DB Dark](https://github.com/user-attachments/assets/4b7d03e8-7cb0-4ba4-b428-c0ea107666d4)

## Config (.env files)

### **Client**  
VITE_BASE_URL=  
  
### **Java Backend**
MONGODB_URI=  
KAFKA_BOOTSTRAP_SERVERS=  
ML_MODEL_URL=  
KAFKA_STREAMS_APPLICATION_ID=  
BOOTSTRAP_SERVERS_CONFIG=  
  
### **Python Generator**  
BASE_URL=  

## How to run?
> [!WARNING]
Don't forget to change the .env files and properties in docker-compose.yml!  

To run using Docker compose run this command in project folder.  
```

docker-compose up --build

```

