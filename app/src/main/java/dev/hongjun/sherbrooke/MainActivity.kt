package dev.hongjun.sherbrooke

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dev.hongjun.sherbrooke.ui.theme.ProjetSherbrookeTheme
import kotlinx.serialization.Serializable


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

    val bitmap = QRCodeConverter.generateQRCodeFromString(
        toJson<UserInfo>(
            UserInfo(
                name = Name("First", "Last"),
                email = Email("first.last@example.com"),
                phoneNumber = PhoneNumber("123-456-7890"),
                socialNetworks = SocialNetworks(
                    discordTag = DiscordTag("tomato#0001"),
                    instagramUsername = "tomato",
                ),
                notes = "Notes"
            )
        )
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "My Profile", modifier = Modifier.padding(16.dp))
        Image(
            painter = rememberImagePainter(bitmap), contentDescription = null, modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .height(300.dp)
                .fillMaxSize()
                .padding(16.dp)
        )

    }

}

@Composable
fun Scan(navController: NavController) {


    val context = LocalContext.current
    val barcodeLauncher: ActivityResultLauncher<ScanOptions> = rememberLauncherForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            // deserialize text into UserInfo struct
            try {
                val contents = fromJson<UserInfo>(result.contents)
                println(contents)
                Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(context, "Invalid QR Code", Toast.LENGTH_LONG).show()
            }

        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Scan")
        Button(onClick = { barcodeLauncher.launch(ScanOptions()) }) {
            Text("Scanner")
        }
    }


}

// TODO: write new composable function to display user info and save to history
@Composable
fun History(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "History")
    }

}

