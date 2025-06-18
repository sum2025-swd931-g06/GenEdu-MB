package com.example.mvvm.ui.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.MainViewModel
import com.example.mvvm.Screen
import com.example.mvvm.ui.components.menuitems.MenuItem
import com.example.mvvm.ui.components.menuitems.MenuItemRow
import com.example.mvvm.ui.theme.DarkPurple
import com.example.mvvm.ui.theme.LightPurple
import com.example.mvvm.ui.theme.MainColor
import com.example.mvvm.utils.navigateTo
import com.example.mvvm.utils.navigateToHome
import androidx.compose.material3.Divider as HorizontalDivider

@Composable
fun AccountScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit = {},
    onMenuItemClick: (MenuItem) -> Unit = {}
) {

    val userData = mainViewModel.userData.collectAsState().value

    // Placeholder for the Account screen content
    // This will be implemented later
    // You can use MenuItem data class to create menu items for the account screen
    val menuItem = listOf(
        MenuItem(
            icon = Icons.Default.Person,
            title = "Họ tên: ${userData?.name ?: "Chưa cập nhật"}",
            hasArrow = false
        ),
        MenuItem(
            icon = Icons.Default.Email,
            title = "Email: ${userData?.email ?: "Chưa cập nhật"}",
            hasArrow = false
        ),
        MenuItem(
            icon = Icons.Default.AccountBox,
            title = "Số CCCD/Căn Cước: ${userData?.idNumber ?: "Chưa cập nhật"}",
            hasArrow = true
        ),
        MenuItem(
            icon = Icons.Default.ShoppingCart,
            title = "Quản lý phương thức thanh toán",
            hasArrow = true
        ),
        MenuItem(
            icon = Icons.Default.Clear,
            title = "Xóa tài khoản",
            hasArrow = false,
            isDestructive = true
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding() // Built-in Compose padding
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LightPurple,
                        MainColor,
                        DarkPurple
                    )
                )
            )
    ) {
        // Here you can use the menuItem list to create your account screen UI
        // For example, you can use LazyColumn to display the menu items
        // and handle onClick events for each item
        Column(modifier = Modifier.fillMaxSize()) {
            //header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick)
                {
                    Icon(
                        modifier = Modifier.clickable {
                            navigateToHome(navController)
                        },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Thông tin tài khoản",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(48.dp))

            }

            // Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFE0E0E0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                //Name
                Text(
                    text = userData?.name ?: "Guest",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Menu Items
                    menuItem.forEach { item ->
                        MenuItemRow(
                            item = item,
                            onClick = { onMenuItemClick(item) }
                        )
                    }

                    // Divider
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0)
                    )

                    // Logout Button
                    MenuItemRow(
                        item = MenuItem(
                            icon = Icons.Default.ExitToApp,
                            title = "Đăng xuất",
                            hasArrow = false,
                            isDestructive = false
                        ),
                        onClick = { navigateTo(navController, Screen.Intro.route) }
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                }

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountInfoScreenPreview() {
    MaterialTheme {
        AccountScreen(
            navController = rememberNavController(),
            mainViewModel = MainViewModel()
        )
    }
}

