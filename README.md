<div align="center">

# 💸 STUDENT EXPENSE TRACKER

### Smart Money Management for Students

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=24&duration=2500&pause=800&color=00C2FF&center=true&vCenter=true&repeat=true&width=850&lines=Track+Expenses+%E2%80%A2+Control+Budgets+%E2%80%A2+Understand+Spending;Java+%E2%80%A2+Android+%E2%80%A2+SQLite+%E2%80%A2+Retrofit;Analytics+%E2%80%A2+Currency+Conversion+%E2%80%A2+Smart+Budgeting;Built+by+Mr.Ahamed" alt="Student Expense Tracker typing animation" />

<br/>

[![Android](https://img.shields.io/badge/Android-Student_Expense_Tracker-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-Primary_Language-F89820?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Gradle](https://img.shields.io/badge/Gradle-Build_System-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![Material Design](https://img.shields.io/badge/Material-UI-6750A4?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)

<br/>

[![Repository](https://img.shields.io/badge/Repository-Student--Expense--Tracker-181717?style=flat-square&logo=github)](https://github.com/Ahamed369/Student-Expense-Tracker)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square)
![Min SDK](https://img.shields.io/badge/Min_SDK-24-00C853?style=flat-square)
![Target SDK](https://img.shields.io/badge/Target_SDK-34-2196F3?style=flat-square)
![Version](https://img.shields.io/badge/Version-1.0-7C4DFF?style=flat-square)
![Status](https://img.shields.io/badge/Status-Completed-success?style=flat-square)

<br/>

**A modern Android personal-finance application designed to help students record expenses, manage category budgets, analyze spending patterns, and convert LKR into international currencies from one clean mobile interface.**

<br/>

[🚀 Explore Features](#-core-features) •
[🛠 Tech Stack](#-technology-stack) •
[🏗 Architecture](#-application-architecture) •
[📁 Structure](#-project-structure) •
[⚙️ Installation](#️-installation--setup) •
[👨‍💻 Developer](#-developer)

</div>

---

## 🌈 PROJECT OVERVIEW

Student Expense Tracker is an Android financial-management application built to provide students with a simple way to understand and control their day-to-day spending.

Instead of treating expense management as a single list of transactions, the application combines **four major financial functions** inside one Android application:

> 💸 **Expense Tracking** → 💰 **Budget Management** → 📊 **Financial Analytics** → 💱 **Currency Conversion**

The application uses a single-activity architecture with multiple fragments and persistent bottom navigation, allowing users to move smoothly between financial tools while maintaining a consistent interface.

---

## ✨ CORE FEATURES

<table>
<tr>
<td width="50%" valign="top">

### 💸 Expense Management

- Add daily expenses
- Record expense title
- Enter amount in LKR
- Select expense category
- Choose transaction date
- Add optional notes
- Display transactions using RecyclerView
- Organize personal spending records

</td>
<td width="50%" valign="top">

### 💰 Budget Management

- Create category-based budgets
- Monitor total spending
- Calculate remaining balances
- Visualize budget consumption
- Percentage-based progress indicators
- Color-coded spending thresholds
- Identify over-budget categories

</td>
</tr>

<tr>
<td width="50%" valign="top">

### 📊 Spending Analytics

- Visual financial summaries
- Category-based expenditure analysis
- Pie chart visualization
- Bar chart visualization
- Weekly spending filter
- Monthly spending filter
- All-time spending analysis

</td>
<td width="50%" valign="top">

### 💱 Currency Conversion

- Enter an amount in LKR
- Convert into international currencies
- Retrieve exchange-rate information
- Display country information
- Show currency codes and names
- Country flag representation
- Offline/cached-rate support logic

</td>
</tr>
</table>

---

## 🎯 BUDGET STATUS SYSTEM

The budget tracker uses visual thresholds to make financial status easier to understand.

```text
0% ───────────────────────────────────────────────────────► 100%

🟢 SAFE                     🟠 CAUTION                    🔴 WARNING
  < 60%                      60% – 79%                     ≥ 80%
```

| Spending Level | Status | Meaning |
|:---:|:---:|:---|
| `< 60%` | 🟢 On Track | Spending remains within a comfortable range |
| `60% – 79%` | 🟠 Caution | Budget usage is becoming significant |
| `≥ 80%` | 🔴 Warning | Spending is approaching or exceeding the limit |

The progress bar and percentage indicators update according to budget usage.

---

## 🛠 TECHNOLOGY STACK

<div align="center">

### Core Development

![Java](https://img.shields.io/badge/Java-F89820?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![XML](https://img.shields.io/badge/XML-005FAD?style=for-the-badge&logo=xml&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

### Android Components

![AndroidX](https://img.shields.io/badge/AndroidX-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Material Design](https://img.shields.io/badge/Material_Design-6750A4?style=for-the-badge&logo=materialdesign&logoColor=white)
![RecyclerView](https://img.shields.io/badge/RecyclerView-1976D2?style=for-the-badge&logo=android&logoColor=white)
![Fragments](https://img.shields.io/badge/Fragments-7B1FA2?style=for-the-badge&logo=android&logoColor=white)

### Data • Networking • Visualization

![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge&logo=square&logoColor=white)
![OkHttp](https://img.shields.io/badge/OkHttp-000000?style=for-the-badge)
![Gson](https://img.shields.io/badge/Gson-FFCA28?style=for-the-badge)
![MPAndroidChart](https://img.shields.io/badge/MPAndroidChart-FF4081?style=for-the-badge&logo=chartdotjs&logoColor=white)

</div>

---

## 🧩 TECHNOLOGY BREAKDOWN

| Technology | Role |
|---|---|
| **Java** | Main application programming language |
| **XML** | Android interface layouts and resources |
| **Android SDK** | Core mobile application platform |
| **AndroidX** | Modern Android support components |
| **Material Components** | Interface elements and navigation |
| **RecyclerView** | Efficient dynamic list rendering |
| **SQLite** | Local financial-data persistence |
| **Retrofit 2** | REST API communication |
| **OkHttp** | HTTP networking and request logging |
| **Gson** | JSON response parsing |
| **MPAndroidChart** | Pie and bar financial visualizations |
| **Gradle** | Dependency and build management |

---

## 🏗 APPLICATION ARCHITECTURE

```text
┌─────────────────────────────────────────────────────────────┐
│                    STUDENT EXPENSE TRACKER                  │
├─────────────────────────────────────────────────────────────┤
│                         MainActivity                        │
│                             │                               │
│                  BottomNavigationView                       │
│                             │                               │
│          ┌──────────────────┼──────────────────┐            │
│          │                  │                  │            │
│          ▼                  ▼                  ▼            │
│   ExpenseFragment     BudgetFragment    AnalyticsFragment   │
│          │                  │                  │            │
│          └──────────────────┼──────────────────┘            │
│                             │                               │
│                             ▼                               │
│                     CurrencyFragment                        │
├─────────────────────────────────────────────────────────────┤
│                     APPLICATION LOGIC                       │
│                                                             │
│   Adapters ───── Models ───── Utilities ───── Database      │
│                                         │                   │
│                                         ▼                   │
│                                  Network Services           │
│                                         │                   │
│                                  Retrofit / APIs            │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 APPLICATION FLOW

```text
                         ┌──────────────────────┐
                         │     Launch App       │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │     MainActivity     │
                         └──────────┬───────────┘
                                    │
                                    ▼
                      ┌────────────────────────────┐
                      │     Bottom Navigation      │
                      └─────────────┬──────────────┘
                                    │
          ┌─────────────────────────┼─────────────────────────┐
          │                         │                         │
          ▼                         ▼                         ▼
   ┌─────────────┐           ┌─────────────┐          ┌─────────────┐
   │  Expenses   │           │   Budget    │          │  Analytics  │
   └──────┬──────┘           └──────┬──────┘          └──────┬──────┘
          │                         │                         │
          ▼                         ▼                         ▼
    Add / Review              Set / Monitor              Analyze
      Expenses                  Budgets                  Spending
          │                         │                         │
          └─────────────────────────┼─────────────────────────┘
                                    │
                                    ▼
                            ┌──────────────┐
                            │   Currency   │
                            │  Converter   │
                            └──────┬───────┘
                                   │
                                   ▼
                          Exchange Rate APIs
```

---

## 🗃 DATA FLOW

```text
USER INPUT
    │
    ▼
┌───────────────────┐
│ Android UI / XML  │
└─────────┬─────────┘
          │
          ▼
┌───────────────────┐
│ Fragment / Logic  │
└─────────┬─────────┘
          │
     ┌────┴────┐
     │         │
     ▼         ▼
┌─────────┐  ┌───────────────┐
│ SQLite  │  │ Retrofit APIs │
└────┬────┘  └───────┬───────┘
     │               │
     ▼               ▼
┌──────────┐   ┌─────────────┐
│ Models   │   │ JSON / Gson │
└────┬─────┘   └──────┬──────┘
     │                │
     └────────┬───────┘
              ▼
      ┌───────────────┐
      │ RecyclerViews │
      │ Charts / UI   │
      └───────────────┘
```

---

## 🧠 MAJOR APPLICATION MODULES

### 01 — Expense Module

```text
ExpenseFragment
      │
      ├── Add Expense Dialog
      │      ├── Title
      │      ├── Amount
      │      ├── Category
      │      ├── Date
      │      └── Notes
      │
      ├── DatabaseHelper
      │
      └── ExpenseAdapter
             │
             └── RecyclerView
```

### 02 — Budget Module

```text
BudgetFragment
      │
      ├── Set Budget
      ├── Calculate Spent Amount
      ├── Calculate Remaining Amount
      ├── Calculate Usage Percentage
      └── BudgetAdapter
             │
             └── Progress Visualization
```

### 03 — Analytics Module

```text
AnalyticsFragment
      │
      ├── Weekly Filter
      ├── Monthly Filter
      ├── All-Time Filter
      │
      ├── PieChart
      └── BarChart
             │
             └── MPAndroidChart
```

### 04 — Currency Module

```text
CurrencyFragment
      │
      ├── LKR Input
      ├── Retrofit Client
      │      ├── ExchangeRateService
      │      └── RestCountriesService
      │
      ├── CurrencyResult
      └── CurrencyAdapter
             │
             └── Converted Results
```

---

## 💱 API INTEGRATION

The currency module uses Retrofit-based network services.

### Exchange-Rate Service

Used to retrieve exchange-rate data for converting Sri Lankan Rupees into supported international currencies.

### Country Information Service

Used to retrieve supporting country/currency information for the conversion interface.

```text
LKR Amount
    │
    ▼
CurrencyFragment
    │
    ▼
ApiClient
    │
    ├──────────────► ExchangeRateService
    │
    └──────────────► RestCountriesService
                         │
                         ▼
                    JSON Response
                         │
                         ▼
                        Gson
                         │
                         ▼
                   CurrencyResult
                         │
                         ▼
                   CurrencyAdapter
```

---

## 📊 ANALYTICS ENGINE

The analytics section provides graphical representations of expense information using **MPAndroidChart**.

### Pie Chart

Designed to communicate category-based spending distribution.

```text
           CATEGORY SPENDING
                  │
       ┌──────────┼──────────┐
       │          │          │
       ▼          ▼          ▼
      Food     Transport    Other
       │          │          │
       └──────────┼──────────┘
                  ▼
               PieChart
```

### Bar Chart

Designed to provide comparative spending information in an easy-to-read graphical format.

Available filters include:

```text
┌─────────────┬─────────────┬─────────────┐
│  This Week  │ This Month  │  All Time   │
└─────────────┴─────────────┴─────────────┘
```

---

## 🗂 PROJECT STRUCTURE

```text
StudentExpenseTracker/
│
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   │
│   └── src/
│       └── main/
│           │
│           ├── AndroidManifest.xml
│           │
│           ├── java/
│           │   └── com/
│           │       └── studentexpensetracker/
│           │           │
│           │           ├── MainActivity.java
│           │           │
│           │           ├── adapters/
│           │           │   ├── BudgetAdapter.java
│           │           │   ├── CurrencyAdapter.java
│           │           │   └── ExpenseAdapter.java
│           │           │
│           │           ├── api/
│           │           │   ├── ApiClient.java
│           │           │   ├── ExchangeRateService.java
│           │           │   └── RestCountriesService.java
│           │           │
│           │           ├── database/
│           │           │   └── DatabaseHelper.java
│           │           │
│           │           ├── fragments/
│           │           │   ├── AnalyticsFragment.java
│           │           │   ├── BudgetFragment.java
│           │           │   ├── CurrencyFragment.java
│           │           │   └── ExpenseFragment.java
│           │           │
│           │           ├── models/
│           │           │   ├── Budget.java
│           │           │   ├── CurrencyResult.java
│           │           │   └── Expense.java
│           │           │
│           │           └── utils/
│           │               └── CategoryUtils.java
│           │
│           └── res/
│               ├── color/
│               ├── drawable/
│               ├── layout/
│               ├── menu/
│               ├── mipmap-*/
│               └── values/
│
├── gradle/
│   └── wrapper/
│
├── .gitignore
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

---

## 🎨 UI / UX DESIGN

The application follows a clean mobile-first design with emphasis on clarity and fast navigation.

### Design Principles

- Persistent bottom navigation
- Clear financial information hierarchy
- Card-based content organization
- Recognizable financial categories
- Color-coded financial states
- Immediate visual feedback
- Readable monetary values
- Consistent layouts
- Minimal navigation depth
- Responsive Android interface

### Navigation

```text
┌─────────────────────────────────────────────────────────┐
│                                                         │
│                 APPLICATION CONTENT                     │
│                                                         │
│                                                         │
├──────────────┬──────────────┬──────────────┬─────────────┤
│      💸      │      💰      │      📊      │      💱     │
│   Expenses   │    Budget    │  Analytics   │  Currency   │
└──────────────┴──────────────┴──────────────┴─────────────┘
```

---

## ⚙️ SYSTEM REQUIREMENTS

| Requirement | Configuration |
|---|---|
| Android Studio | Recommended latest stable version |
| Android SDK | Compile SDK 34 |
| Minimum Android SDK | API 24 |
| Target Android SDK | API 34 |
| Java Compatibility | Java 8 source/target compatibility |
| Gradle Wrapper | Included |
| Internet | Required for live currency API functionality |

---

## ⚙️ INSTALLATION & SETUP

### 1. Clone the Repository

```bash
git clone https://github.com/Ahamed369/Student-Expense-Tracker.git
```

### 2. Enter the Project Directory

```bash
cd Student-Expense-Tracker
```

### 3. Open in Android Studio

Open **Android Studio** and select:

```text
File → Open → Student-Expense-Tracker
```

### 4. Allow Gradle to Sync

Android Studio should detect the Gradle configuration automatically.

Wait until dependency synchronization completes.

### 5. Select a Device

Use either:

```text
Android Emulator
        OR
Physical Android Device
```

### 6. Run the Application

Press:

```text
▶ Run 'app'
```

The application should build and launch on the selected Android device.

---

## 🧪 BUILD FROM TERMINAL

### Windows

```powershell
.\gradlew.bat assembleDebug
```

### Linux / macOS

```bash
./gradlew assembleDebug
```

---

## 🔐 SECURITY & REPOSITORY HYGIENE

The repository `.gitignore` excludes common local, generated, and sensitive Android development files, including:

```text
.gradle/
.idea/
build/
**/build/
local.properties
*.apk
*.aab
*.jks
*.keystore
.vscode/
*.log
```

This keeps machine-specific configuration, generated build output, signing files, and temporary development files outside version control.

---

## 📦 IMPORTANT DEPENDENCIES

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.fragment:fragment:1.6.2'
implementation 'androidx.core:core:1.12.0'

implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

implementation 'com.google.code.gson:gson:2.10.1'
```

---

## 🚀 FUNCTIONAL HIGHLIGHTS

<div align="center">

![Expenses](https://img.shields.io/badge/EXPENSES-Track_Transactions-00B8D4?style=for-the-badge)
![Budget](https://img.shields.io/badge/BUDGET-Control_Spending-00C853?style=for-the-badge)
![Analytics](https://img.shields.io/badge/ANALYTICS-Visual_Insights-AA00FF?style=for-the-badge)
![Currency](https://img.shields.io/badge/CURRENCY-Live_Conversion-FF6D00?style=for-the-badge)

</div>

---

## 🧭 USER JOURNEY

```text
START
  │
  ▼
Open Student Expense Tracker
  │
  ▼
Record Daily Expenses
  │
  ├───────────────────────────────┐
  │                               │
  ▼                               ▼
Set Category Budgets        Review Expense History
  │                               │
  └──────────────┬────────────────┘
                 │
                 ▼
          Analyze Spending
                 │
                 ▼
      Check Budget Progress
                 │
                 ▼
       Convert LKR Currency
                 │
                 ▼
        Make Better Decisions
```

---

## 🔮 POSSIBLE FUTURE ENHANCEMENTS

Potential future extensions could include:

- User authentication
- Cloud synchronization
- Export to PDF or CSV
- Recurring expense support
- Savings-goal management
- Income tracking
- Dark-mode customization
- Additional currencies
- Advanced financial reports
- Spending notifications
- Backup and restore
- Biometric application lock
- Home-screen widgets
- Improved accessibility
- Automated expense categorization

---

## 🎮 DEVELOPER ZONE

<div align="center">

### ⚡ CODE • BUILD • TEST • IMPROVE ⚡

```text
╔══════════════════════════════════════════════════════════════╗
║                                                              ║
║                 STUDENT EXPENSE TRACKER                      ║
║                                                              ║
║             [ JAVA ] [ ANDROID ] [ SQLITE ]                  ║
║          [ RETROFIT ] [ APIs ] [ ANALYTICS ]                 ║
║                                                              ║
║                   SYSTEM STATUS                              ║
║                                                              ║
║        EXPENSE ENGINE        ████████████████████             ║
║        BUDGET ENGINE         ████████████████████             ║
║        ANALYTICS ENGINE      ████████████████████             ║
║        CURRENCY ENGINE       ████████████████████             ║
║                                                              ║
║                    BUILD: READY                              ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

### 🕹️ MINI TERMINAL

```text
> boot student-expense-tracker

[████████████████████] 100%

> loading expense module............. OK
> loading budget module.............. OK
> loading analytics module........... OK
> loading currency module............ OK
> connecting financial tools......... OK

SYSTEM READY ✓
```

</div>

---

## 🐍 CONTRIBUTION SNAKE

<div align="center">

<img src="https://raw.githubusercontent.com/Ahamed369/Ahamed369/output/github-contribution-grid-snake-dark.svg" alt="GitHub contribution snake" />

</div>

> **Note:** The snake above requires the `github-contribution-grid-snake` workflow in the `Ahamed369/Ahamed369` profile repository. If that workflow is not enabled, remove this section to avoid a broken image.

---

## 📈 GITHUB ACTIVITY

<div align="center">

<img height="170" src="https://github-readme-stats.vercel.app/api?username=Ahamed369&show_icons=true&theme=tokyonight&hide_border=true&border_radius=12" alt="Ahamed GitHub statistics" />

<img height="170" src="https://github-readme-stats.vercel.app/api/top-langs/?username=Ahamed369&layout=compact&theme=tokyonight&hide_border=true&border_radius=12" alt="Ahamed top languages" />

<br/>

<img src="https://github-readme-streak-stats.herokuapp.com/?user=Ahamed369&theme=tokyonight&hide_border=true&border_radius=12" alt="Ahamed GitHub streak" />

</div>

---

## 👨‍💻 DEVELOPER

<div align="center">

### Mr.Ahamed

**Computer Science Undergraduate • Full-Stack & Mobile Application Developer • Entrepreneur**

<br/>

[![GitHub](https://img.shields.io/badge/GitHub-Ahamed369-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Ahamed369)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Mr.Ahamed-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/mr-ahamed-6146a5276)

<br/>

> Building practical digital products through software development, mobile technology, UI/UX thinking, and entrepreneurship.

</div>

---

## 🤝 CONTRIBUTING

Suggestions and improvements are welcome.

```bash
# Fork the repository

# Create a feature branch
git checkout -b feature/your-feature

# Stage changes
git add .

# Commit changes
git commit -m "Add your feature"

# Push the branch
git push origin feature/your-feature
```

Then open a Pull Request on GitHub.

---

## ⭐ SUPPORT THE PROJECT

If you find the project useful or interesting:

<div align="center">

### ⭐ STAR THE REPOSITORY

### 🍴 FORK IT

### 🧑‍💻 EXPLORE THE CODE

### 🚀 BUILD SOMETHING BETTER

<br/>

[![GitHub stars](https://img.shields.io/github/stars/Ahamed369/Student-Expense-Tracker?style=for-the-badge&logo=github&label=Stars)](https://github.com/Ahamed369/Student-Expense-Tracker/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/Ahamed369/Student-Expense-Tracker?style=for-the-badge&logo=github&label=Forks)](https://github.com/Ahamed369/Student-Expense-Tracker/forks)

</div>

---

## 📌 REPOSITORY INFORMATION

```text
Repository : Student-Expense-Tracker
Owner      : Ahamed369
Developer  : Mr.Ahamed
Platform   : Android
Language   : Java
UI         : XML + Material Components
Database   : SQLite
Networking : Retrofit + OkHttp
Parsing    : Gson
Charts     : MPAndroidChart
Version    : 1.0
Branch     : main
```

---

<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=22&duration=2600&pause=900&color=7C4DFF&center=true&vCenter=true&repeat=true&width=850&lines=Track+Smarter+%E2%80%A2+Spend+Smarter+%E2%80%A2+Build+Smarter;From+Daily+Expenses+to+Financial+Insights;Student+Expense+Tracker+%E2%80%A2+Built+with+Java+%26+Android" alt="Footer typing animation" />

<br/>

### 💸 STUDENT EXPENSE TRACKER

**Designed & Developed by Mr.Ahamed**

<br/>

![Made with Java](https://img.shields.io/badge/Made_with-Java-F89820?style=for-the-badge&logo=openjdk&logoColor=white)
![Built for Android](https://img.shields.io/badge/Built_for-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Built with Passion](https://img.shields.io/badge/Built_with-Passion-FF1744?style=for-the-badge&logo=heart&logoColor=white)

<br/>

**© 2026 Mr.Ahamed**

</div>
