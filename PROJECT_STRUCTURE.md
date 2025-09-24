# Project Structure

```
websocketredis/
├── 📁 .mvn/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/
│   │   │       └── 📁 example/
│   │   │           └── 📁 websocketredis/
│   │   │               │
│   │   │               ├── ☕️ WebsocketRedisApplication.java (ไฟล์เริ่มต้นโปรเจ็กต์)
│   │   │               │
                        │   ├── config/
                        │   ├── RedisConfig.java
                        │   └── WebSocketConfig.java
                        ├── controller/
                        │   ├── FileUploadController.java
                        │   └── ProjectController.java
                        ├── dto/
                        │   ├── ProjectStatusMessage.java
                        │   ├── StartProjectRequest.java
                        │   └── TaskResponse.java
                        ├── service/
                        │   ├── FileUploadService.java
                        │   ├── ProjectCreationService.java
                        │   ├── RedisPublisherService.java
                        │   └── RedisMessageSubscriber.java
│   │   │
│   │   └── 📁 resources/
│   │       ├── 📁 static/      (เก็บไฟล์ CSS, JavaScript, รูปภาพ)
│   │       ├── 📁 templates/   (เก็บไฟล์ HTML Template ถ้ามี)
│   │       └── 📄 application.properties (ไฟล์ตั้งค่าหลักของโปรเจ็กต์)
│   │
│   └── 📁 test/
│       └── 📁 java/
│           └── 📁 com/
│               └── 📁 example/
│                   └── 📁 websocketredis/
│                       ├── 🧪 WebsocketRedisApplicationTests.java
│                       └── 🧪 controller/
│                           └── 📄 ProjectControllerTest.java
│
├── 📄 .gitignore           (ไฟล์บอก Git ว่าไม่ต้องสนใจไฟล์ไหนบ้าง)
├── 📄 mvnw                 (Maven Wrapper สำหรับ Linux/Mac)
├── 📄 mvnw.cmd             (Maven Wrapper สำหรับ Windows)
└── 📄 pom.xml               (ไฟล์หัวใจของ Maven: บอก Dependencies และการ Build)
```