package com.example.mvvm.utils

import com.example.mvvm.Screen

fun navigateToHome(navController: androidx.navigation.NavController) {
    navController.navigate(Screen.Home.route)
}

fun navigateTo(navController: androidx.navigation.NavController, destination: String) {
    navController.navigate(destination)
//    {
//        popUpTo(navController.graph.findStartDestination().id) {
//            saveState = true
//        }
//        launchSingleTop = true
//        restoreState = true
//    }
}



