# 📒 Address Book Service

A **Spring Boot-based Address Book Service** that provides basic CRUD operations for managing address book entries.  
You can **add**, **update**, **delete**, and **query** address records using simple REST APIs.

---

## ✅ Features
- Add new address entries
- Update existing address details
- Delete address records
- Search/query address entries based on any keyword(partial or full text)

---

## 🛠️ Technologies/Dependencies Used
- **Java 17+**
- **Spring Boot**
- **Maven**
- **Lombok**
- **MapStruct**

---

## ⚙️ Requirements
- **Java 17 or higher**

---

## ▶️ Running the Application
### **Option 1:(If maven installed) Execute the maven command**
If you have maven installed already in your system then just run the following command in your bash in the same project folder

```bash
mvn spring-boot:run
```

### **Option 2: Execute the jar file**
If you don't have maven installed you can simply build the jar using the below command and then run the jar.
Make sure you are in your project folder

```bash
mvnw.cmd clean package(On windows or cmd)
./mvnw clean package (On linux/mac or gitbash)
java -jar target/address-book-0.0.1-SNAPSHOT.jar
```
Application will start on port 5000


