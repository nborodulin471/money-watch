# Money Watch

## Описание проекта
Money Watch - это система управления личными финансами, разработанная как учебный проект. Система предоставляет REST API для управления финансами, включая авторизацию, управление пользователями, банками, счетами и генерацию финансовых отчетов.

## Основные функции
- 🔐 Аутентификация и авторизация пользователей (JWT)
- 👥 Управление пользователями (регистрация, профиль)
-  Управление транзакциями (доходы/расходы)
- 🏦 Управление банками и счетами
- 📊 Генерация финансовых отчетов в PDF
- 📈 Визуализация данных с помощью графиков
- 🔍 Фильтрация и поиск транзакций
- 👮‍♂️ Административный интерфейс для управления системой

## Технологии
- **Backend**: Spring Boot 3
- **База данных**: PostgreSQL
- **Безопасность**: Spring Security, JWT
- **Документация**: Swagger UI
- **Генерация PDF**: iText, PDFBox
- **Графики**: JFreeChart
- **Сборка**: Gradle
- **Язык**: Java 21
- **Контейнеризация**: Docker

## Структура проекта

    money-watch/
    ├── src/
    │ ├── main/
    │ │ ├── java/ru/moneywatch/
    │ │ │ ├── config/ # Конфигурации приложения
    │ │ │ ├── controller/ # REST контроллеры
    │ │ │ ├── model/ # Модели данных
    │ │ │ │ ├── dtos/ # Data Transfer Objects
    │ │ │ │ ├── entities/ # JPA сущности
    │ │ │ │ ├── enums/ # Перечисления
    │ │ │ │ └── mappers/ # Мапперы DTO <-> Entity
    │ │ │ ├── repository/ # JPA репозитории
    │ │ │ ├── service/ # Бизнес-логика
    │ │ │ │ ├── auth/ # Сервисы аутентификации
    │ │ │ │ └── impl/ # Реализации сервисов
    │ │ │ └── util/ # Утилиты
    │ │ └── resources/
    │ │ └── application.properties # Конфигурация
    │ └── test/ # Тесты
    ├── doc/ # Документация и скриншоты
    ├── build.gradle # Конфигурация сборки
    └── README.md # Документация проекта

## Описание API

## Авторизация (`/api/auth`)

| Метод  | Конечная точка          | Описание                              |
|--------|-------------------------|---------------------------------------|
| POST   | `/api/auth/login`       | Аутентификация пользователя и получение JWT токена |
| POST   | `/api/auth/register`    | Регистрация нового пользователя       |

### Примеры запросов

#### Регистрация нового пользователя
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "user@example.com",
    "password": "password123",
    "role": "ROLE_USER",
    "personType": "INDIVIDUAL",
    "inn": "12345678"
}
```

#### Вход в систему
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "user@example.com",
    "password": "password123"
}
```
Ответ:
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

_Источник:_ `AuthController`

---

## Профиль пользователя (`/api/auth/profile`)

| Метод  | Конечная точка          | Описание                              | Роль           |
|--------|-------------------------|---------------------------------------|----------------|
| GET    | `/api/auth/profile/{id}`| Получить данные пользователя по ID    | USER, ADMIN    |
| POST   | `/api/auth/profile/{id}`| Редактировать данные пользователя     | USER, ADMIN    |
| DELETE | `/api/auth/profile`     | Удалить текущего пользователя         | USER, ADMIN    |

### Примеры запросов

#### Получение профиля пользователя
```http
GET /api/auth/profile/1
Authorization: Bearer {access_token}
```

#### Обновление профиля
```http
POST /api/auth/profile/1
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "username": "updated@example.com",
    "password": "newpassword123",
    "role": "ROLE_USER",
    "personType": "INDIVIDUAL",
    "inn": "12345678"
}
```

_Источник:_ `UserController`

---

## Транзакции (`/api/transaction`)  

| Метод  | Конечная точка          | Описание                              | Роль           |
|--------|-------------------------|---------------------------------------|----------------|
| GET    | `/api/transaction`      | Список всех транзакций пользователя   | USER, ADMIN    |
| POST   | `/api/transaction`      | Создание новой транзакции             | USER, ADMIN    |
| POST   | `/api/transaction/{id}` | Редактирование транзакции             | USER, ADMIN    |

### Примеры запросов

#### Получение списка транзакций
```http
GET /api/transaction
Authorization: Bearer {access_token}
```

#### Создание новой транзакции
```http
POST /api/transaction
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "date": "2024-03-20",
    "typeTransaction": "INCOME",
    "comment": "Зарплата",
    "sum": 100000.00,
    "category": "SALARY",
    "bankAccountId": 1,
    "userAccountId": 2
}
```

#### Редактирование транзакции
```http
POST /api/transaction/1
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "date": "2024-03-20",
    "typeTransaction": "INCOME",
    "comment": "Обновленная зарплата",
    "sum": 110000.00,
    "category": "SALARY",
    "bankAccountId": 1,
    "userAccountId": 2
}
```

_Источник:_ `TransactionController` 

---

## Счета (`/api/account`)

| Метод  | Конечная точка          | Описание                              | Роль           |
|--------|-------------------------|---------------------------------------|----------------|
| GET    | `/api/account`          | Список всех счетов аутентифицированного пользователя | USER, ADMIN    |
| POST   | `/api/account`          | Создать новый счет в указанном банке  | USER, ADMIN    |

### Примеры запросов

#### Создание нового счета
```http
POST /api/account
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "bankId": 1,
    "accountNumber": "40817810099910004312",
    "balance": 0.00
}
```

#### Получение списка счетов
```http
GET /api/account
Authorization: Bearer {access_token}
```

_Источник:_ `AccountController`

---

## Банки (`/api/bank`)

| Метод  | Конечная точка          | Описание                              | Роль           |
|--------|-------------------------|---------------------------------------|----------------|
| GET    | `/api/bank`             | Список всех банков текущего пользователя | USER, ADMIN    |
| POST   | `/api/bank`             | Создать новый банк                    | USER, ADMIN    |
| DELETE | `/api/bank/{id}`        | Удалить банк по ID                    | USER, ADMIN    |

### Примеры запросов

#### Создание нового банка
```http
POST /api/bank
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "name": "Сбербанк",
    "bic": "044525225"
}
```

#### Получение списка банков
```http
GET /api/bank
Authorization: Bearer {access_token}
```

_Источник:_ `BankController`

---

## Административные функции (`/api/admin`)  

> Только для администраторов

| Метод  | Конечная точка                    | Описание                                      |
|--------|-----------------------------------|-----------------------------------------------|
| GET    | `/api/admin`                      | Список всех транзакций в системе              |
| POST   | `/api/admin`                      | Создание нового административного аккаунта    |
| GET    | `/api/admin/filter`               | Получение отфильтрованных транзакций          |
| DELETE | `/api/admin/transaction`          | Удаление транзакции                           |
| POST   | `/api/admin/transaction/{id}`     | Изменение статуса транзакции                  |
| GET    | `/api/admin/users`                | Список всех пользователей                     |
| GET    | `/api/admin/users/{id}`           | Получение информации о пользователе           |
| POST   | `/api/admin/users/{id}`           | Обновление пользователя                       |
| DELETE | `/api/admin/users/{id}`           | Удаление пользователя                         |
| GET    | `/api/admin/{id}`                 | Получение транзакции по ID                    |
| DELETE | `/api/admin/{id}`                 | Удаление административного аккаунта           |

### Примеры запросов

#### Получение всех транзакций

```http
GET /api/admin
Authorization: Bearer {access_token}
```

#### Фильтрация транзакций

```http
GET /api/admin/filter?startDate=2024-01-01&endDate=2024-03-20&type=INCOME
Authorization: Bearer {access_token}
```

#### Удаление транзакции

```http
DELETE /api/admin/transaction?id=1
Authorization: Bearer {access_token}
```

#### Изменение статуса транзакции

```http
POST /api/admin/transaction/1
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "status": "COMPLETED"
}
```

#### Управление пользователями

```http
GET /api/admin/users
Authorization: Bearer {access_token}
```

```http
GET /api/admin/users/1
Authorization: Bearer {access_token}
Content-Type: application/json

{
    "username": "admin@example.com",
    "role": "ROLE_ADMIN",
    "personType": "INDIVIDUAL",
    "inn": "12345678"
}
```

```http
DELETE /api/admin/users/1
Authorization: Bearer {access_token}
```

_Источник:_ `AdminController`

---

## Отчеты (PDF) (`/api/reports`)

_Все возвращают PDF-документы, сгенерированные с помощью iText/PDFBox на уровне сервиса_

| Метод  | Конечная точка                              | Описание                                      |
|--------|---------------------------------------------|-----------------------------------------------|
| GET    | `/api/reports/transaction-stats`            | Отчет по статистике транзакций               |
| GET    | `/api/reports/transaction-stats-chart`      | Отчет по статистике с графиками              |
| GET    | `/api/reports/transaction-amount`           | Отчет по суммам транзакций                   |
| GET    | `/api/reports/transaction-amount-chart`     | Отчет по суммам с графиками                  |
| GET    | `/api/reports/transaction-sum`              | Отчет по типам транзакций                    |
| GET    | `/api/reports/transaction-sum-chart`        | Отчет по типам с графиками                   |
| GET    | `/api/reports/category-summary`             | Отчет по категориям                          |
| GET    | `/api/reports/category-summary-chart`       | Отчет по категориям с графиками              |
| GET    | `/api/reports/bank-stats`                   | Отчет по статистике банков                   |
| GET    | `/api/reports/bank-stats-chart`             | Отчет по статистике банков с графиками       |
| GET    | `/api/reports/transaction-dynamic`          | Отчет по динамике транзакций                 |

### Примеры запросов

#### Получение отчета по статистике транзакций
```http
GET /api/reports/transaction-stats
Authorization: Bearer {access_token}
Accept: application/pdf
```

#### Получение отчета по статистике с графиками
```http
GET /api/reports/transaction-stats-chart
Authorization: Bearer {access_token}
Accept: application/pdf
```

#### Получение отчета по динамике транзакций
```http
GET /api/reports/transaction-dynamic?periodType=MONTH&startDate=2024-01-01&endDate=2024-03-20
Authorization: Bearer {access_token}
Accept: application/pdf
```

_Источник:_ `ReportController`

---

## Начало работы
- **Клонируйте** репозиторий и перейдите в папку `money-watch`
- Убедитесь, что у вас установлены Java 21 и Docker (PostgreSQL)
- Запустите базу данных PostgreSQL с помощью Docker:
  ```bash
  docker run --name moneywatch -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=moneywatch -p 5432:5432 -d postgres:latest
  ```
- Приложение уже настроено с:
  - Порт: 8083
  - База данных: PostgreSQL (localhost:5432/moneywatch)
  - Секретный ключ JWT предварительно настроен
- Запустите с помощью `./mvnw spring-boot:run` или через вашу IDE
- Зарегистрируйте нового пользователя по адресу: `http://localhost:8083/api/auth/register`
- Войдите в систему по адресу: `http://localhost:8083/api/auth/login`
- Тестируйте конечные точки через Swagger UI: `http://localhost:8083/swagger-ui.html`

---

## 🖋 Команда
**Громов Иван** - Team Leader

**Никита Бородулин** - Java Developer

**Александр Расторгуев** - Java Developer

**Иветта Демидова** - Java Developer

**Березняк Владимир** - System Analyst

**Дмитрий Шерихов** - Frontend developer


GitHub: [https://github.com/nborodulin471/money-watch](https://github.com/nborodulin471/money-watch)  

---

## Права на использование
© 2024 Команда Дэдлайн))

Данный проект является учебным заданием и распространяется только в образовательных целях. Все права на исходный код и документацию принадлежат авторам.
