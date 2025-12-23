# 📚 Biblioteka - Sistem za Upravljanje Bibliotekom

Moderna desktop aplikacija za upravljanje bibliotekom razvijena u Javi sa JavaFX tehnologijom. Sistem omogućava kompletno upravljanje knjigama, korisnicima i pozajmicama sa podrškom za različite korisničke uloge.

## ✨ Funkcionalnosti

### 👨‍💼 Admin Panel
- **Upravljanje knjigama**
  - Dodavanje novih knjiga sa detaljnim informacijama (naslov, autor, ISBN, godina, cijena)
  - Uređivanje postojećih knjiga i upravljanje količinom dostupnih primjeraka
  - Brisanje knjiga iz sistema
  - Pregled svih knjiga sa filterima i pretragom
  - Export liste knjiga u PDF format
  
- **Praćenje pozajmica**
  - Pregled svih aktivnih i zatvorenih pozajmica
  - Informacije o korisnicima koji su pozajmili knjige
  - Datumi pozajmice i vraćanja

### 👤 Korisnički Panel
- **Pregled dostupnih knjiga**
  - Pretraživanje knjiga po naslovu, autoru ili ISBN-u
  - Pregled detalja o knjigama (cijena, godina izdanja, dostupnost)
  - Prikaz broja dostupnih primjeraka

- **Upravljanje pozajmicama**
  - Pozajmljivanje dostupnih knjiga
  - Pregled svih trenutno pozajmljenih knjiga
  - Vraćanje knjiga jednim klikom

## 🛠️ Tehnologije

- **Java 11** - Programski jezik
- **JavaFX 21.0.6** - GUI framework
- **MySQL 8.0** - Relaciona baza podataka
- **Maven** - Build tool i dependency management
- **iText 7.2.5** - PDF export funkcionalnost
- **JDBC** - Konekcija sa bazom podataka

## 📋 Preduvjeti

Prije pokretanja projekta, potrebno je instalirati:

1. **Java Development Kit (JDK) 11 ili noviji**
   - [Preuzmi JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

2. **MySQL Server 8.0 ili noviji**
   - [Preuzmi MySQL](https://dev.mysql.com/downloads/mysql/)

3. **Maven 3.6 ili noviji** (opcionalno ako koristite wrapper)
   - [Preuzmi Maven](https://maven.apache.org/download.cgi)
     

## 🚀 Instalacija i Pokretanje

### 1. Kloniranje Projekta

```bash
git clone <repository-url>
cd biblioteka
```

### 2. Konfiguracija Baze Podataka

Kreirajte MySQL bazu podataka:

```sql
CREATE DATABASE library_db;
```

Ažurirajte database credentials u `src/main/java/com/biblioteka/database/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "root";
private static final String PASSWORD = "vasalozinka";
```

**Napomena:** Tabele se automatski kreiraju pri prvom pokretanju aplikacije.

### 3. Pokretanje Aplikacije

#### Koristeći Maven:

```bash
mvn clean install
mvn javafx:run
```

#### Koristeći Maven Wrapper (Linux/Mac):

```bash
./mvnw clean install
./mvnw javafx:run
```

#### Koristeći Maven Wrapper (Windows):

```bash
mvnw.cmd clean install
mvnw.cmd javafx:run
```

#### Iz IDE-a:

Pokrenite glavnu klasu: `com.biblioteka.MainApp`

## 🔑 Default Korisnici

Aplikacija dolazi sa predefinisanim korisnicima za testiranje:

| Username | Password | Uloga | Opis |
|----------|----------|-------|------|
| admin | admin123 | ADMIN | Administrator sa punim pravima |
| korisnik | korisnik123 | USER | Obični korisnik za testiranje |

## 🗄️ Struktura Baze Podataka

### Tabela: users
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);
```

### Tabela: books
```sql
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    year INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'DOSTUPNO',
    available_quantity INT DEFAULT 1
);
```

### Tabela: loans
```sql
CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    borrower VARCHAR(100) NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
```

## 📁 Struktura Projekta

```
biblioteka/
├── src/main/java/com/biblioteka/
│   ├── controller/          # JavaFX kontroleri
│   │   ├── AdminMainController.java
│   │   ├── UserMainController.java
│   │   ├── LoginController.java
│   │   └── BookFormController.java
│   ├── dao/                 # Data Access Objects
│   │   ├── BookDAO.java
│   │   ├── LoanDAO.java
│   │   └── UserDAO.java
│   ├── database/            # Konekcija sa bazom
│   │   └── DatabaseConnection.java
│   ├── model/               # Modeli podataka
│   │   ├── Book.java
│   │   ├── Loan.java
│   │   └── User.java
│   ├── util/                # Utility klase
│   │   └── PDFExporter.java
│   ├── Launcher.java        # Entry point
│   └── MainApp.java         # Glavna aplikacija
├── src/main/resources/com/biblioteka/view/
│   ├── login.fxml           # Login ekran
│   ├── admin_main.fxml      # Admin panel
│   ├── user_main.fxml       # Korisnički panel
│   └── book_form.fxml       # Forma za knjige
├── pom.xml                  # Maven konfiguracija
└── README.md               # Dokumentacija
```

## 🎯 Kako Koristiti

### Login

1. Pokrenite aplikaciju
2. Unesite username i password
3. Kliknite na "Prijavi se"

### Admin Funkcionalnosti

1. **Dodavanje knjige:**
   - Kliknite na "Dodaj knjigu"
   - Popunite sva polja (naslov, autor, ISBN, godina, cijena, količina)
   - Kliknite "Sačuvaj"

2. **Uređivanje knjige:**
   - Selektujte knjigu iz tabele
   - Kliknite "Uredi knjigu"
   - Izmjenite podatke i količinu
   - Kliknite "Sačuvaj"

3. **Brisanje knjige:**
   - Selektujte knjigu iz tabele
   - Kliknite "Obriši knjigu"
   - Potvrdite akciju

4. **Export u PDF:**
   - Kliknite "Export PDF"
   - Odaberite lokaciju za čuvanje fajla

### Korisničke Funkcionalnosti

1. **Pozajmljivanje knjige:**
   - Pregledajte dostupne knjige u lijevoj tabeli
   - Selektujte knjigu sa količinom > 0
   - Kliknite "Pozajmi knjigu"

2. **Vraćanje knjige:**
   - Pregledajte svoje pozajmljene knjige u desnoj tabeli
   - Selektujte knjigu koju želite vratiti
   - Kliknite "Vrati knjigu"

## 🔄 Automatska Upravljanje Količinom

- Kada korisnik pozajmi knjigu, količina dostupnih primjeraka se automatski smanjuje za 1
- Kada korisnik vrati knjigu, količina dostupnih primjeraka se automatski povećava za 1
- Knjige sa količinom 0 ne mogu biti pozajmljene

## 🐛 Rješavanje Problema

### Greška: "Access denied for user"
- Provjerite MySQL korisničko ime i lozinku u `DatabaseConnection.java`
- Osigurajte da MySQL server radi

### Greška: "Communications link failure"
- Provjerite da li MySQL server radi na portu 3306
- Provjerite firewall postavke

### JavaFX greške
- Provjerite da koristite JDK 11 ili noviji
- Provjerite da Maven koristi ispravnu verziju Jave


## 👨‍💻 Autori

Bišić Ibrahim, Skopljak Ahmed, Baručija Adnan

---
Napomena: Ovo je samo fakultetski projekat bez ikakve vrste zastite (spring security)
---


