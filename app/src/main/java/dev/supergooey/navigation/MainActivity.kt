package dev.supergooey.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.supergooey.navigation.Destination.One
import dev.supergooey.navigation.Destination.Three
import dev.supergooey.navigation.Destination.Two
import dev.supergooey.navigation.ui.theme.NavigationTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      App()
    }
  }
}

data class AppState(
  val navBarItems: List<Item>
) {
  val selectedNavBarItem = navBarItems.first { it.selected }
}

data class Item(
  val label: String,
  val icon: ImageVector,
  val route: Destination,
  val selected: Boolean,
)

sealed interface Destination {
  @Serializable
  data object One : Destination
  @Serializable
  data object Two : Destination
  @Serializable
  data object Three : Destination
}


@Composable
fun App() {
  var state by remember {
    mutableStateOf(
      AppState(
        navBarItems = listOf(
          Item(
            label = "One",
            icon = Icons.Rounded.Home,
            route = One,
            selected = true
          ),
          Item(
            label = "Two",
            icon = Icons.Rounded.Person,
            route = Two,
            selected = false
          ),
          Item(
            label = "Three",
            icon = Icons.Rounded.DateRange,
            route = Three,
            selected = false
          ),
        )
      )
    )
  }

  NavigationTheme {
    val navController = rememberNavController()
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      contentWindowInsets = WindowInsets.safeContent,
      bottomBar = {
        NavigationBar {
          state.navBarItems.forEach { item ->
            NavigationBarItem(
              selected = item.selected,
              label = { Text(item.label) },
              onClick = {
                navController.navigate(item.route)
                state = state.copy(
                  navBarItems = state.navBarItems.map { oldItem ->
                    if (oldItem == item) {
                      oldItem.copy(selected = true)
                    } else {
                      oldItem.copy(selected = false)
                    }
                  }
                )
              },
              icon = {
                Icon(
                  imageVector = item.icon,
                  contentDescription = item.label
                )
              }
            )
          }
        }
      }
    ) { innerPadding ->
      NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        enterTransition = { slideIn(initialOffset = { IntOffset(x = it.width, y = 0) }, animationSpec = spring()) },
        exitTransition = { scaleOut(animationSpec = spring()) },
        startDestination = state.selectedNavBarItem.route
      ) {
        composable<One> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("One")
          }
        }
        composable<Two> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Two")
          }
        }
        composable<Three> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Three")
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun AppPreview() {
  App()
}
