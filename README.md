
# ECO-mmerce
## Cloud Computing Branch 
Made by Farrel & Aziz
This branch focuses on the Cloud Computing aspect of our application. We have developed backend endpoints for integration with mobile development and created the necessary cloud configurations for deployment on Google Cloud.
### 💻 Tech Stack
- Java (Node JS)
- Express JS
- Python (Flask API)
- Google App Engine
- Firestore
- Tensorflow
### ✨ Dependencies
- "node":"^20.13.1"
- "bcrypt": "^5.1.1"
- "@google-cloud/firestore": "^7.8.0"
- "@google-cloud/storage": "^7.11.2"
- "body-parser": "^1.20.2"
- "dotenv": "^16.4.5"
- "express": "^4.19.2"
- "firebase": "^10.12.2"
- "firebase-admin": "^12.1.1"
- "jsonwebtoken": "^9.0.2"
- "flask":"^3.0.3"
- "tensorflow":"^2.16.1"
- "pandas":"^2.2.2"
- "numpy":"^1.24.2"
### 🌐 Cloud Architecture Overview
![image](https://github.com/fabs90/ECO-mmerce/assets/87571919/e0dfa62b-c011-49d0-856e-cd957c41c531)


### ☁️ Cloud Services
- **Cloud Firestore**
Cloud Firestore is used to store the database of authentication details and product details. It is a flexible, scalable database for mobile, web, and server development from Firebase and Google Cloud Platform.
- **App Engine**
Google App Engine is a fully managed platform that allows the building and deploying of applications without worrying about managing the underlying infrastructure. Two services are deployed on App Engine for different purposes:
	1. Default Service:
	-   **Runtime**: Node.js
	- **Environment**: Standard
	- **Purpose**: This service handles the primary backend logic and API endpoints for the application. It is designed to support the integration with mobile development by providing the necessary backend functionality. 
	2. Python Service:
	-   **Runtime**: Python with Flask API
	- **Environment**: Flexible
	- **Purpose**: This service is dedicated to handling the machine learning part of the application. It provides endpoints for ML-based product recommendations.

### ⬆️ Endpoint Routes
![image](https://github.com/fabs90/ECO-mmerce/assets/87571919/fa6a22f2-0aaf-427b-9fe5-6aa62d9a502e)
### 🔗 Deployment Link
https://eco-mmerce-cloud.et.r.appspot.com 
