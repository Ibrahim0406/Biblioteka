📚 Biblioteka - Sistem za Upravljanje Bibliotekom
Moderna desktop aplikacija za upravljanje bibliotekom razvijena u Javi sa JavaFX tehnologijom. Sistem omogućava kompletno upravljanje knjigama, korisnicima, pozajmicama, rezervacijama i recenzijama sa podrškom za različite korisničke uloge.
✨ Funkcionalnosti
👨‍💼 Admin Panel

Upravljanje knjigama

Dodavanje novih knjiga sa detaljnim informacijama (naslov, autor, ISBN, godina, cijena)
Uređivanje postojećih knjiga i upravljanje količinom dostupnih primjeraka
Brisanje knjiga iz sistema
Pregled svih knjiga sa filterima i pretragom
Export liste knjiga u PDF format
Automatsko ažuriranje statusa knjiga (dostupno/nedostupno) prema količini


Praćenje pozajmica

Pregled svih aktivnih i zatvorenih pozajmica
Informacije o korisnicima koji su pozajmili knjige
Datumi pozajmice i vraćanja
Automatsko upravljanje inventarom pri pozajmljivanju i vraćanju



👤 Korisnički Panel

Pregled dostupnih knjiga

Pretraživanje knjiga po naslovu, autoru ili ISBN-u
Pregled detalja o knjigama (cijena, godina izdanja, dostupnost)
Prikaz broja dostupnih primjeraka
Pregled prosječnih ocjena i recenzija za svaku knjigu
Zvjezdice prikazuju kvalitet knjige na osnovu korisničkih ocjena


Upravljanje pozajmicama

Pozajmljivanje dostupnih knjiga
Pregled svih trenutno pozajmljenih knjiga
Vraćanje knjiga jednim klikom
Automatsko smanjenje/povećanje dostupne količine


Sistem rezervacija

Rezervacija knjiga koje trenutno nisu dostupne
Pregled aktivnih rezervacija
Otkazivanje rezervacija
Automatsko obavještenje kada knjiga postane dostupna


Recenzije i ocjene

Ostavljanje recenzija za pročitane knjige (1-5 zvjezdica)
Pisanje detaljnih komentara o knjigama
Pregled prosječnih ocjena i broja recenzija
Mogućnost recenziranja samo vraćenih knjiga
Jedna recenzija po korisniku za svaku knjigu



🛠️ Tehnologije

Java 11 - Programski jezik
JavaFX 21.0.6 - GUI framework
MySQL 8.0 - Relaciona baza podataka
Maven - Build tool i dependency management
iText 7.2.5 - PDF export funkcionalnost
JDBC - Konekcija sa bazom podataka

📋 Preduvjeti
Prije pokretanja projekta, potrebno je instalirati:

Java Development Kit (JDK) 11 ili noviji

Preuzmi JDK

MySQL Server 8.0 ili noviji

Preuzmi MySQL

Maven 3.6 ili noviji (opcionalno ako koristite wrapper)

Preuzmi Maven


🚀 Instalacija i Pokretanje
1. Kloniranje Projekta
bashgit clone <repository-url>
cd biblioteka
2. Konfiguracija Baze Podataka
Kreirajte MySQL bazu podataka:
sqlCREATE DATABASE library_db;
Ažurirajte database credentials u src/main/java/com/biblioteka/database/DatabaseConnection.java:
javaprivate static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "root";
private static final String PASSWORD = "vasalozinka";
Napomena: Tabele se automatski kreiraju pri prvom pokretanju aplikacije.
3. Pokretanje Aplikacije
Koristeći Maven:
bashmvn clean install
mvn javafx:run
Koristeći Maven Wrapper (Linux/Mac):
bash./mvnw clean install
./mvnw javafx:run
Koristeći Maven Wrapper (Windows):
bashmvnw.cmd clean install
mvnw.cmd javafx:run
Iz IDE-a:
Pokrenite glavnu klasu: com.biblioteka.MainApp
🔑 Default Korisnici
Aplikacija dolazi sa predefinisanim korisnicima za testiranje:
Username admin
Password admin123
Uloga ADMIN
Opis Administrator sa punim pravima
korisnik
korisnik123
USER
Obični korisnik za testiranje
```

## 📁 Struktura Projekta
```
biblioteka/
├── src/main/java/com/biblioteka/
│   ├── controller/          # JavaFX kontroleri
│   │   ├── AdminMainController.java
│   │   ├── UserMainController.java
│   │   ├── LoginController.java
│   │   ├── BookFormController.java
│   │   └── ReviewFormController.java
│   ├── dao/                 # Data Access Objects
│   │   ├── BookDAO.java
│   │   ├── LoanDAO.java
│   │   ├── UserDAO.java
│   │   ├── ReviewDAO.java
│   │   └── ReservationDAO.java
│   ├── database/            # Konekcija sa bazom
│   │   └── DatabaseConnection.java
│   ├── model/               # Modeli podataka
│   │   ├── Book.java
│   │   ├── BookWithRating.java
│   │   ├── Loan.java
│   │   ├── User.java
│   │   ├── Review.java
│   │   └── Reservation.java
│   ├── util/                # Utility klase
│   │   └── PDFExporter.java
│   ├── Launcher.java        # Entry point
│   └── MainApp.java         # Glavna aplikacija
├── src/main/resources/com/biblioteka/view/
│   ├── login.fxml           # Login ekran
│   ├── admin_main.fxml      # Admin panel
│   ├── user_main.fxml       # Korisnički panel
│   ├── book_form.fxml       # Forma za knjige
│   ├── review_form.fxml     # Forma za recenzije
│   └── styles.css           # Stilovi
├── pom.xml                  # Maven konfiguracija
└── README.md               # Dokumentacija

🎯 Kako Koristiti
Login

Pokrenite aplikaciju
Unesite username i password
Kliknite na "Prijavi se"

Admin Funkcionalnosti

Dodavanje knjige:

Kliknite na "Dodaj knjigu"
Popunite sva polja (naslov, autor, ISBN, godina, cijena, količina)
Kliknite "Sačuvaj"


Uređivanje knjige:

Selektujte knjigu iz tabele
Kliknite "Uredi knjigu"
Izmjenite podatke i količinu
Kliknite "Sačuvaj"


Brisanje knjige:

Selektujte knjigu iz tabele
Kliknite "Obriši knjigu"
Potvrdite akciju


Export u PDF:

Kliknite "Export PDF"
Odaberite lokaciju za čuvanje fajla



Korisničke Funkcionalnosti

Pozajmljivanje knjige:

Pregledajte dostupne knjige u lijevoj tabeli
Selektujte knjigu sa količinom > 0
Kliknite "Pozajmi knjigu"
Količina se automatski smanjuje


Vraćanje knjige:

Pregledajte svoje pozajmljene knjige u desnoj tabeli (gornji dio)
Selektujte knjigu koju želite vratiti
Kliknite "Vrati knjigu"
Količina se automatski povećava


Rezervacija knjige:

Selektujte knjigu koja nije dostupna (količina = 0)
Kliknite "Rezerviši"
Pratite rezervacije u tabeli "Moje rezervacije"
Otkazivanje rezervacije je moguće klikom na "Otkaži"


Ostavljanje recenzije:

Selektujte knjigu koju ste već vratili
Kliknite "Ostavi Recenziju"
Odaberite ocjenu (1-5 zvjezdica)
Napišite komentar
Kliknite "Sačuvaj recenziju"
Možete ostaviti samo jednu recenziju po knjizi


Pregled ocjena:

Ocjene su vidljive u koloni "Ocjena" u tabeli knjiga
Format: ★★★★☆ (4.2) - zvjezdice i prosječna ocjena
Knjige bez ocjena prikazuju "Nema ocjena"



🔄 Automatsko Upravljanje Sistemom
Inventar

Kada korisnik pozajmi knjigu, količina dostupnih primjeraka se automatski smanjuje za 1
Kada korisnik vrati knjigu, količina dostupnih primjeraka se automatski povećava za 1
Status knjige se automatski ažurira:

DOSTUPNO - kada je količina > 0
NEDOSTUPNO - kada je količina = 0


Knjige sa količinom 0 ne mogu biti pozajmljene, ali mogu biti rezervisane

Rezervacije

Rezervacije su moguće samo za knjige koje trenutno nisu dostupne
Korisnici mogu pregledati sve svoje aktivne rezervacije
Rezervacije se mogu otkazati u bilo kom momentu
Status rezervacija: AKTIVNA ili OTKAZANA

Recenzije i Ocjene

Recenzije mogu ostavljati samo korisnici koji su vratili knjigu
Svaki korisnik može ostaviti maksimalno jednu recenziju po knjizi
Ocjene: 1-5 zvjezdica
Prosječna ocjena se automatski izračunava i prikazuje
Broj recenzija se prati za svaku knjigu

🐛 Rješavanje Problema
Greška: "Access denied for user"

Provjerite MySQL korisničko ime i lozinku u DatabaseConnection.java
Osigurajte da MySQL server radi

Greška: "Communications link failure"

Provjerite da li MySQL server radi na portu 3306
Provjerite firewall postavke

Greška: "Table doesn't exist"

Tabele se automatski kreiraju pri pokretanju
Provjerite da li imate ovlaštenja za kreiranje tabela u bazi

JavaFX greške

Provjerite da koristite JDK 11 ili noviji
Provjerite da Maven koristi ispravnu verziju Jave

Greška pri ostavljanju recenzije

Provjerite da ste vratili knjigu prije recenziranja
Provjerite da već niste ostavili recenziju za tu knjigu

📊 Statistika i Izvještaji

Admin: Export PDF sa kompletnom listom knjiga
Korisnici: Pregled svojih pozajmica, rezervacija i recenzija
Ocjene: Automatski izračunati prosjek ocjena za svaku knjigu

🔐 Sigurnost
NAPOMENA: Ovo je fakultetski projekat bez spring security zaštite:

Lozinke se čuvaju u plain text formatu
Nema enkripcije podataka
Nema validacije na backend-u
Nema zaštite od SQL injection napada

Za produkcijsku upotrebu bilo bi potrebno:

Implementirati Spring Security
Enkriptovati lozinke (BCrypt)
Dodati validaciju podataka
Koristiti prepared statements (već implementirano)
Dodati logging sistem
Implementirati backup sistem

👨‍💻 Autori
Bišić Ibrahim, Skopljak Ahmed, Baručija Adnan
