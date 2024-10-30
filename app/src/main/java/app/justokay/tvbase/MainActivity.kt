package app.justokay.tvbase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.justokay.tvbase.theme.AppTheme
import kotlinx.serialization.Serializable

sealed class Navigation {
    @Serializable
    object Home : Navigation()

    @Serializable
    data class Detail(val id: String) : Navigation()
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Navigation.Home
                ) {
                    composable<Navigation.Home> {
                        HomeScreen(onItemClick = {
                            navController.navigate(Navigation.Detail(it))
                        })
                    }
                    composable<Navigation.Detail> {
                        val id = it.toRoute<Navigation.Detail>().id

                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "Detail $id",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }

}

val data = listOf(
    "Feature",
    "Movie",
    "Index",
)

@Composable
fun HomeScreen(
    onItemClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Hero()

        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(data)
            HomeContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onItemClick = onItemClick
            )
        }
    }
}

data class Section(
    val title: String,
    val data: List<String>
)

val contentData = mutableListOf<Section>().apply {
    List(20) {
        add(
            Section(
                title = "Title $it",
                data = List(50) {
                    it.toString()
                }
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {}
) {
    val listState = rememberLazyListState()

    Box(
        modifier = modifier
            .background(color = Color.LightGray)
            .focusGroup()
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(contentData) { index, item ->
                SwimLine(item, onItemClick)
            }
        }
    }
}

@Composable
fun SwimLine(
    section: Section,
    onItemClick: (String) -> Unit = {}
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = section.title, color = Color.White)
        LazyRow(state = listState, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(section.data) { index, item ->
                StandardCard(item, modifier = Modifier.clickable {
                    onItemClick(item)
                })
            }
        }
    }
}

@Composable
fun StandardCard(
    name: String,
    modifier: Modifier = Modifier
) {
    val focusRequester = FocusRequester()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .heightIn(100.dp)
            .aspectRatio(1.77f)
            .onFocusChanged {
                Log.e(
                    name,
                    "isFocused ${it.isFocused}, hasFocus ${it.hasFocus}, isCaptured ${it.isCaptured}"
                )
            }
            .border(
                color = if (isFocused) Color.Red else Color.White,
                width = 2.dp
            )
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name, color = Color.White)
    }
}

@Composable
fun TopBar(data: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.Black.copy(alpha = 0.8f))
            .padding(horizontal = 60.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.forEachIndexed { index, name ->
                NavigationItem(name)
                if (index != data.lastIndex) {
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
fun NavigationItem(name: String) {
    val focusRequester = FocusRequester()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    Text(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource),
        text = name,
        style = TextStyle(
            color = if (isFocused) Color.Red else Color.White,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp
        )
    )
}

@Composable
fun Hero() {

}

@Preview(device = Devices.TV_1080p, showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    Box {
        Column(
            modifier = Modifier.background(Color.Red)
        ) {
            Text(text = "1")
            Text(text = "2")
        }
    }
}
