package com.sks225.snapeat.utilities

fun bmiCategory(bmi:Float): String{
    if (bmi<16){
        return "Severe Thinness"
    }
    else if (bmi<16.9 && bmi>16){
        return "Moderate Thinness"
    }
    else if (bmi<18.4 && bmi>17){
        return "Mild Thinness"
    }
    else if (bmi<24.9 && bmi>18.5){
        return "Normal"
    }
    else if (bmi<29.9 && bmi>25){
        return "Overweight"
    }
    else if (bmi<34.9 && bmi>30){
        return "Obese Class-I"
    }
    else {
        return "Obese Class-II"
    }
}