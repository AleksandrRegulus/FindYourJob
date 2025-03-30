package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.core.view.isVisible
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.ui.navigation.BottomBar
import ru.practicum.android.diploma.ui.navigation.NavGraph
import ru.practicum.android.diploma.ui.theme.ComposeTheme

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startXmlProject()

        setContent { // Jetpack Compose
            ComposeTheme {
                val navController = rememberNavController()

//    navController.addOnDestinationChangedListener { _, destination, _ ->
//        Log.d("MYLOG", "${destination.route}")
//
//    }

                Scaffold(
                    modifier = Modifier,
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            modifier = Modifier.height(dimensionResource(R.dimen._56dp)),
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavGraph(navHostController = navController)
                    }
                }
            }
        }
    }

    private fun startXmlProject() {
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment,
                R.id.favoriteFragment,
                R.id.aboutFragment -> {
                    binding.divider.isVisible = true
                    binding.bottomNavigationView.isVisible = true
                }

                else -> {
                    binding.divider.isVisible = false
                    binding.bottomNavigationView.isVisible = false
                }
            }
        }
    }

}
