Mindset: สร้างและทวนสอบจากแกนกลาง (Inside-Out)

เราจะสร้างความมั่นใจในโค้ดของเราเป็นชั้นๆ จากแกนกลางของ Logic ออกไปสู่ส่วนที่ติดต่อกับภายนอก
วิธีนี้ทำให้เรามั่นใจว่าแต่ละส่วนทำงานถูกต้องก่อนที่จะนำไปประกอบกัน

- ชั้นในสุด: Core Logic (Service Layer)

    - คำถาม: "Logic หลักของฟีเจอร์นี้ทำงานถูกต้องหรือไม่?"

    - นี่คือหัวใจของระบบ ต้องทดสอบให้แม่นยำที่สุดในแบบ Unit Test โดยไม่ต้องพึ่งพาส่วนอื่นๆ

- ชั้นกลาง: API Access (Controller Layer)

    - คำถาม: "ประตูทางเข้าสู่ Logic ของเรา เปิด-ปิด และเรียกใช้บริการถูกต้องหรือไม่?"

    - ทดสอบเพื่อให้แน่ใจว่า HTTP Request สามารถเรียก Service ที่เราทดสอบไปแล้วได้อย่างถูกต้อง

- ชั้นนอกสุด: Integration (การทำงานร่วมกัน)

    - คำถาม: "เมื่อประกอบร่างทุกอย่างเข้าด้วยกัน ทั้งระบบทำงานได้จริงตามที่คาดหวังหรือไม่?"

    - ทดสอบภาพรวมทั้งหมดเพื่อจับข้อผิดพลาดที่เกิดจากการตั้งค่า (Configuration) หรือการสื่อสารระหว่างส่วนต่างๆ

ตัวอย่างการทดสอบตามลำดับ: Flow การสร้างโปรเจกต์

เราจะทดสอบฟีเจอร์ "Start New Project" ที่มีการส่งสถานะอัปเดตผ่าน Redis และ WebSocket

Layer 1: ทดสอบ Service Logic (ProjectCreationService)

เป้าหมาย: ยืนยันว่า startProjectCreation ส่งข้อความสถานะ ไปหา RedisPublisherService ได้ถูกต้องตามลำดับ

- ไฟล์: ProjectCreationServiceTest.java (Unit Test)

- Method ที่ทดสอบ: startProjectCreation()

- วิธีทดสอบ: Mock RedisPublisherService แล้ว verify ว่ามันถูกเรียกใช้ด้วยข้อความที่ถูกต้อง

```java

@Test
void startProjectCreation_shouldPublishStatusMessagesInOrder() {
    // Arrange
    StartProjectRequest request = new StartProjectRequest();
    request.setTaskId("task-123");
    String topic = "/topic/new-project-status/task-123";

    // Act
    projectCreationService.startProjectCreation(request);

    // Assert: ตรวจสอบว่า publisher ถูกเรียกด้วยข้อความที่ถูกต้องตามลำดับ
    InOrder inOrder = Mockito.inOrder(publisher);
    inOrder.verify(publisher).publishAsJson(eq(topic), argThat(msg -> msg.message().contains("Step 1/3")));
    inOrder.verify(publisher).publishAsJson(eq(topic), argThat(msg -> msg.message().contains("Step 2/3")));
    inOrder.verify(publisher).publishAsJson(eq(topic), argThat(msg -> msg.message().contains("Step 3/3")));
    inOrder.verify(publisher).publishAsJson(eq(topic), argThat(msg -> msg.status() == NewProjectStatus.COMPLETED));
}
```

Layer 2: ทดสอบ Controller (ProjectController)

เป้าหมาย: ยืนยันว่าเมื่อมีการยิง API มาที่ /start จะเรียกใช้ ProjectCreationService

- ไฟล์: ProjectControllerTest.java (Web Layer Test)

- Method ที่เชื่อมต่อ: startNewProject() -> projectCreationService.startProjectCreation()

- วิธีทดสอบ: Mock ProjectCreationService แล้วยิง Request จำลองไปที่ Endpoint

```java

@Test
void startNewProject_whenCalled_shouldInvokeCreationService() throws Exception {
    // Arrange
    String requestJson = "{\"taskId\": \"task-123\", \"customerName\": \"custA\", \"projectName\": \"projX\"}";

    // Act: ยิง request ไปที่ endpoint /api/new-project/start
    mockMvc.perform(post("/api/new-project/start")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
            .andExpect(status().isOk());

    // Assert: ตรวจสอบว่า projectCreationService.startProjectCreation() ถูกเรียก 1 ครั้ง
    verify(projectCreationService, times(1)).startProjectCreation(any(StartProjectRequest.class));
}
```

Layer 3: ทดสอบการทำงานร่วมกัน (Integration Test)

เป้าหมาย: ยืนยัน Flow ทั้งหมดตั้งแต่ ยิง API -> ส่งข้อความผ่าน Redis -> Client ได้รับข้อความผ่าน WebSocket

- ไฟล์: ProjectFlowIntegrationTest.java (Full Integration Test)

- Methods ที่เชื่อมต่อ: ทั้งระบบ

- วิธีทดสอบ: spin up แอปพลิเคชันเกือบทั้งหมด, ใช้ Testcontainer สำหรับ Redis, และสร้าง WebSocket client
  จำลองเพื่อรอรับข้อความ

```java

@Test
void whenStartProjectApiIsCalled_webSocketClientShouldReceiveStatusUpdates() {
    // Arrange: สร้าง WebSocket client จำลองขึ้นมาเพื่อดักฟัง message
    // ... setup WebSocketStompClient ...
    // ... client.subscribe("/topic/new-project-status/task-abc", handler) ...

    // Act: ยิง HTTP Request จริงๆ ไปที่แอปพลิเคชันของเรา
    restTemplate.postForEntity("/api/new-project/start", startRequest, String.class);

    // Assert: รอสักพัก แล้วตรวจสอบว่า WebSocket client ได้รับข้อความตามที่เราคาดหวัง
    // (เช่น เช็คว่าได้รับข้อความ "Step 1/3...", "Step 2/3...", และ "Project created successfully!")
    await().atMost(15, TimeUnit.SECONDS).until(() ->
            handler.getReceivedMessages().contains("Project created successfully!")
    );
}
```