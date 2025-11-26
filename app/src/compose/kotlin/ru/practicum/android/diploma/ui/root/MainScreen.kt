package ru.practicum.android.diploma.ui.root

//@SuppressLint("RestrictedApi")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen() {
//    val navController = rememberNavController()
//
////    navController.addOnDestinationChangedListener { _, destination, _ ->
////        Log.d("MYLOG", "${destination.route}")
////
////    }
//
//    Scaffold(
//        modifier = Modifier,
//        bottomBar = {
//            Log.d("MYLOG", "${navController.currentDestination?.route}")
//
//            BottomBar(
//                navController = navController,
//                modifier = Modifier.height(dimensionResource(R.dimen._56dp)),
//            )
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            NavGraph(navHostController = navController)
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBar(navController: NavHostController) {
//
//    when (navController.currentDestination?.route) {
//        BottomBarScreen.ScreenSearch.route -> {
//            TopAppBar(
//                title = {
//                    Text(text = stringResource(R.string.search_vacancy))
//                }
//            )
//        }
//
//        BottomBarScreen.ScreenFavorite.route -> {
//            TopAppBar(
//                title = {
//                    Text(text = stringResource(R.string.team))
//                }
//            )
//        }
//
//        BottomBarScreen.ScreenAbout.route -> {
//            TopAppBar(
//                title = {
//                    Text(text = stringResource(R.string.team))
//                }
//            )
//        }
//
//        else -> {
//            TopAppBar(
//                title = {
//                    Text(text = "НИРАБОТАИТЬ")
//                }
//            )
//        }
//    }
//
//}
