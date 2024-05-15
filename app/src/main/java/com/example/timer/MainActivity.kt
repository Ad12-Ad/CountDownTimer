package com.example.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import com.example.timer.ui.theme.TimerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    TimerScreen(isAdmin = true) // For Admin
}

@Composable
fun TimerScreen(isAdmin: Boolean) {
    val custom_fontFamily = FontFamily(
        Font(R.font.nunito_bold, FontWeight.Bold),
        Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
        Font(R.font.nunito_extralight, FontWeight.ExtraLight),
        Font(R.font.nunito_light, FontWeight.Light),
        Font(R.font.nunito_medium, FontWeight.Medium),
        Font(R.font.nunito_regular, FontWeight.Normal),
        Font(R.font.nunito_semibold , FontWeight.SemiBold)
    )


    val totalTimeInMillis = 5 * 60 * 1000L
    var timeLeftInMillis by remember { mutableStateOf(totalTimeInMillis) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf("Start") }

    val coroutineScope = rememberCoroutineScope()
    val timer = object : CoroutineScope by coroutineScope {
        private var job: Job? = null

        fun start() {
            job = launch {
                while (isTimerRunning && timeLeftInMillis > 0) {
                    delay(1000L)
                    timeLeftInMillis -= 1000L
                }
                if (timeLeftInMillis == 0L) {
                    isTimerRunning = false
                    buttonText = "Start"
                    timeLeftInMillis = totalTimeInMillis
                }
            }
        }

        fun stop() {
            job?.cancel()
            isTimerRunning = false
            buttonText = "Start"
            timeLeftInMillis = totalTimeInMillis
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .height(300.dp)
                .shadow(10.dp, RoundedCornerShape(30.dp), true)
                .background(Color(0XFFEFEEEA)),
            shape = RoundedCornerShape(30.dp)
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = formatTime(timeLeftInMillis),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = custom_fontFamily,
                        fontSize = 48.sp
                    )
                )

                if (isAdmin) {
                    Button(
                        onClick = {
                            when (buttonText) {
                                "Start" -> {
                                    isTimerRunning = true
                                    buttonText = "Pause"
                                    timer.start()
                                }
                                "Pause" -> {
                                    isTimerRunning = false
                                    buttonText = "Resume"
                                }
                                "Resume" -> {
                                    isTimerRunning = true
                                    buttonText = "Pause"
                                    timer.start()
                                }
                                "Stop" -> {
                                    timer.stop()
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .shadow(50.dp, RoundedCornerShape(15.dp)),
                        shape =  RoundedCornerShape(30.dp)
                    ) {
                        Text(text = buttonText, style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = custom_fontFamily,
                            fontSize = 25.sp
                        ))
                    }
                }
            }

        }

    }


}

private fun formatTime(timeLeftInMillis: Long): String {
    val minutes = (timeLeftInMillis / (1000 * 60)) % 60
    val seconds = (timeLeftInMillis / 1000) % 60
    return "%02d:%02d".format(minutes, seconds)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Display1(){
    App()
}