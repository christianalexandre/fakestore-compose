# Fake Store

This project is a simple e-commerce application built to demonstrate modern Android development practices. It utilizes the [DummyJSON](https://dummyjson.com/) API to create a fake store, allowing users to browse products, add them to a cart, and simulate a checkout process.

The main purpose of this project is to serve as a learning resource in:

*   **Jetpack Compose:** Building a modern, declarative UI.
*   **Clean Architecture:** Structuring the application for better separation of concerns, testability, and maintainability.
*   **Android Testing:** Implementing unit and UI tests to ensure code quality.

This project is also part of a post-graduation course in Mobile Application Development.

## Features

*   Browse a list of products from the DummyJSON API.
*   View product details.
*   Add and remove products from the shopping cart.
    * Shopping cart data is persisted in Firebase Firestore.
* User authentication (login/registration) using Firebase Authentication.
* Push notifications with deep linking to product detail pages.
*   (Future) Simulate a checkout process.

Note: Firebase features require a googleservices.json file in the /app/ folder. Please generate your own or contact me for the file.

## Architecture

This project follows the principles of **Clean Architecture**, separating the code into three main layers:

*   **Data Layer:** Responsible for fetching data from the network (using Retrofit) and providing it to the domain layer.
*   **Domain Layer:** Contains the core business logic of the application, including use cases and domain models.
*   **Presentation Layer:** The UI layer, built with Jetpack Compose, responsible for displaying data and handling user interactions.

## How to Run

### Prerequisites

*   Android Studio Iguana | 2023.2.1 or later
*   JDK 17 or later

### Step-by-Step

1.  Clone the repository:
    ```bash
    git clone https://github.com/christianalexandre/fakestore-compose.git
    ```
2.  Open the project in Android Studio.
3.  Let Android Studio download the required Gradle dependencies.
4.  Put Google Services file in /app/ folder. 
5.  Run the `app` module on an emulator or a physical device.

## Tools / Dependencies

*   [Kotlin](https://kotlinlang.org/)
*   [Jetpack Compose](https://developer.android.com/jetpack/compose) for the UI
*   [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous programming
*   [Hilt](https://dagger.dev/hilt/) for dependency injection
*   [Retrofit](https://square.github.io/retrofit/) for networking
*   [Firebase](https://console.firebase.google.com/) for Authentication, DataStore and Push notification.
