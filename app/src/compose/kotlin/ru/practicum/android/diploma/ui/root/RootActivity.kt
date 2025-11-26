package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.navigation.BottomBar
import ru.practicum.android.diploma.ui.navigation.NavGraph
import ru.practicum.android.diploma.ui.theme.ComposeTheme

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTheme {
                val navController = rememberNavController()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                Scaffold(
                    modifier = Modifier,
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            modifier = Modifier.height(dimensionResource(R.dimen._56dp)),
                            bottomBarState = bottomBarState
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavGraph(
                            navHostController = navController,
                            bottomBarState = bottomBarState
                        )
                    }
                }
            }
        }
    }

}
