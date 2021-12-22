package app.justokay.tvbase

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
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
import androidx.fragment.app.FragmentActivity
import app.justokay.tvbase.theme.MyTextComposeApplicationTheme
import kotlinx.coroutines.launch

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

//    private var state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                .replace(R.id.main_browse_fragment, MainFragment())
//                .commitNow()
//        }
        setContent {
            MyTextComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                HomeScreen()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return super.onKeyDown(keyCode, event)
    }
}

val data = listOf(
    "Feature",
    "Movie",
    "Index",
)

@Composable
fun HomeScreen() {
//    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        focusManager.moveFocus(FocusDirection.Next)
//    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Hero()

        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(data)
            HomeContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
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

@Composable
fun HomeContent(
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    Box(modifier = modifier.background(color = Color.LightGray)) {
        LazyColumn(state = listState) {
            itemsIndexed(contentData) { index, item ->
                SwimLine(item) {
                    coroutine.launch {
                        listState.animateScrollToItem(index, -100.dp.value.toInt())
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun SwimLine(section: Section, hasFocus: () -> Unit) {
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = section.title, color = Color.White)
        LazyRow(state = listState) {
            itemsIndexed(section.data) { index, item ->
                StandardCard(item) {
                    coroutine.launch {
                        listState.animateScrollToItem(index)
                    }
                    hasFocus()
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun StandardCard(name: String, receiveFocus: () -> Unit) {
    val focusRequester = FocusRequester()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    if (isFocused) {
        receiveFocus()
    }

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
            ),
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
    var focusState by remember {
        mutableStateOf(false)
    }

//    val requester = remember {
//        FocusRequester()
//    }

//    LaunchedEffect(Unit) {
//        requester.requestFocus()
//    }
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

@Preview(device = Devices.PIXEL_2, showBackground = true, showSystemUi = true)
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
