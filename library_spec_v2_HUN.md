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
   [AZONOSÍTÓ] Cím - Szerző - STÁTUSZ
   ```
   - **Elérhető könyvek:** Alapértelmezett színben jelennek meg
   - **Kölcsönzött könyvek:** Sárgával, "[KÖLCSÖNÖZVE]" jelzéssel és várható visszahozási dátummal
   - Példa: `[SF-042] Dűne - Frank Herbert - [KÖLCSÖNÖZVE 2025-12-15-ig]`

**Követelmények:**
- A könyveket az azonosító számrészének sorrendjében kell rendezni (SF-001, SF-002, SF-010, stb.)
- Egyértelmű vizuális különbségtétel elérhető és kölcsönzött könyvek között

---

### 3.2 Könyv Kölcsönzése

**Leírás:** A felhasználók kölcsönözhetnek egy könyvet az azonosító és tag-azonosítás megadásával.

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
     - Megerősítést jelenít meg: "Tagság létrehozva! Az azonosítód: XXXXXX"
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
   [AZONOSÍTÓ] Cím - Kölcsönözve: ÉÉÉÉ-HH-NN - Határidő: ÉÉÉÉ-HH-NN [KÉSEDELMES: +X nap, 50 Ft/nap = X Ft]
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
Büntetés = (Aktuális Dátum - Határidő) × 50 Ft/nap
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
Tagsági Azonosító | Tag Neve     | Könyv Azonosító | Késedelmes Napok | Büntetés Összege
------------------|--------------|-----------------|------------------|------------------
000123            | Kovács János | SF-005          | 5                | 250 Ft
000456            | Nagy Anna    | DR-012          | 12               | 600 Ft
                                                     ÖSSZESEN:         | 850 Ft
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
   - Példa: "Legnépszerűbb Kategória: Sci-fi (142 kölcsönzés)"

2. **Leggyakrabban Késő Tag**
   - Mérőszám: Késedelmes visszahozások száma tagonként
   - Megjelenítés: Tagsági azonosító, név és késések száma
   - Példa: "Leggyakrabban Késő: Kovács János (Azonosító: 000123) - 8 késés"

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
- Engedélyezett karakterek: Magyar ABC betűi, szóközök, kötőjel (-)
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

#### 4.2.1 Absztrakt Book Osztály

```java
public abstract class Book {
    private final String id;           // Egyedi azonosító (pl. "SF-042")
    private final String title;        // Könyv címe
    private final String author;       // Szerző neve
    private String loanedTo;           // Tagsági azonosító (null ha elérhető)
    
    // Konstruktor
    public Book(String id, String title, String author) { ... }
    
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

#### 4.2.2 Book Alosztályok

Minden kategóriának saját osztálya van, amely a Book osztályt örökli:

**SciFi.java**
```java
public class SciFi extends Book {
    private static final int LOAN_DURATION = 14;  // napok
    private static int count = 0;                  // Példányszámláló
    
    public SciFi(String id, String title, String author) {
        super(id, title, author);
        count++;
    }
    
    @Override
    public int getLoanDuration() { return LOAN_DURATION; }
    
    @Override
    public String getCategory() { return "Sci-fi"; }
    
    public static int getCount() { return count; }
}
```

**Hasonló struktúra a többi osztályhoz:**
- `Drama.java` (LOAN_DURATION = 28)
- `History.java` (LOAN_DURATION = 21)
- `Children.java` (LOAN_DURATION = 14)
- `Technical.java` (LOAN_DURATION = 7)

---

#### 4.2.3 Member Osztály

```java
public class Member {
    private final String memberId;     // Egyedi azonosító (000001-999999)
    private final String name;         // Tag neve
    private int loanedBooks;           // Aktív kölcsönzések száma (max 3)
    
    // Konstruktor
    public Member(String memberId, String name) { ... }
    
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

#### 4.2.4 Loan Osztály

```java
public class Loan {
    private final String bookId;
    private final String memberId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    
    public Loan(String bookId, String memberId, LocalDate loanDate, int loanDuration) {
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
- Egy kölcsönzés soronként
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
- Egy büntetés soronként könyvenként

---

### 5.3 Fájl Frissítési Stratégia

**Frissítési Időzítés:**
- A fájlok **azonnal** frissülnek minden művelet után (kölcsönzés, visszahozás, tag létrehozás)
- Atomi írási műveletek használata (írás ideiglenes fájlba, majd átnevezés) az adatsérülés megelőzéséhez

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

#### 6.1.1 InvalidBookIdException
**Mikor dobódik:** 
- Könyv azonosító formátuma helytelen (nem felel meg a [KATEGÓRIA]-[SZÁM] mintának)
- Könyv azonosító nem létezik a rendszerben

**Példa üzenetek:**
```
"Érvénytelen könyv azonosító: 'XY-123'. Elvárt formátum: [SF|DR|HS|CH|TC]-[001-999]"
"Az 'SF-999' könyv azonosító nem létezik a könyvtárban."
```

---

#### 6.1.2 InvalidMemberException
**Mikor dobódik:**
- Tagsági azonosító formátuma helytelen
- Tagsági azonosító nem létezik a rendszerben
- Tag elérte a maximum kölcsönzési limitet (3 könyv)

**Példa üzenetek:**
```
"Érvénytelen tagsági azonosító: 'ABC123'. Elvárt numerikus azonosító: 000001-999999"
"A '000999' tagsági azonosító nem található."
"A '000042' tag elérte a maximum kölcsönzési limitet (3 könyv)."
```

---

#### 6.1.3 InvalidInputException
**Mikor dobódik:**
- Felhasználói bemenet nem felel meg az érvényesítésnek (név, cím, szerző)
- Érvénytelen menüválasztás
- Érvénytelen dátum formátum

**Példa üzenetek:**
```
"Érvénytelen név: 'János123'. A nevek csak betűket, szóközöket és kötőjeleket tartalmazhatnak."
"Érvénytelen menüválasztás: '9'. Kérjük válasszon 1-6 között."
```

---

#### 6.1.4 FileOperationException
**Mikor dobódik:**
- Fájl nem olvasható vagy írható
- Fájl formátum sérült
- Hiányzó szükséges adatfájlok

**Példa üzenetek:**
```
"Hiba a 'loanableBooks.txt' olvasásakor: Fájl nem található."
"Adatsérülés észlelve a 'members.txt' fájlban a 42. sorban."
"Nem sikerült menteni a kölcsönzési adatokat. A változások visszagörgetésre kerültek."
```

---

### 6.2 Kivételkezelési Stratégia

**Felhasználói Felület Szintjén:**
1. Minden kivételt elkapni a UI határon
2. Felhasználóbarát hibaüzenetek megjelenítése
3. Helyreállítási lehetőségek felajánlása:
   - "Nyomj Entert a menübe való visszatéréshez"
   - "Nyomj R-t az újrapróbálkozáshoz"
4. Az alkalmazás soha ne omoljon össze

**Példa:**
```java
try {
    loanBook(bookId, memberId);
} catch (InvalidBookIdException e) {
    displayError(e.getMessage());
    promptReturnToMenu();
} catch (InvalidMemberException e) {
    displayError(e.getMessage());
    offerMemberCreation();
} catch (FileOperationException e) {
    displayError("Rendszerhiba: " + e.getMessage());
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
┌─────────────────────────────────┐
│ Elem             │ Színkód      │
├──────────────────┼──────────────┤
│ Cím/Fejléc       │ Cián (Félkövér) │
│ Menü Opciók      │ Fehér        │
│ Kiválasztott Opció│ Zöld (Félkövér) │
│ Elérhető Könyvek │ Zöld         │
│ Kölcsönzött Könyvek│ Sárga      │
│ Késedelmes Tételek│ Piros (Félkövér) │
│ Hibaüzenetek     │ Piros        │
│ Siker Üzenetek   │ Zöld         │
│ Bemenet Prompt   │ Fehér        │
└─────────────────────────────────┘
```

---

### 7.2 Menü Struktúra

```
╔══════════════════════════════════════╗
║   KÖNYVTÁRI KEZELŐ RENDSZER          ║
╚══════════════════════════════════════╝

1. Könyvek Listázása Kategóriánként
2. Könyv Kölcsönzése
3. Könyv Visszahozása
4. Könyvek Keresése
5. Összes Büntetés Listázása
6. Statisztikák Megtekintése
7. Kilépés

Válasszon egy opciót (1-7): _
```

**Navigáció:**
- Számgombok (1-7) menüopciók kiválasztásához
- Enter megerősítéshez
- ESC előző menübe való visszatéréshez (almenükben)
- Nyíl gombok a lista nézetekben történő kiválasztáshoz (opcionális fejlesztés)

---

### 7.3 Minta Képernyő Folyamatok

#### 7.3.1 Könyvek Listázása Kategóriánként

```
╔══════════════════════════════════════╗
║   KATEGÓRIA VÁLASZTÁS                ║
╚══════════════════════════════════════╝

1. Sci-fi (14 könyv)
2. Dráma (8 könyv)
3. Történelem (12 könyv)
4. Gyermekkönyv (10 könyv)
5. Szakkönyv (6 könyv)
6. Vissza a Főmenübe

Válasszon kategóriát (1-6): 1

╔══════════════════════════════════════╗
║   SCI-FI KÖNYVEK                     ║
╚══════════════════════════════════════╝

[SF-001] Dűne - Frank Herbert
         [ELÉRHETŐ]

[SF-042] Alapítvány - Isaac Asimov
         [KÖLCSÖNÖZVE 2025-12-15-ig]

[SF-105] Mentőexpedíció - Andy Weir
         [ELÉRHETŐ]

Nyomj Entert a menübe való visszatéréshez...
```

---

#### 7.3.2 Könyv Kölcsönzése

```
╔══════════════════════════════════════╗
║   KÖNYV KÖLCSÖNZÉSE                  ║
╚══════════════════════════════════════╝

Írd be a Könyv Azonosítót: SF-042
Írd be a Tagsági Azonosítót: 000123

✓ Könyv: Alapítvány - Isaac Asimov
✓ Tag: Kovács János (Azonosító: 000123)
✓ Jelenlegi kölcsönzések: 1/3

Kölcsönzés Dátuma: 2025-12-01
Határidő: 2025-12-15 (14 nap)

Megerősíted a kölcsönzést? (I/N): I

✓ Könyv sikeresen kölcsönözve!

Nyomj Entert a menübe való visszatéréshez...
```

---

#### 7.3.3 Könyv Visszahozása (Büntetéssel)

```
╔══════════════════════════════════════╗
║   KÖNYV VISSZAHOZÁSA                 ║
╚══════════════════════════════════════╝

Írd be a Tagsági Azonosítót: 000123

Aktív kölcsönzések - Kovács János:

[SF-005] Neurománc
         Kölcsönözve: 2025-10-15 | Határidő: 2025-10-29
         ⚠ KÉSEDELMES: 33 nap | Büntetés: 1 650 Ft

[DR-012] Rómeó és Júlia
         Kölcsönözve: 2025-11-20 | Határidő: 2025-12-18
         ✓ Időben (17 nap van még)

Írd be a visszahozandó könyv azonosítóját: SF-005

⚠ BÜNTETÉSI ÉRTESÍTÉS
A könyv 33 napja késedelmes.
Teljes büntetés: 1 650 Ft (33 nap × 50 Ft/nap)

Megerősíted a visszahozást? (I/N): I

✓ Könyv sikeresen visszahozva.
✓ Büntetés rögzítve: 1 650 Ft

Nyomj Entert a menübe való visszatéréshez...
```

---

#### 7.3.4 Statisztikák

```
╔══════════════════════════════════════╗
║   KÖNYVTÁRI STATISZTIKÁK             ║
╚══════════════════════════════════════╝

📊 Legnépszerűbb Kategória:
   Sci-fi (142 összes kölcsönzés)

⏰ Leggyakrabban Késő Tag:
   Kovács János (Azonosító: 000123)
   Késések száma: 8

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

További információk:
• Összes könyv a leltárban: 50
• Jelenleg kölcsönzött könyvek: 23
• Összes kintlévő büntetés: 4 350 Ft

Nyomj Entert a menübe való visszatéréshez...
```

---

#### 7.3.5 Büntetések Listázása

```
╔══════════════════════════════════════╗
║   AKTÍV BÜNTETÉSEK                   ║
╚══════════════════════════════════════╝

Tagsági Az. | Tag Neve      | Könyv Az. | Késés | Büntetés
------------|---------------|-----------|-------|----------
000123      | Kovács János  | SF-005    | 33 nap| 1 650 Ft
000456      | Nagy Anna     | DR-012    | 12 nap|   600 Ft
000089      | Szabó Péter   | TC-003    | 21 nap| 1 050 Ft
000201      | Kiss Éva      | HS-008    | 5 nap |   250 Ft
                                    ÖSSZESEN:  | 3 550 Ft

Nyomj Entert a menübe való visszatéréshez...
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
- `Book` és minden alosztály (SciFi, Drama, stb.)
- `Member`
- `Loan` (különösen a büntetésszámítás)
- `Fine`
- Bemenet érvényesítő metódusok

**Teszt Esetek (Minimum):**

**Book Osztály:**
- Megváltoztathatatlan mezők nem változtathatók
- Kölcsönzési időtartam helyes minden kategóriára
- Példányszámlálók helyesen inkrementálódnak

**Member Osztály:**
- Kölcsönzési limit betartása (max 3)
- Kölcsönzésszám helyes inkrementálása/dekrementálása

**Loan Osztály:**
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
│   ├── Main.java                  // Belépési pont
│   ├── model/
│   │   ├── Book.java              // Absztrakt osztály
│   │   ├── SciFi.java
│   │   ├── Drama.java
│   │   ├── History.java
│   │   ├── Children.java
│   │   ├── Technical.java
│   │   ├── Member.java
│   │   ├── Loan.java
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
│   │   ├── InvalidBookIdException.java
│   │   ├── InvalidMemberException.java
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
- Tartsd a metódusokat fókuszáltnak és tömörnek (Egyszeri Felelősség Elve)
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
- **Verzió:** 2.0
- **Dátum:** 2025-12-01
- **Státusz:** Implementálásra Jóváhagyva