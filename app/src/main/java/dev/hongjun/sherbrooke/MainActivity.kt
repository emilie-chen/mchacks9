package dev.hongjun.sherbrooke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import dev.hongjun.sherbrooke.ui.theme.ProjetSherbrookeTheme

import dev.hongjun.sherbrooke.QRCodeConverter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetSherbrookeTheme {
                MainPage()
            }
        }
    }
}

@Composable
fun MainPage() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.MyProfile,
        Screen.Scan,
        Screen.History,
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.MyProfile.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.MyProfile.route) {
                MyProfile(navController)
            }
            composable(Screen.Scan.route) {
                Scan(navController)
            }
            composable(Screen.History.route) {
                History(navController)
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object MyProfile : Screen("my_profile", R.string.my_profile)
    object Scan : Screen("scan", R.string.scan)
    object History : Screen("history", R.string.history)
}

@Composable
fun MyProfile(navController: NavController) {
    Text(text = "My Profile")
    val bitmap = QRCodeConverter.generateQRCodeFromString("Sample Text")
    Image(painter = rememberImagePainter(bitmap) , contentDescription = null)
}

@Composable
fun Scan(navController: NavController) {
    Text(text = "Scan")
}

@Composable
fun History(navController: NavController) {
    Text(text = "History")
}

