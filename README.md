# Bold Weather App

| Regular phone   | Foldable phone   |
|------------|------------|
| ![regular-ezgif com-resize](https://github.com/user-attachments/assets/dace633a-0e00-4400-8572-b9f2fe0a58b1)  | ![foldable-ezgif com-optimize](https://github.com/user-attachments/assets/e4fa2f08-82bc-491a-bdb4-fe710b7dce47)  |


## Overview

This Android application displays the current weather for a specified location. It uses the WeatherAPI to fetch weather data and showcases it using Jetpack Compose for the UI, Model-View-Intent (MVI) architecture for state management, Coil for image loading, and manual dependency injection for managing dependencies.

## Features

- Uses adaptive layout to properly work all screen sizes and foldables as well.
- Display current weather conditions for a given location.
- Displays current and next 2 forecast days.
- User-friendly interface with modern Jetpack Compose elements.
- Support for both spanish and english languages

## Getting Started
### Setup

1. Clone the repository from: https://github.com/Okabe94/BoldWeather.git
2. Switch to master branch
3. Get your API key from: https://www.weatherapi.com
4. Add your OpenWeather API key
    * Go to your local.properties and add your API key:
        
          WEATHER_API_KEY="YourWeatherAPIKey"

5. Build and Run the App

## Architecture

- **Jetpack Compose**: Used for building a responsive and modern UI.
- **MVI (Model-View-Intent)**: Used for managing the app’s state and handling user interactions.
- **Coil**: Used for efficient image loading and caching.
- **Manual Dependency Injection**: Implemented to manage dependencies and ensure modularity without increasing bundle size.

## Code Highlights
* **Jetpack Compose:** The app's UI is built using Compose, enabling a declarative way to create and manage the user interface.
* **MVI Architecture:**
    * Model: Handles data fetching from the Weather API.
    * View: Composed of UI components displaying the weather information.
    * Intent: Represents user actions and triggers state updates.
* **Coil:** Efficiently loads images, minimizing memory usage and improving performance.
* **Manual Dependency Injection:** Dependencies are managed manually to maintain flexibility and modularity, ensuring easier testing and maintenance.
