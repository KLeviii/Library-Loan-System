# Library Loan System - Technical Specification

## Table of Contents
1. [System Overview](#system-overview)
2. [System Requirements](#system-requirements)
3. [Functional Requirements](#functional-requirements)
4. [Data Model](#data-model)
5. [File Formats and Data Persistence](#file-formats-and-data-persistence)
6. [Exception Handling](#exception-handling)
7. [User Interface](#user-interface)
8. [Additional Features](#additional-features)
9. [Testing Requirements](#testing-requirements)

---

## 1. System Overview

### 1.1 Purpose
The system is a simple library management and loan system that enables book registration, listing, and deletion for library members (who are also tracked by the system). The system manages loans, returns, and fine tracking, while providing statistics on popular categories and frequently late members.

### 1.2 Core Features
- Book inventory management across 5 categories
- Member registration and management
- Loan and return processing
- Automatic fine calculation for overdue books
- Statistics and reporting
- Terminal User Interface (TUI) with ANSI color codes

---

## 2. System Requirements

### 2.1 Operating System
- **Target OS:** Windows 10 or Windows 11 (64-bit)
- **Guaranteed stable operation** on these platforms

### 2.2 Development Environment
- **IDE:** JetBrains IntelliJ IDEA Community Edition 2025.1 or newer
- **Programming Language:** Java
- **JDK Version:** JDK 21 or newer
- **Code Language:** All code, comments, and UI text must be in English

### 2.3 Hardware Requirements
- **RAM:** Minimum 2GB for IDE, 8GB recommended for system
- **Storage:** 3.5GB free disk space (SSD recommended)
- **Display:** Minimum resolution 1024x768
- **Processor:** 64-bit compatible

---

## 3. Functional Requirements

### 3.1 List Books by Category

**Description:** Users can view the current inventory of books filtered by category.

**Behavior:**
1. System displays list of available categories (Sci-fi, Drama, History, Children, Technical)
2. User selects desired category
3. System displays books sorted by the numeric portion of their ID (ascending order)
4. Display format for each book:
   ```
   [ID] Title by Author - STATUS
   ```
   - **Available books:** Display in default color
   - **Loaned books:** Display in yellow with "[LOANED]" tag and expected return date
   - Example: `[SF-042] Dune by Frank Herbert - [LOANED until 2025-12-15]`

**Requirements:**
- Books must be sorted by numeric ID portion (SF-001, SF-002, SF-010, etc.)
- Clear visual distinction between available and loaned books

---

### 3.2 Loan a Book

**Description:** Users can loan a book by providing its ID and member identification.

**Process Flow:**
1. User enters book ID
2. System validates book ID format and availability
3. If book is available:
   - System prompts for member ID
   - **Existing member:** System verifies member ID
   - **New member:** 
     - System notifies user that they are not yet a member
     - Prompts for name input
     - Generates unique member ID (range: 1-999999, sequential)
     - Saves new member data
     - Displays confirmation: "Membership created! Your ID: XXXXXX"
4. System checks if member has fewer than 3 active loans
5. System records loan with current date
6. System updates all relevant data files
7. Display confirmation with loan details and return due date

**Business Rules:**
- Maximum 3 concurrent loans per member
- Book must be available (not currently loaned)
- Member ID must be valid (existing or newly created)

**Error Cases:**
- Book ID does not exist → Display error, return to menu
- Book already loaned → Display expected return date, offer to reserve (if feature enabled)
- Member has 3 active loans → Display error message, list their current loans
- Invalid name format → Display error, re-prompt

---

### 3.3 Return a Book

**Description:** Users can return loaned books and view associated fines if applicable.

**Process Flow:**
1. User enters member ID
2. System displays member's active loans sorted by loan date (oldest first)
3. Display format:
   ```
   [ID] Title - Loaned: YYYY-MM-DD - Due: YYYY-MM-DD [OVERDUE: +X days, 50 HUF/day = Y HUF]
   ```
   - **Overdue books:** Display in red with fine calculation
   - **On-time books:** Display in green
4. User selects book to return (by entering book ID)
5. If overdue:
   - System calculates total fine (days overdue × 50 HUF)
   - Displays fine amount
   - Records fine in fines.txt
6. System updates loan status
7. System updates all relevant data files
8. Display confirmation

**Fine Calculation:**
```
(current date - due date) × 50 HUF per day
```

**Requirements:**
- Real-time fine calculation based on current system date
- Fines are recorded even if not paid immediately
- Clear visual indication of overdue status

---

### 3.4 Search Books

**Description:** Users can search for books using multiple criteria.

**Search Options:**
1. **By Book ID:** Exact match (e.g., "SF-042")
2. **By Title:** Partial match, case-insensitive
3. **By Author:** Partial match, case-insensitive

**Display:** Show search results with same format as book listing, including availability status.

---

### 3.5 List All Fines

**Description:** Display all active fines across all members.

**Display Format:**
```
Member ID | Member Name | Book ID | Days Overdue | Fine Amount
----------|-------------|---------|--------------|------------
000123    | John Doe    | SF-005  | 5            | 250 HUF
000456    | Jane Smith  | DR-012  | 12           | 600 HUF
                                   TOTAL:        | 850 HUF
```

**Requirements:**
- Show only unpaid fines
- Calculate total outstanding fines
- Sort by fine amount (descending)

---

### 3.6 Statistics

**Description:** Display library usage statistics.

**Statistics to Display:**

1. **Most Popular Category**
   - Metric: Total number of loans (all-time) per category
   - Display: Category name and loan count
   - Example: "Most Popular Category: Sci-fi (142 loans)"

2. **Most Frequently Late Member**
   - Metric: Total number of overdue returns per member
   - Display: Member ID, name, and count of late returns
   - Example: "Most Frequently Late: John Doe (ID: 000123) - 8 late returns"

**Additional Statistics (Optional Display):**
- Total books in inventory
- Total active loans
- Average loan duration per category

---

### 3.7 Input Validation

**Book Title Validation:**
- Allowed characters: Hungarian alphabet letters (A-Z, Á, É, Í, Ó, Ö, Ő, Ú, Ü, Ű), spaces, digits, hyphen (-), comma (,), period (.)
- Length: 1-200 characters
- Example valid titles: "1984", "Harry Potter és a bölcsek köve", "X-Men - Origins"

**Author Name Validation:**
- Allowed characters: Hungarian alphabet letters, spaces, hyphen (-), apostrophe (')
- Length: 2-100 characters
- Must contain at least one letter
- Example valid names: "Isaac Asimov", "Móra Ferenc", "Gárdonyi Géza"

**Member Name Validation:**
- Same rules as Author Name
- Minimum 2 characters, maximum 100 characters

**Book ID Format:**
- Pattern: `[CATEGORY_CODE]-[NUMBER]`
- Category codes: SF, DR, HS, CH, TC
- Number: 001-999 (zero-padded to 3 digits)
- Example: "SF-042", "DR-001"

**Member ID Format:**
- Range: 1-999999
- Generated sequentially by the system
- Format: Zero-padded to 6 digits (e.g., "000001", "012345")

---

## 4. Data Model

### 4.1 Book Categories

| Category | Max Loan Duration | ID Prefix | Description |
|----------|-------------------|-----------|-------------|
| **Sci-fi** | 14 days | SF | Science fiction books |
| **Drama** | 28 days | DR | Drama and theatrical works |
| **History** | 21 days | HS | Historical books and biographies |
| **Children** | 14 days | CH | Children's literature |
| **Technical** | 7 days | TC | Technical and educational books |

---

### 4.2 Class Structure

#### 4.2.1 Abstract Book Class

```java
public abstract class Book {
    private final String id;           // Unique identifier (e.g., "SF-042")
    private final String title;        // Book title
    private final String author;       // Author name
    private String loanedTo;           // Member ID (null if available)
    
    // Constructor
    public Book(String id, String title, String author) { ... }
    
    // Getters only for immutable fields
    public String getId() { ... }
    public String getTitle() { ... }
    public String getAuthor() { ... }
    
    // Getter and setter for loanedTo
    public String getLoanedTo() { ... }
    public void setLoanedTo(String memberId) { ... }
    
    // Abstract method to be implemented by subclasses
    public abstract int getLoanDuration();
    public abstract String getCategory();
}
```

#### 4.2.2 Book Subclasses

Each category has its own class inheriting from Book:

**SciFi.java**
```java
public class SciFi extends Book {
    private static final int LOAN_DURATION = 14;  // days
    private static int count = 0;                  // Instance counter
    
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

**Similar structure for:**
- `Drama.java` (LOAN_DURATION = 28)
- `History.java` (LOAN_DURATION = 21)
- `Children.java` (LOAN_DURATION = 14)
- `Technical.java` (LOAN_DURATION = 7)

---

#### 4.2.3 Member Class

```java
public class Member {
    private final String memberId;     // Unique ID (000001-999999)
    private final String name;         // Member name
    private int loanedBooks;           // Count of active loans (max 3)
    
    // Constructor
    public Member(String memberId, String name) { ... }
    
    // Getters
    public String getMemberId() { ... }
    public String getName() { ... }
    public int getLoanedBooks() { ... }
    
    // Setters
    public void setLoanedBooks(int count) { ... }
    
    // Helper methods
    public boolean canLoanMore() { return loanedBooks < 3; }
    public void incrementLoans() { loanedBooks++; }
    public void decrementLoans() { loanedBooks--; }
}
```

---

#### 4.2.4 Loan Class

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
    
    // Calculate days overdue (0 if not overdue)
    public int getDaysOverdue() {
        LocalDate today = LocalDate.now();
        return today.isAfter(dueDate) ? 
               (int) ChronoUnit.DAYS.between(dueDate, today) : 0;
    }
    
    // Calculate fine amount
    public int calculateFine() {
        return getDaysOverdue() * 50;  // 50 HUF per day
    }
    
    public boolean isOverdue() {
        return getDaysOverdue() > 0;
    }
    
    // Getters
    public String getBookId() { ... }
    public String getMemberId() { ... }
    public LocalDate getLoanDate() { ... }
    public LocalDate getDueDate() { ... }
}
```

---

#### 4.2.5 Fine Class

```java
public class Fine {
    private final String memberId;
    private final String bookId;
    private final LocalDate overdueDate;
    private int amount;
    
    public Fine(String memberId, String bookId, LocalDate overdueDate, int amount) { ... }
    
    // Getters and setters
    public String getMemberId() { ... }
    public String getBookId() { ... }
    public int getAmount() { ... }
    public void setAmount(int amount) { ... }
}
```

---

## 5. File Formats and Data Persistence

### 5.1 File Storage Location
All data files must be stored in the project's `data/` directory:
```
project-root/
├── src/
├── data/
│   ├── loanableBooks.txt
│   ├── members.txt
│   ├── loanedBooks.txt
│   └── fines.txt
└── ...
```

---

### 5.2 File Formats

#### 5.2.1 loanableBooks.txt
**Format:** `bookId;title;author`

**Example:**
```
SF-001;Dune;Frank Herbert
SF-042;Foundation;Isaac Asimov
DR-005;Hamlet;William Shakespeare
HS-012;Sapiens;Yuval Noah Harari
CH-008;Harry Potter and the Philosopher's Stone;J.K. Rowling
TC-003;Clean Code;Robert C. Martin
```

**Rules:**
- Book ID numeric portion zero-padded to 3 digits
- One book per line
- Semicolon (;) as delimiter
- No trailing semicolon
- UTF-8 encoding to support Hungarian characters

---

#### 5.2.2 members.txt
**Format:** `memberId;name`

**Example:**
```
000001;John Doe
000042;Jane Smith
000105;Kovács János
```

**Rules:**
- Member ID is zero-padded to 6 digits
- One member per line
- Semicolon (;) as delimiter
- No trailing semicolon
- UTF-8 encoding

---

#### 5.2.3 loanedBooks.txt
**Format:** `bookId;memberId;loanDate`

**Example:**
```
SF-001;000042;2025-11-20
DR-005;000001;2025-11-28
HS-012;000042;2025-11-15
```

**Rules:**
- Date format: YYYY-MM-DD (ISO 8601)
- Member ID is zero-padded to 6 digits
- One loan per line
- Semicolon (;) as delimiter
- No trailing semicolon
- Active loans only (returned books are removed)

---

#### 5.2.4 fines.txt
**Format:** `memberId;bookId;overdueDate;amount`

**Example:**
```
000001;SF-042;2025-11-10;350
000042;DR-008;2025-11-25;150
```

**Rules:**
- Amount in HUF (integer)
- Overdue date is the date the book became overdue (due date + 1 day)
- Fines remain until paid/cleared
- Book ID numeric portion zero-padded to 3 digits
- One fine per line per book
- Semicolon (;) as delimiter
- No trailing semicolon

---

### 5.3 File Update Strategy

**Update Timing:**
- Files are updated **immediately** after each operation (loan, return, member creation)
- Use atomic write operations (write to temp file, then rename) to prevent data corruption

**Update Operations:**

| Action | Files Updated |
|--------|---------------|
| Loan a book | `loanedBooks.txt` (add entry), `members.txt` (update loan count if new member) |
| Return a book | `loanedBooks.txt` (remove entry), `fines.txt` (add if overdue) |
| Create member | `members.txt` (add entry) |
| Clear fine | `fines.txt` (remove entry) |

**Error Handling:**
- If file write fails, roll back in-memory changes
- Log error details for debugging
- Display user-friendly error message

---

## 6. Exception Handling

### 6.1 Custom Exception Classes

#### 6.1.1 InvalidBookIdException
**When thrown:** 
- Book ID format is incorrect (not matching [CATEGORY]-[NUMBER])
- Book ID does not exist in the system

**Example message:**
```
"Invalid book ID: 'XY-123'. Expected format: [SF|DR|HS|CH|TC]-[001-999]"
"Book ID 'SF-999' does not exist in the library."
```

---

#### 6.1.2 InvalidMemberException
**When thrown:**
- Member ID format is incorrect
- Member ID does not exist in the system
- Member has reached maximum loan limit (3 books)

**Example message:**
```
"Invalid member ID: 'ABC123'. Expected numeric ID: 000001-999999"
"Member ID '000999' not found."
"Member '000042' has reached the maximum loan limit (3 books)."
```

---

#### 6.1.3 InvalidInputException
**When thrown:**
- User input fails validation (name, title, author)
- Invalid menu selection
- Invalid date format

**Example message:**
```
"Invalid name: 'John123'. Names can only contain letters, spaces, and hyphens."
"Invalid menu choice: '9'. Please select 1-6."
```

---

#### 6.1.4 FileOperationException
**When thrown:**
- File cannot be read or written
- File format is corrupted
- Missing required data files

**Example message:**
```
"Error reading 'loanableBooks.txt': File not found."
"Data corruption detected in 'members.txt' at line 42."
"Failed to save loan data. Changes have been rolled back."
```

---

### 6.2 Exception Handling Strategy

**User Interface Level:**
1. Catch all exceptions at the TUI boundary
2. Display user-friendly error messages
3. Offer recovery options:
   - "Press Enter to return to menu"
   - "Press R to retry"
4. Never crash the application

**Example:**
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
    displayError("System error: " + e.getMessage());
    logError(e);
    promptReturnToMenu();
}
```

---

## 7. User Interface

### 7.1 TUI Design Principles

**Technology:**
- ANSI escape codes for colors and formatting
- Standard input/output (System.in, System.out)
- Clear screen functionality between views

**Color Scheme:**
```
┌─────────────────────────────────┐
│ Element          │ Color Code   │
├──────────────────┼──────────────┤
│ Title/Header     │ Cyan (Bold)  │
│ Menu Options     │ White        │
│ Selected Option  │ Green (Bold) │
│ Available Books  │ Green        │
│ Loaned Books     │ Yellow       │
│ Overdue Items    │ Red (Bold)   │
│ Error Messages   │ Red          │
│ Success Messages │ Green        │
│ Input Prompt     │ White        │
└─────────────────────────────────┘
```

---

### 7.2 Menu Structure

```
╔══════════════════════════════════════╗
║   LIBRARY MANAGEMENT SYSTEM          ║
╚══════════════════════════════════════╝

1. List Books by Category
2. Loan a Book
3. Return a Book
4. Search Books
5. List All Fines
6. View Statistics
7. Exit

Select an option (1-7): _
```

**Navigation:**
- Number keys (1-7) to select menu options
- Enter or y/n prompt to confirm
- ESC or Enter to return to previous menu (in submenus)
- Arrow keys for selection in list views (optional enhancement)

---

### 7.3 Sample Screen Flows

#### 7.3.1 List Books by Category

```
╔══════════════════════════════════════╗
║   SELECT CATEGORY                    ║
╚══════════════════════════════════════╝

1. Sci-fi (14 books)
2. Drama (8 books)
3. History (12 books)
4. Children (10 books)
5. Technical (6 books)
6. Back to Main Menu

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

#### 7.3.2 Loan a Book

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

Confirm loan? (y/n): y

✓ Book successfully loaned!

Press Enter to return to menu...
```

---

#### 7.3.3 Return a Book (with Fine)

```
╔══════════════════════════════════════╗
║   RETURN A BOOK                      ║
╚══════════════════════════════════════╝

Enter Member ID: 000123

Active loans for John Doe:

[SF-005] Neuromancer
         Loaned: 2025-10-15 | Due: 2025-10-29
         ⚠ OVERDUE: 33 days | Fine: 1,650 HUF

[DR-012] Romeo and Juliet
         Loaned: 2025-11-20 | Due: 2025-12-18
         ✓ On time (17 days remaining)

Enter Book ID to return: SF-005

⚠ FINE NOTICE
Book is 33 days overdue.
Total fine: 1,650 HUF (33 days × 50 HUF/day)

Confirm return? (y/n): y

✓ Book returned successfully.
✓ Fine recorded: 1,650 HUF

Press Enter to return to menu...
```

---

#### 7.3.4 Statistics

```
╔══════════════════════════════════════╗
║   LIBRARY STATISTICS                 ║
╚══════════════════════════════════════╝

📊 Most Popular Category:
   Sci-fi (142 total loans)

⏰ Most Frequently Late Member:
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

#### 7.3.5 List fines

```
╔══════════════════════════════════════╗
║   UNPAID FINES                       ║
╚══════════════════════════════════════╝

Member ID   | Member Name   | Book ID   | Late    | Fine
------------|---------------|-----------|---------|----------
000123      | Kovács János  | SF-005    | 33 days | 1 650 Ft
000456      | Nagy Anna     | DR-012    | 12 days |   600 Ft
000089      | Szabó Péter   | TC-003    | 21 days | 1 050 Ft
000201      | Kiss Éva      | HS-008    | 5 days  |   250 Ft
                                        TOTAL:    | 3 550 Ft

Press Enter to return to menu...
```

---

## 8. Additional Features

### 8.1 Feature Slot #1
**[Reserved for additional feature - to be specified]**

---

### 8.2 Feature Slot #2
**[Reserved for additional feature - to be specified]**

---

## 9. Testing Requirements

### 9.1 Unit Testing

**Classes to Test:**
- `Book` and all subclasses (SciFi, Drama, etc.)
- `Member`
- `Loan` (especially fine calculation)
- `Fine`
- Input validation methods

**Test Cases (Minimum):**

**Book Class:**
- Verify immutable fields cannot be changed
- Verify loan duration for each category
- Verify instance counters increment correctly

**Member Class:**
- Verify loan limit enforcement (max 3)
- Verify loan count increment/decrement

**Loan Class:**
- Calculate correct due date for each category
- Calculate correct fine amount for various overdue periods
- Handle non-overdue cases (fine = 0)

**Input Validation:**
- Accept valid book titles, author names, member names
- Reject invalid characters
- Reject out-of-range lengths

---

### 9.2 Integration Testing

**File Operations:**
- Successfully load all data files on startup
- Handle missing files gracefully (create empty files)
- Handle corrupted data (skip invalid lines, log errors)
- Successfully save changes after each operation

**Loan Workflow:**
- Complete loan process (select book → verify member → record loan)
- Prevent loan when member has 3 active loans
- Prevent loan of already-loaned book

**Return Workflow:**
- Return on-time book (no fine)
- Return overdue book (calculate and record fine)
- Update member's loan count correctly

---

### 9.3 Sample Test Data

**Create these test files in `data/` directory:**

**loanableBooks.txt:**
```
SF-001;Dune;Frank Herbert
SF-002;Foundation;Isaac Asimov
DR-001;Hamlet;William Shakespeare
HS-001;Sapiens;Yuval Noah Harari
CH-001;Harry Potter;J.K. Rowling
TC-001;Clean Code;Robert C. Martin
```

**members.txt:**
```
000001;Test User One
000002;Test User Two
000003;Test User Three
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

### 9.4 Edge Cases to Test

1. **Member with exactly 3 loans tries to loan a 4th**
2. **Return a book on the exact due date (should have 0 fine)**
3. **Return a book 1 day overdue (should have 50 HUF fine)**
4. **New member creation with duplicate name (should be allowed, different IDs)**
5. **Book ID with leading zeros (SF-001 vs SF-1)**
6. **Member name with Hungarian characters (Á, É, Í, etc.)**
7. **Very long title (200 characters at the limit)**
8. **File contains UTF-8 BOM (should handle gracefully)**
9. **Date calculations across month/year boundaries**
10. **Sequential member ID generation approaching max (999999)**

---

## 10. Implementation Notes

### 10.1 Recommended Libraries
- **Date/Time:** `java.time.LocalDate` (built-in, JDK 21)
- **File I/O:** `java.nio.file.Files` (built-in)
- **Collections:** `java.util.ArrayList`, `java.util.HashMap` (built-in)
- **ANSI Colors:** Implement custom utility class or use external library (e.g., Jansi)

### 10.2 Code Organization
```
src/
├── main/
│   ├── Main.java                  // Entry point
│   ├── model/
│   │   ├── Book.java              // Abstract class
│   │   ├── SciFi.java
│   │   ├── Drama.java
│   │   ├── History.java
│   │   ├── Children.java
│   │   ├── Technical.java
│   │   ├── Member.java
│   │   ├── Loan.java
│   │   └── Fine.java
│   ├── service/
│   │   ├── BookService.java       // Book operations
│   │   ├── MemberService.java     // Member operations
│   │   ├── LoanService.java       // Loan operations
│   │   └── FileService.java       // File I/O operations
│   ├── ui/
│   │   ├── MenuUI.java            // Main menu
│   │   ├── ConsoleUI.java         // Console utilities
│   │   └── AnsiColors.java        // ANSI color codes
│   ├── exception/
│   │   ├── InvalidBookIdException.java
│   │   ├── InvalidMemberException.java
│   │   ├── InvalidInputException.java
│   │   └── FileOperationException.java
│   └── util/
│       ├── InputValidator.java    // Input validation
│       └── DateUtils.java         // Date utilities
└── test/
    └── [Unit tests mirror src structure]
```

### 10.3 Best Practices
- Use meaningful variable and method names
- Add JavaDoc comments for all public methods
- Follow Java naming conventions (camelCase for variables, PascalCase for classes)
- Keep methods focused and concise (Single Responsibility Principle)
- Use constants for magic numbers (e.g., `MAX_LOANS = 3`, `FINE_PER_DAY = 50`)
- Handle exceptions at appropriate levels
- Log important operations (loan, return, member creation)

---

## 11. Glossary

| Term | Definition |
|------|------------|
| **Loan Duration** | Maximum number of days a book can be borrowed before becoming overdue |
| **Fine** | Penalty amount charged for overdue books (50 HUF per day) |
| **Active Loan** | A book currently loaned out and not yet returned |
| **Overdue** | A book not returned by its due date |
| **Member** | Registered library user with unique ID |
| **TUI** | Text-based User Interface (terminal/console interface) |
| **ANSI Codes** | Escape sequences for terminal text formatting and colors |

---

## Document Version
- **Version:** 2.1
- **Date:** 2025-12-02
- **Status:** Awaiting testing