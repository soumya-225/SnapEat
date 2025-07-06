# SnapEat – Nutrition Tracking App

**SnapEat** is an Android application that helps users track their daily nutrition by simply taking a photo of their meals. It provides an easy way to monitor calorie intake and macronutrients like carbohydrates, fats, and proteins.

## 🌟 Key Features

- **Photo-Based Food Logging**: Just snap a picture of your meal—SnapEat identifies the food and logs nutritional values.
- **Manual Quantity Input**: Choose an approximate quantity like “2 cups” or “150 g” for accurate tracking.
- **Daily Summary**: See what you’ve consumed today with a breakdown of calories, carbs, proteins, and fats.
- **Smart Suggestions**: Get food recommendations based on nutrients you’ve consumed less of during the day.
- **Weekly Reports**: View your nutritional trends over the last 7 days with intuitive bar charts.
- **Water & Workout Tracking**: Track daily water intake and workout duration.
- **BMI Calculator**: Easily calculate and monitor your Body Mass Index.

## ⚙️ How It Works

1. User takes a photo of their meal.
2. A machine learning model identifies the food.
3. Nutritional values per unit are fetched from a food database.
4. User selects the estimated portion size.
5. The data is logged and visualized in the app.

## 🛠 Tech Stack

- **Platform**: Android (XML UI Toolkit)
- **Backend**: Flask (ML model deployment)
- **Cloud**: Firebase Authentication, Realtime Database, and Storage

---

