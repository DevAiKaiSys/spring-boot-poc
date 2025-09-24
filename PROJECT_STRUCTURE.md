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
│   │   │               ├── 📁 config/
│   │   │               │   └── 📄 WebSocketConfig.java (สำหรับตั้งค่า WebSocket)
│   │   │               │
│   │   │               ├── 📁 controller/
│   │   │               │   └── 📄 ProjectController.java (รับ request จากภายนอก)
│   │   │               │
│   │   │               ├── 📁 service/
│   │   │               │   └── 📄 ProjectService.java (จัดการ Logic หลัก)
│   │   │               │
│   │   │               ├── 📁 dto/ (Data Transfer Object)
│   │   │               │
│   │   │               └── 📁 repository/
│   │   │                   └── 📄 ProjectRepository.java (สำหรับจัดการข้อมูล)
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