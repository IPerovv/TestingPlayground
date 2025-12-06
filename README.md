# Описание курсового проекта

[![On PR Android Workflow](https://github.com/IPerovv/TestingPlayground/actions/workflows/on-pr-into-main.yml/badge.svg)](https://github.com/IPerovv/TestingPlayground/actions/workflows/on-pr-into-main.yml)

## 1. Выбранная / Предлагаемая тема
**Автоматизация CI/CD процессов Android-приложений с использованием Gitlab CI: сборка, тестирование, анализ и уведомления**

---

## 2. Название проекта
**Android CI/CD Automation Workflow**

---

## 3. Краткое описание проекта
Проект представляет собой CI/CD pipeline для Android-приложения, реализованный на Gitlab CI.  
Основная цель — обеспечить полную автоматизацию процесса проверки Pull Request’ов в ветку `main`, включая:

1. Сборку проекта и выгрузку артефактов.
2. Проверку качества кода (Lint).
3. Запуск юнит-тестов и инструментальных тестов.
4. Генерацию `debug.apk` при успешном прохождении всех проверок.
5. Формирование итогового отчёта.
6. Отправку уведомления в Telegram о результатах CI-процесса.

---

## 4. Технологический стек

### Приложение

- **Язык:** Kotlin
- **Фреймворк:** Android SDK
- **Сборка:** Gradle, AGP
- **Конфигурация:** build.gradle, gradle.properties

### DevOps инструменты

- **CI/CD:** GitLab CI
- **Контейнеризация и окружение:**  GitHub-hosted runners
- **Тестирование:**
    - **Юнит-тесты:** JUnit
    - **Инструментальные тесты:** Android Instrumentation (Espresso)
    - **Статический анализ:** Android Lint
- **Кэширование и артефакты:**
    - Gradle Cache (`actions/cache@v3`)
    - Build Artifacts (`actions/upload-artifact`, `actions/download-artifact`)
- **Мониторинг и уведомления:**
    - Telegram Bot API (уведомления о результатах сборки)

---

## 5. Структура проекта (Архитектура)

### Основные workflow:
1. **setup-android.yml** Инициализация окружения для всех этапов CI.
2. **build.yml** Сборка проекта (`./gradlew assembleDebug`) и выгрузка артефактов сборки (`actions/upload-artifact`).
3. **run-lint.yml** Проверка качества кода (`./gradlew lint`) с использованием ранее собранных артефактов.
4. **run-unit-tests.yml** Запуск модульных тестов (`./gradlew testDebugUnitTest`).
5. **run-instrumentation-tests.yml** Запуск инструментальных тестов (через `reactiveCIrcus/android-emulator-runner@v2`) с использованием загруженных артефактов.
6. **generate-apk.yml** Генерация `debug.apk` при успешном прохождении всех проверок.
7. **form-message.yml** Формирование итогового отчёта о выполнении пайплайна после успешной сборки APK
8. **notify-telegram.yml** Отправка уведомлений с результатами CI/CD в Telegram.

### Логическая схема CI-процесса

```
Pull Request → main
        │
        ▼
   setup-android.yml
 (установка окружения и кэширование Gradle)
        │
        ▼
     build.yml
 (сборка и выгрузка артефактов)
        │
        ├────────────┬─────────────┬─────────────┐
        ▼            ▼             ▼             ▼
 run-lint.yml   run-unit-tests.yml run-instrumentation-tests.yml
 (подгружают артефакты и выполняют проверки)
        └────────────┴─────────────┴─────────────┘
                    │
                    ▼
           generate-apk.yml
      (создание debug.apk при успешных проверках)
                    │
                    ▼
           form-message.yml
      (агрегация статусов и формирование отчёта)
                    │
                    ▼
          notify-telegram.yml
     (уведомление о результатах в Telegram)

```

---

## 6. План реализации и применение DevOps-практик

### Этап 1: Настройка инфраструктуры и окружения

**Поэтапный план:**
1. Создание репозитория и структуры Android-проекта.
2. Разработка общего Action `setup-android` для установки Java, SDK и кэширования Gradle.
3. Настройка базового workflow `build.yml` для сборки проекта.
4. Конфигурация Gradle (`gradle.properties`, `build.gradle`) для оптимальной сборки.
5. Проверка воспроизводимости сборки и выгрузки артефактов.

**Применение DevOps:**
- **Infrastructure as Code:** описание CI-конвейера в YAML.
- **Configuration as Code:** все параметры окружения зафиксированы в репозитории.
- **Caching:** использование `actions/cache` для ускорения сборок.
- **Version Control:** Git для версионирования кода и конфигураций.

---

### Этап 2: Внедрение Continuous Integration

**Поэтапный план:**
1. Настройка автоматического запуска CI при Pull Request в `main`.
2. Добавление этапов линтинга (`run-lint.yml`), юнит-тестов (`run-unit-tests.yml`) и инструментальных тестов (`run-instrumentation-tests.yml`).
3. Реализация логики подгрузки артефактов сборки на всех этапах.
4. Формирование общих отчётов (JUnit, Lint,).
5. Добавление статуса выполнения для каждой job (build, lint, tests).

**Применение DevOps:**
- **Continuous Integration:** автоматическая сборка и тестирование на каждый PR.
- **Quality Gates:** обязательное прохождение тестов и линтинга перед merge.
- **Test Automation:** многослойное тестирование (юнит, инструментальные тесты).
- **Artifact Management:** передача результатов сборки между job’ами.

---

### Этап 3: Автоматическая сборка и уведомления (Continuous Delivery & Feedback)

**Поэтапный план:**

1. Добавление условия генерации `debug.apk` при успешных тестах.
2. Настройка workflow `form-message.yml` для агрегации результатов всех проверок.
3. Реализация отправки результатов в Telegram через `notify-telegram.yml`.
4. Проверка форматирования сообщений и корректности передачи статусов.

**Применение DevOps:**
- **Conditional Execution:** выполнение шагов в зависимости от статусов job.
- **Continuous Delivery:** автоматическая сборка `debug.apk` при успешных проверках.
- **Continuous Feedback:** мгновенные уведомления разработчикам о результатах.
- **Transparency:** централизованное отображение статусов всех проверок.

---

### Этап 4: Мониторинг, оптимизация и совершенствование пайплайна

**Поэтапный план:**
1. Анализ времени выполнения отдельных job и оптимизация кэшей.
2. Добавление бейджей статуса сборки в `README.md`.

**Применение DevOps:**
- **Observability:** мониторинг пайплайна и метрик выполнения.
- **Metrics-Driven Development:** анализ успешности и скорости CI.
- **Continuous Improvement:** постоянное улучшение процессов CI/CD.  

---
Ivan Perov, SPBSTU, 5140904/50102, 2025