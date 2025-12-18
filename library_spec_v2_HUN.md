# Könyvtári Kölcsönző Rendszer - Műszaki Specifikáció

## Tartalomjegyzék
1. [Rendszer Áttekintés](#1-rendszer-áttekintés)
2. [Rendszerkövetelmények](#2-rendszerkövetelmények)
3. [Funkcionális Követelmények](#3-funkcionális-követelmények)
4. [Adatmodell](#4-adatmodell)
5. [Fájlformátumok és Adatmegőrzés](#5-fájlformátumok-és-adatmegőrzés)
6. [Kivételkezelés](#6-kivételkezelés)
7. [Felhasználói Felület](#7-felhasználói-felület)
8. [További Funkciók](#8-további-funkciók)
9. [Tesztelési Követelmények](#9-tesztelési-követelmények)

---

## 1. Rendszer Áttekintés

### 1.1 Célkitűzés
A rendszer egy egyszerű könyvtári nyilvántartó és kölcsönző rendszer, amely lehetővé teszi könyvek nyilvántartását, listázását és törlését a könyvtár tagjai számára (akiket szintén nyilván tart a rendszer). A rendszer kezeli a kölcsönzéseket, visszahozásokat és büntetések nyilvántartását, miközben statisztikákat biztosít a népszerű kategóriákról és gyakran késő tagokról.

### 1.2 Alapvető Funkciók
- Könyvleltár kezelés 5 kategóriában
- Tag regisztráció és kezelés
- Kölcsönzés és visszahozás feldolgozás
- Automatikus büntetésszámítás késedelmes könyvekhez
- Statisztikák és jelentések
- Terminál Felhasználói Felület (TUI) ANSI színkódokkal

---

## 2. Rendszerkövetelmények

### 2.1 Operációs Rendszer
- **Célplatform:** Windows 10 vagy Windows 11 (64-bit)
- **Garantált stabil működés** ezeken a platformokon

### 2.2 Fejlesztői Környezet
- **IDE:** JetBrains IntelliJ IDEA Community Edition 2025.1 vagy újabb
- **Programozási Nyelv:** Java
- **JDK Verzió:** JDK 21 vagy újabb
- **Kód Nyelve:** Minden kód, komment és UI szöveg angol nyelvű kell legyen

### 2.3 Hardverkövetelmények
- **RAM:** Minimum 2GB az IDE-hez, 8GB ajánlott a teljes rendszerhez
- **Tárhely:** 3,5GB szabad lemezterület (SSD ajánlott)
- **Kijelző:** Minimum felbontás 1024x768
- **Processzor:** 64-bit kompatibilis

---

## 3. Funkcionális Követelmények

### 3.1 Könyvek Listázása Kategória Szerint

**Leírás:** A felhasználók megtekinthetik a könyvek aktuális leltárát kategóriák szerint szűrve.

**Működés:**
1. A rendszer megjeleníti az elérhető kategóriák listáját (Sci-fi, Dráma, Történelem, Gyermekkönyv, Szakkönyv)
2. A felhasználó kiválasztja a kívánt kategóriát
3. A rendszer megjeleníti a könyveket az azonosítójuk számrészének növekvő sorrendjében
4. Megjelenítési formátum minden könyvhöz:
   ```
   [AZONOSÍTÓ] Cím by Szerző - STÁTUSZ
   ```
   - **Elérhető könyvek:** Alapértelmezett színben jelennek meg
   - **Kölcsönzött könyvek:** Sárgával, "[LOANED]" jelzéssel és várható visszahozási dátummal
   - Példa: `[SF-042] Dune by Frank Herbert - [LOANED until 2025-12-15]`

**Követelmények:**
- A könyveket az azonosító számrészének sorrendjében kell rendezni (SF-001, SF-002, SF-010, stb.)
- Egyértelmű vizuális különbségtétel elérhető és kölcsönzött könyvek között

---

### 3.2 Könyv Kölcsönzése

**Leírás:** A felhasználók kölcsönözhetnek egy könyvet az azonosító és tag-azonosító megadásával.

**Folyamat:**
1. Felhasználó megadja a könyv azonosítóját
2. Rendszer ellenőrzi a könyv azonosító formátumát és elérhetőségét
3. Ha a könyv elérhető:
   - Rendszer bekéri a tagsági azonosítót
   - **Létező tag:** Rendszer ellenőrzi a tagsági azonosítót
   - **Új tag:** 
     - Rendszer értesíti a felhasználót, hogy még nem tag
     - Bekéri a nevet
     - Generál egyedi tagsági azonosítót (tartomány: 1-999999, szekvenciális)
     - Elmenti az új tag adatait
     - Megerősítést jelenít meg:  "Membership created! Your ID: XXXXXX"
4. Rendszer ellenőrzi, hogy a tagnak kevesebb mint 3 aktív kölcsönzése van-e
5. Rendszer rögzíti a kölcsönzést az aktuális dátummal
6. Rendszer frissíti az összes releváns adatfájlt
7. Megerősítést jelenít meg a kölcsönzés részleteivel és a visszahozási határidővel

**Üzleti Szabályok:**
- Maximum 3 egyidejű kölcsönzés tagonként
- A könyvnek elérhetőnek kell lennie (jelenleg nem kölcsönzött)
- A tagsági azonosítónak érvényesnek kell lennie (létező vagy újonnan létrehozott)

**Hibakezelés:**
- Könyv azonosító nem létezik → Hibaüzenet, visszatérés a menübe
- Könyv már kölcsönözve → Várható visszahozási dátum megjelenítése, előjegyzés felajánlása (ha a funkció engedélyezett)
- Tagnak 3 aktív kölcsönzése van → Hibaüzenet, aktuális kölcsönzések listázása
- Érvénytelen névformátum → Hibaüzenet, újrapróbálkozás

---

### 3.3 Könyv Visszahozása

**Leírás:** A felhasználók visszahozhatják a kölcsönzött könyveket és megtekinthetik a kapcsolódó büntetéseket, ha vannak.

**Folyamat:**
1. Felhasználó megadja a tagsági azonosítót
2. Rendszer megjeleníti a tag aktív kölcsönzéseit kölcsönzési dátum szerint rendezve (legrégebbi először)
3. Megjelenítési formátum:
   ```
   [AZONOSÍTÓ] Cím - Loaned: ÉÉÉÉ-HH-NN - Due: ÉÉÉÉ-HH-NN [OVERDUE: +X days, 50 Ft/day = Y HUF]
   ```
   - **Késedelmes könyvek:** Pirossal jelennek meg a büntetésszámítással
   - **Időben lévő könyvek:** Zölddel jelennek meg
4. Felhasználó kiválasztja a visszahozandó könyvet (könyv azonosító megadásával)
5. Ha késedelmes:
   - Rendszer kiszámítja a teljes büntetést (késedelmes napok × 50 Ft)
   - Megjeleníti a büntetés összegét
   - Rögzíti a büntetést a fines.txt-ben
6. Rendszer frissíti a kölcsönzési státuszt
7. Rendszer frissíti az összes releváns adatfájlt
8. Megerősítés megjelenítése

**Büntetésszámítás:**
```
(aktuális dátum - határidő) × 50 Ft/nap
```

**Követelmények:**
- Valós idejű büntetésszámítás az aktuális rendszerdátum alapján
- A büntetések rögzítésre kerülnek még akkor is, ha nem azonnal fizetik ki
- Egyértelmű vizuális jelzés a késedelmes státuszra

---

### 3.4 Könyvek Keresése

**Leírás:** A felhasználók több szempont alapján kereshetnek könyveket.

**Keresési Opciók:**
1. **Könyv Azonosító Szerint:** Pontos egyezés (pl. "SF-042")
2. **Cím Szerint:** Részleges egyezés, kis/nagybetű nem számít
3. **Szerző Szerint:** Részleges egyezés, kis/nagybetű nem számít

**Megjelenítés:** A keresési eredményeket ugyanolyan formátumban jelenítse meg, mint a könyvlistázást, elérhetőségi státusszal együtt.

---

### 3.5 Összes Büntetés Listázása

**Leírás:** Az összes aktív büntetés megjelenítése az összes tag között.

**Megjelenítési Formátum:**
```
main.model.Member ID | main.model.Member Name | main.model.Book ID | Days Overdue | Fine Amount
----------|-------------|---------|--------------|------------
000123    | John Doe    | SF-005  | 5            | 250 HUF
000456    | Jane Smith  | DR-012  | 12           | 600 HUF
                                   TOTAL:        | 850 HUF
```

**Követelmények:**
- Csak a kifizetetlen büntetések megjelenítése
- Teljes kintlévő büntetések összegének kiszámítása
- Büntetés összeg szerint rendezés (csökkenő)

---

### 3.6 Statisztikák

**Leírás:** Könyvtári használati statisztikák megjelenítése.

**Megjelenítendő Statisztikák:**

1. **Legnépszerűbb Kategória**
   - Mérőszám: Összes kölcsönzések száma (minden idők) kategóriánként
   - Megjelenítés: Kategória név és kölcsönzések száma
   - Példa:  "Most Popular Category: Sci-fi (142 loans)"

2. **Leggyakrabban Késő Tag**
   - Mérőszám: Késedelmes visszahozások száma tagonként
   - Megjelenítés: Tagsági azonosító, név és késések száma
   - Példa: "Most Frequently Late: John Doe (ID: 000123) - 8 late returns"

**További Statisztikák (Opcionális Megjelenítés):**
- Összes könyv a leltárban
- Összes aktív kölcsönzés
- Átlagos kölcsönzési időtartam kategóriánként

---

### 3.7 Bemenet Ellenőrzés

**Könyv Cím Érvényesítése:**
- Engedélyezett karakterek: Magyar ABC betűi (A-Z, Á, É, Í, Ó, Ö, Ő, Ú, Ü, Ű), szóközök, számjegyek, kötőjel (-), vessző (,), pont (.)
- Hossz: 1-200 karakter
- Érvényes címek példái: "1984", "Harry Potter és a bölcsek köve", "X-Men - Kezdetek"

**Szerző Név Érvényesítése:**
- Engedélyezett karakterek: Magyar ABC betűi, szóközök, kötőjel (-), aposztróf (')
- Hossz: 2-100 karakter
- Legalább egy betűt kell tartalmaznia
- Érvényes nevek példái: "Isaac Asimov", "Móra Ferenc", "Gárdonyi Géza"

**Tag Név Érvényesítése:**
- Ugyanazok a szabályok, mint a Szerző Névnél
- Minimum 2 karakter, maximum 100 karakter

**Könyv Azonosító Formátum:**
- Minta: `[KATEGÓRIA_KÓD]-[SZÁM]`
- Kategória kódok: SF, DR, HS, CH, TC
- Szám: 001-999 (nullával kitöltve 3 számjegyig)
- Példa: "SF-042", "DR-001"

**Tagsági Azonosító Formátum:**
- Tartomány: 1-999999
- A rendszer által szekvenciálisan generálva
- Formátum: Nullával kitöltve 6 számjegyig (pl. "000001", "012345")

---

## 4. Adatmodell

### 4.1 Könyv Kategóriák

| Kategória | Max. Kölcsönzési Idő | Azonosító Előtag | Leírás |
|----------|-------------------|-----------|-------------|
| **Sci-fi** | 14 nap | SF | Tudományos-fantasztikus könyvek |
| **Dráma** | 28 nap | DR | Dráma és színházi művek |
| **Történelem** | 21 nap | HS | Történelmi könyvek és életrajzok |
| **Gyermekkönyv** | 14 nap | CH | Gyermekirodalmi művek |
| **Szakkönyv** | 7 nap | TC | Szakmai és oktatási könyvek |

---

### 4.2 Osztály Struktúra

#### 4.2.1 Absztrakt main.model.Book Osztály

```java
public abstract class main.model.Book {
    private final String id;           // Egyedi azonosító (pl. "SF-042")
    private final String title;        // Könyv címe
    private final String author;       // Szerző neve
    private String loanedTo;           // Tagsági azonosító (null ha elérhető)
    
    // Konstruktor
    public main.model.Book(String id, String title, String author) { ... }
    
    // Csak getterek a megváltoztathatatlan mezőkhöz
    public String getId() { ... }
    public String getTitle() { ... }
    public String getAuthor() { ... }
    
    // Getter és setter a loanedTo-hoz
    public String getLoanedTo() { ... }
    public void setLoanedTo(String memberId) { ... }
    
    // Absztrakt metódusok az alosztályoknak
    public abstract int getLoanDuration();
    public abstract String getCategory();
}
```

#### 4.2.2 main.model.Book Alosztályok

Minden kategóriának saját osztálya van, amely a main.model.Book osztályt örökli:

**main.model.SciFi.java**

```java
import main.model.Book;

public class SciFi extends Book {
   private static final int LOAN_DURATION = 14;  // napok
   private static int count = 0;                  // Példányszámláló

   public main.model.SciFi(
   String id, String
   title,
   String author)

   {
      super(id, title, author);
      count++;
   }

   @Override
   public int getLoanDuration() {
      return LOAN_DURATION;
   }

   @Override
   public String getCategory() {
      return "Sci-fi";
   }

   public static int getCount() {
      return count;
   }
}
```

**Hasonló struktúra a többi osztályhoz:**
- `main.model.Drama.java` (LOAN_DURATION = 28)
- `main.model.History.java` (LOAN_DURATION = 21)
- `main.model.Children.java` (LOAN_DURATION = 14)
- `main.model.Technical.java` (LOAN_DURATION = 7)

---

#### 4.2.3 main.model.Member Osztály

```java
public class main.model.Member {
    private final String memberId;     // Egyedi azonosító (000001-999999)
    private final String name;         // Tag neve
    private int loanedBooks;           // Aktív kölcsönzések száma (max 3)
    
    // Konstruktor
    public main.model.Member(String memberId, String name) { ... }
    
    // Getterek
    public String getMemberId() { ... }
    public String getName() { ... }
    public int getLoanedBooks() { ... }
    
    // Setterek
    public void setLoanedBooks(int count) { ... }
    
    // Segédmetódusok
    public boolean canLoanMore() { return loanedBooks < 3; }
    public void incrementLoans() { loanedBooks++; }
    public void decrementLoans() { loanedBooks--; }
}
```

---

#### 4.2.4 main.model.Loan Osztály

```java
public class main.model.Loan {
    private final String bookId;
    private final String memberId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    
    public main.model.Loan(String bookId, String memberId, LocalDate loanDate, int loanDuration) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(loanDuration);
    }
    
    // Késedelmes napok számítása (0 ha nem késedelmes)
    public int getDaysOverdue() {
        LocalDate today = LocalDate.now();
        return today.isAfter(dueDate) ? 
               (int) ChronoUnit.DAYS.between(dueDate, today) : 0;
    }
    
    // Büntetés összegének számítása
    public int calculateFine() {
        return getDaysOverdue() * 50;  // 50 Ft/nap
    }
    
    public boolean isOverdue() {
        return getDaysOverdue() > 0;
    }
    
    // Getterek
    public String getBookId() { ... }
    public String getMemberId() { ... }
    public LocalDate getLoanDate() { ... }
    public LocalDate getDueDate() { ... }
}
```

---

#### 4.2.5 Fine Osztály

```java
public class Fine {
    private final String memberId;
    private final String bookId;
    private final LocalDate overdueDate;
    private int amount;
    
    public Fine(String memberId, String bookId, LocalDate overdueDate, int amount) { ... }
    
    // Getterek és setterek
    public String getMemberId() { ... }
    public String getBookId() { ... }
    public int getAmount() { ... }
    public void setAmount(int amount) { ... }
}
```

---

## 5. Fájlformátumok és Adatmegőrzés

### 5.1 Fájlok Tárolási Helye
Az összes adatfájlt a projekt `data/` könyvtárában kell tárolni:
```
projekt-gyökér/
├── src/
├── data/
│   ├── loanableBooks.txt
│   ├── members.txt
│   ├── loanedBooks.txt
│   └── fines.txt
└── ...
```

---

### 5.2 Fájlformátumok

#### 5.2.1 loanableBooks.txt
**Formátum:** `bookId;title;author`

**Példa:**
```
SF-001;Dune;Frank Herbert
SF-042;Foundation;Isaac Asimov
DR-005;Hamlet;William Shakespeare
HS-012;Sapiens;Yuval Noah Harari
CH-008;Harry Potter and the Philosopher's Stone;J.K. Rowling
TC-003;Clean Code;Robert C. Martin
```

**Szabályok:**
- Könyv azonosító számrésze nullával kitöltve 3 számjegyig
- Egy könyv soronként
- Pontosvessző (;) elválasztó
- Nincs lezáró pontosvessző
- UTF-8 kódolás a magyar karakterek támogatásához

---

#### 5.2.2 members.txt
**Formátum:** `memberId;name`

**Példa:**
```
000001;Kovács János
000042;Nagy Anna
000105;Szabó Péter
```

**Szabályok:**
- Tagsági azonosító nullával kitöltve 6 számjegyig
- Egy tag soronként
- Pontosvessző (;) elválasztó
- Nincs lezáró pontosvessző
- UTF-8 kódolás

---

#### 5.2.3 loanedBooks.txt
**Formátum:** `bookId;memberId;loanDate`

**Példa:**
```
SF-001;000042;2025-11-20
DR-005;000001;2025-11-28
HS-012;000042;2025-11-15
```

**Szabályok:**
- Dátum formátum: ÉÉÉÉ-HH-NN (ISO 8601)
- Tagsági azonosító nullával kitöltve 6 számjegyig
- Egy kölcsönzés soronként
- Pontosvessző (;) elválasztó
- Nincs lezáró pontosvessző
- Csak aktív kölcsönzések (visszahozott könyvek törlődnek)

---

#### 5.2.4 fines.txt
**Formátum:** `memberId;bookId;overdueDate;amount`

**Példa:**
```
000001;SF-042;2025-11-10;350
000042;DR-008;2025-11-25;150
```

**Szabályok:**
- Összeg forintban (egész szám)
- Késedelmes dátum az a nap, amikor a könyv késedelmessé vált (határidő + 1 nap)
- Büntetések megmaradnak kifizetésig/törölésig
- Könyv azonosító számrésze nullával kitöltve 3 számjegyig
- Egy büntetés soronként könyvenként
- Pontosvessző (;) elválasztó
- Nincs lezáró pontosvessző

---

### 5.3 Fájl Frissítési Stratégia

**Frissítési Időzítés:**
- A fájlok **azonnal** frissülnek minden művelet után (kölcsönzés, visszahozás, tag létrehozás)
- Atomikus írási műveletek használata (írás ideiglenes fájlba, majd átnevezés) az adatsérülés megelőzéséhez

**Frissítési Műveletek:**

| Művelet | Frissített Fájlok |
|--------|---------------|
| Könyv kölcsönzése | `loanedBooks.txt` (bejegyzés hozzáadása), `members.txt` (kölcsönzésszám frissítése ha új tag) |
| Könyv visszahozása | `loanedBooks.txt` (bejegyzés törlése), `fines.txt` (hozzáadás ha késedelmes) |
| Tag létrehozása | `members.txt` (bejegyzés hozzáadása) |
| Büntetés törlése | `fines.txt` (bejegyzés törlése) |

**Hibakezelés:**
- Ha a fájlírás sikertelen, a memóriabeli változások visszagörgetése
- Hibarészletek naplózása hibakereséshez
- Felhasználóbarát hibaüzenet megjelenítése

---

## 6. Kivételkezelés

### 6.1 Egyedi Kivétel Osztályok

#### 6.1.1 main.exception.InvalidBookIdException
**Mikor dobódik:** 
- Könyv azonosító formátuma helytelen (nem felel meg a [KATEGÓRIA]-[SZÁM] mintának)
- Könyv azonosító nem létezik a rendszerben

**Példa üzenetek:**
```
"Invalid book ID: 'XY-123'. Expected format: [SF|DR|HS|CH|TC]-[001-999]"
"main.model.Book ID 'SF-999' does not exist in the library."
```

---

#### 6.1.2 main.exception.InvalidMemberException
**Mikor dobódik:**
- Tagsági azonosító formátuma helytelen
- Tagsági azonosító nem létezik a rendszerben
- Tag elérte a maximum kölcsönzési limitet (3 könyv)

**Példa üzenetek:**
```
"Invalid member ID: 'ABC123'. Expected numeric ID: 000001-999999"
"main.model.Member ID '000999' not found."
"main.model.Member '000042' has reached the maximum loan limit (3 books)."
```

---

#### 6.1.3 InvalidInputException
**Mikor dobódik:**
- Felhasználói bemenet nem felel meg az érvényesítésnek (név, cím, szerző)
- Érvénytelen menüválasztás
- Érvénytelen dátum formátum

**Példa üzenetek:**
```
"Invalid name: 'John123'. Names can only contain letters, spaces, and hyphens."
"Invalid menu choice: '9'. Please select 1-6."
```

---

#### 6.1.4 FileOperationException
**Mikor dobódik:**
- Fájl nem olvasható vagy írható
- Fájl formátum sérült
- Hiányzó szükséges adatfájlok

**Példa üzenetek:**
```
"Error reading 'loanableBooks.txt': File not found."
"Data corruption detected in 'members.txt' at line 42."
"Failed to save loan data. Changes have been rolled back."
```

---

### 6.2 Kivételkezelési Stratégia

**Felhasználói Felület Szintjén:**
1. Minden kivételt elkapni a TUI határon
2. Felhasználóbarát hibaüzenetek megjelenítése
3. Helyreállítási lehetőségek felajánlása:
   - "Press Enter to return to menu"
   - "Press R to retry"
4. Az alkalmazás soha ne omoljon össze

**Példa:**

```java
import main.exception.InvalidBookIdException;
import main.exception.InvalidMemberException;try{
loanBook(bookId, memberId);
}catch(
InvalidBookIdException e){

displayError(e.getMessage());

promptReturnToMenu();
}catch(
InvalidMemberException e){

displayError(e.getMessage());

offerMemberCreation();
}catch(
FileOperationException e){

displayError("System error: "+e.getMessage());

logError(e);

promptReturnToMenu();
}
```

---

## 7. Felhasználói Felület

### 7.1 TUI Tervezési Alapelvek

**Technológia:**
- ANSI escape kódok színekhez és formázáshoz
- Standard bemenet/kimenet (System.in, System.out)
- Képernyőtörlés funkció a nézetek között

**Színséma:**
```
┌────────────────────────────────────────┐
│ Elem                │ Színkód          │
├─────────────────────┼──────────────────┤
│ Cím/Fejléc          │ Cián (Félkövér)  │
│ Menü Opciók         │ Fehér            │
│ Kiválasztott Opció  │ Zöld (Félkövér)  │
│ Elérhető Könyvek    │ Zöld             │
│ Kölcsönzött Könyvek │ Sárga            │
│ Késedelmes Tételek  │ Piros (Félkövér) │
│ Hibaüzenetek        │ Piros            │
│ Siker Üzenetek      │ Zöld             │
│ Bemenet Prompt      │ Fehér            │
└────────────────────────────────────────┘
```

---

### 7.2 Menü Struktúra

```
╔══════════════════════════════════════╗
║   LIBRARY MANAGEMENT SYSTEM          ║
╚══════════════════════════════════════╝

1. List Books by Category
2. main.model.Loan a main.model.Book
3. Return a main.model.Book
4. Search Books
5. List All Fines
6. View Statistics
7. Exit

Select an option (1-7): _
```

**Navigáció:**
- Számgombok (1-7) menüopciók kiválasztásához
- Enter vagy y/n kérés a megerősítéshez
- ESC vagy Enter az előző menübe való visszatéréshez (almenükben)
- Nyíl gombok a lista nézetekben történő kiválasztáshoz (opcionális fejlesztés)

---

### 7.3 Minta Képernyő Folyamatok

#### 7.3.1 Könyvek Listázása Kategóriánként

```
╔══════════════════════════════════════╗
║   SELECT CATEGORY                    ║
╚══════════════════════════════════════╝

1. Sci-fi (14 books)
2. Drama (8 books)
3. History (12 books)
4. Children (10 books)
5. Technical (6 books)
6. Back to main menu

Select category (1-6): 1

╔══════════════════════════════════════╗
║   SCI-FI BOOKS                       ║
╚══════════════════════════════════════╝

[SF-001] Dune by Frank Herbert - [AVAILABLE]

[SF-042] Foundation by Isaac Asimov - [LOANED until 2025-12-15]

[SF-105] The Martian by Andy Weir - [AVAILABLE]

Press Enter to return to menu...
```

---

#### 7.3.2 Könyv Kölcsönzése

```
╔══════════════════════════════════════╗
║   LOAN A BOOK                        ║
╚══════════════════════════════════════╝

Enter Book ID: SF-042
Enter Member ID: 000123

✓ Book: Foundation by Isaac Asimov
✓ Member: John Doe (ID: 000123)
✓ Current loans: 1/3

Loan Date: 2025-12-01
Due Date: 2025-12-15 (14 days)

Confirm loan? (Y/N): Y

Book successfully loaned!

Press Enter to return to menu...
```

---

#### 7.3.3 Könyv Visszahozása (Büntetéssel)

```
╔══════════════════════════════════════╗
║   RETURN A BOOK                      ║
╚══════════════════════════════════════╝

Enter main.model.Member ID: 000123

Active loans for John Doe:

[SF-005] Neuromancer
         Loaned: 2025-10-15 | Due: 2025-10-29
         ⚠ OVERDUE: 33 days | Fine: 1,650 HUF

[DR-012] Romeo and Juliet
         Loaned: 2025-11-20 | Due: 2025-12-18
         ✓ On time (17 days remaining)

Enter main.model.Book ID to return: SF-005

⚠ FINE NOTICE
main.model.Book is 33 days overdue.
Total fine: 1,650 HUF (33 days × 50 HUF/day)

Confirm return? (y/n): y

✓ main.model.Book returned successfully.
✓ Fine recorded: 1,650 HUF

Press Enter to return to menu...
```

---

#### 7.3.4 Statisztikák

```
╔══════════════════════════════════════╗
║   LIBRARY STATISTICS                 ║
╚══════════════════════════════════════╝

📊 Most Popular Category:
   Sci-fi (142 total loans)

⏰ Most Frequently Late main.model.Member:
   Kovács János (ID: 000123)
   Late returns: 8

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Additional information:
• Total books in inventory: 50
• Currently loaned books: 23
• Total unpaid fines: 4 350 Ft

Press Enter to return to menu...
```

---

#### 7.3.5 Büntetések Listázása

```
╔══════════════════════════════════════╗
║   UNPAID FINES                       ║
╚══════════════════════════════════════╝

main.model.Member ID   | main.model.Member Name   | main.model.Book ID   | Late    | Fine
------------|---------------|-----------|---------|----------
000123      | Kovács János  | SF-005    | 33 days | 1 650 Ft
000456      | Nagy Anna     | DR-012    | 12 days |   600 Ft
000089      | Szabó Péter   | TC-003    | 21 days | 1 050 Ft
000201      | Kiss Éva      | HS-008    | 5 days  |   250 Ft
                                        TOTAL:    | 3 550 Ft

Press Enter to return to menu...
```

---

## 8. További Funkciók

### 8.1 Funkció Hely #1
**[Fenntartva további funkcióhoz - később kerül meghatározásra]**

Lehetséges funkciók:
- Könyv előjegyzés (várólistázás)
- Email értesítések a határidőkről
- Napi könyvajánló
- Integrált mini-játékok (pl. Wordle, Akasztófa)

---

### 8.2 Funkció Hely #2
**[Fenntartva további funkcióhoz - később kerül meghatározásra]**

Lehetséges funkciók:
- Külső API integráció (NASA, NYT, stb.)
- Kölcsönzési előzmények exportálása
- Tag aktivitási jelentések
- Könyv értékelési rendszer

---

## 9. Tesztelési Követelmények

### 9.1 Egység Tesztelés (Unit Testing)

**Tesztelendő Osztályok:**
- `main.model.Book` és minden alosztály (main.model.SciFi, main.model.Drama, stb.)
- `main.model.Member`
- `main.model.Loan` (különösen a büntetésszámítás)
- `Fine`
- Bemenet érvényesítő metódusok

**Teszt Esetek (Minimum):**

**main.model.Book Osztály:**
- Megváltoztathatatlan mezők nem változtathatók
- Kölcsönzési időtartam helyes minden kategóriára
- Példányszámlálók helyesen inkrementálódnak

**main.model.Member Osztály:**
- Kölcsönzési limit betartása (max 3)
- Kölcsönzésszám helyes inkrementálása/dekrementálása

**main.model.Loan Osztály:**
- Helyes határidő számítás minden kategóriára
- Helyes büntetésszámítás különböző késedelmes időtartamokra
- Nem késedelmes esetek kezelése (büntetés = 0)

**Bemenet Érvényesítés:**
- Érvényes könyvcímek, szerzőnevek, tagnevek elfogadása
- Érvénytelen karakterek elutasítása
- Tartományon kívüli hosszak elutasítása

---

### 9.2 Integrációs Tesztelés

**Fájlműveletek:**
- Összes adatfájl sikeres betöltése indításkor
- Hiányzó fájlok kecses kezelése (üres fájlok létrehozása)
- Sérült adatok kezelése (érvénytelen sorok átugrása, hibák naplózása)
- Változtatások sikeres mentése minden művelet után

**Kölcsönzési Munkafolyamat:**
- Teljes kölcsönzési folyamat (könyv kiválasztása → tag ellenőrzése → kölcsönzés rögzítése)
- Kölcsönzés megakadályozása, ha a tagnak 3 aktív kölcsönzése van
- Már kölcsönzött könyv kölcsönzésének megakadályozása

**Visszahozási Munkafolyamat:**
- Időben visszahozott könyv (nincs büntetés)
- Késedelmes könyv visszahozása (büntetés számítás és rögzítés)
- Tag kölcsönzésszámának helyes frissítése

---

### 9.3 Minta Teszt Adatok

**Hozd létre ezeket a teszt fájlokat a `data/` könyvtárban:**

**loanableBooks.txt:**
```
SF-001;Dűne;Frank Herbert
SF-002;Alapítvány;Isaac Asimov
DR-001;Hamlet;William Shakespeare
HS-001;Sapiens;Yuval Noah Harari
CH-001;Harry Potter;J.K. Rowling
TC-001;Clean Code;Robert C. Martin
```

**members.txt:**
```
000001;Teszt Felhasználó Egy
000002;Teszt Felhasználó Kettő
000003;Teszt Felhasználó Három
```

**loanedBooks.txt:**
```
SF-001;000001;2025-11-20
DR-001;000001;2025-11-15
```

**fines.txt:**
```
000002;HS-001;2025-10-15;1250
```

---

### 9.4 Szélsőséges Esetek Tesztelése

1. **Tag pontosan 3 kölcsönzéssel próbál egy 4.-et kölcsönözni**
2. **Könyv visszahozása pontosan a határidőn (0 büntetés legyen)**
3. **Könyv visszahozása 1 nappal a határidő után (50 Ft büntetés legyen)**
4. **Új tag létrehozása duplikált névvel (engedélyezett legyen, különböző azonosítókkal)**
5. **Könyv azonosító vezető nullákkal (SF-001 vs SF-1)**
6. **Tag név magyar karakterekkel (Á, É, Í, stb.)**
7. **Nagyon hosszú cím (200 karakter a limiten)**
8. **Fájl UTF-8 BOM-ot tartalmaz (kecsesen kell kezelni)**
9. **Dátumszámítások hónap/év határon keresztül**
10. **Szekvenciális tagsági azonosító generálás a maximum közelében (999999)**

---

## 10. Implementációs Jegyzetek

### 10.1 Ajánlott Könyvtárak
- **Dátum/Idő:** `java.time.LocalDate` (beépített, JDK 21)
- **Fájl I/O:** `java.nio.file.Files` (beépített)
- **Kollekciók:** `java.util.ArrayList`, `java.util.HashMap` (beépített)
- **ANSI Színek:** Egyedi segédosztály implementálása vagy külső könyvtár használata (pl. Jansi)

### 10.2 Kód Organizáció
```
src/
├── main/
│   ├── main.Main.java                  // Belépési pont
│   ├── model/
│   │   ├── main.model.Book.java              // Absztrakt osztály
│   │   ├── main.model.SciFi.java
│   │   ├── main.model.Drama.java
│   │   ├── main.model.History.java
│   │   ├── main.model.Children.java
│   │   ├── main.model.Technical.java
│   │   ├── main.model.Member.java
│   │   ├── main.model.Loan.java
│   │   └── Fine.java
│   ├── service/
│   │   ├── BookService.java       // Könyv műveletek
│   │   ├── MemberService.java     // Tag műveletek
│   │   ├── LoanService.java       // Kölcsönzési műveletek
│   │   └── FileService.java       // Fájl I/O műveletek
│   ├── ui/
│   │   ├── MenuUI.java            // Főmenü
│   │   ├── ConsoleUI.java         // Konzol segédprogramok
│   │   └── AnsiColors.java        // ANSI színkódok
│   ├── exception/
│   │   ├── main.exception.InvalidBookIdException.java
│   │   ├── main.exception.InvalidMemberException.java
│   │   ├── InvalidInputException.java
│   │   └── FileOperationException.java
│   └── util/
│       ├── InputValidator.java    // Bemenet érvényesítés
│       └── DateUtils.java         // Dátum segédprogramok
└── test/
    └── [Egység tesztek tükrözik a src struktúrát]
```

### 10.3 Legjobb Gyakorlatok
- Használj beszédes változó- és metódusneveket
- Adj hozzá JavaDoc kommenteket minden nyilvános metódushoz
- Kövesd a Java elnevezési konvenciókat (camelCase változókhoz, PascalCase osztályokhoz)
- Tartsd a metódusokat fókuszáltnak és tömörnek (Egyetlen Felelősség Elve)
- Használj konstansokat a "mágikus számokhoz" (pl. `MAX_LOANS = 3`, `FINE_PER_DAY = 50`)
- Kezelj kivételeket megfelelő szinteken
- Naplózd a fontos műveleteket (kölcsönzés, visszahozás, tag létrehozása)

---

## 11. Szójegyzék

| Kifejezés | Meghatározás |
|------|------------|
| **Kölcsönzési Időtartam** | Maximum napok száma, ameddig egy könyv kölcsönözhető, mielőtt késedelmessé válna |
| **Büntetés** | Büntetési összeg, amit a késedelmes könyvekért számítanak (50 Ft/nap) |
| **Aktív Kölcsönzés** | Jelenleg kölcsönzött és még vissza nem hozott könyv |
| **Késedelmes** | Olyan könyv, amelyet nem hoztak vissza a határidőre |
| **Tag** | Regisztrált könyvtári felhasználó egyedi azonosítóval |
| **TUI** | Szöveg alapú Felhasználói Felület (terminál/konzol felület) |
| **ANSI Kódok** | Escape szekvenciák a terminál szöveg formázásához és színezéséhez |

---

## Dokumentum Verzió
- **Verzió:** 2.1
- **Dátum:** 2025-12-02
- **Státusz:** Tesztelésre vár