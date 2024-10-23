package com.example.foodkit.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.foodkit.R
import com.example.foodkit.components.detailsAnalysis.AnimatedBorderCard
import com.example.foodkit.components.detailsAnalysis.DetailsRow
import com.example.foodkit.components.detailsAnalysis.LegendItem
import com.example.foodkit.components.detailsAnalysis.SalesProgressBar
import com.example.foodkit.presentation.view.userBottomNavigation.EditScreen
import com.example.foodkit.presentation.viewModel.CartForTestViewModel
import com.example.foodkit.presentation.viewModel.FoodDetailViewModel
import com.example.foodkit.presentation.viewModel.MasterViewModel
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainDetailsAnalysis(
    navController: NavController,
    foodId: String,
    userId: String,
) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(stringResource(id = R.string.details), stringResource(id = R.string.edit))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.main_details_analysis)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                TabRow(
                    containerColor = Color.White,
                    contentColor = Color.DarkGray,
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = colorResource(id = R.color.appColor),
                            height = 4.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) },
                            selectedContentColor = colorResource(id = R.color.appColor),
                            unselectedContentColor = Color.DarkGray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                when (selectedTab) {
                    0 -> DetailsAnalysisForMaster(foodId, userId)
                    1 -> EditScreen(navController, foodId, userId)
                }
            }
        }
    )
}


@SuppressLint("DefaultLocale")
@Composable
fun DetailsAnalysisForMaster(
    foodId: String,
    userId: String,
) {
    val foodDetailViewModel: FoodDetailViewModel =
        koinViewModel(parameters = { parametersOf(userId) })
    val cartViewModel: CartForTestViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val masterViewModel: MasterViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        foodDetailViewModel.loadFood(foodId)
        foodDetailViewModel.loadUserRating(foodId, userId)
    }

    foodDetailViewModel.food?.let { food ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(food.imageUrl),
                        contentDescription = "Food Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 220.dp)
                        .background(Color.White),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedBorderCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        borderWidth = 2.dp,
                        gradient = Brush.linearGradient(listOf(Color.Black, Color.White)),
                        animationDuration = 10000,
                        onCardClick = {}
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(Color.Transparent)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DetailsRow(stringResource(id = R.string.name), food.name)
                                Divider(color = Color.DarkGray)
                                DetailsRow(stringResource(id = R.string.category), food.category)
                                Divider(color = Color.DarkGray)
                                DetailsRow(stringResource(id = R.string.price), "\$${food.price}")
                                Divider(color = Color.DarkGray)
                                DetailsRow(stringResource(id = R.string.available_quantity), food.availableQuantity.toString())
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.dual_dynamics),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            SalesProgressBar(food.totalSales, food.availableQuantity)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LegendItem(
                                    color = colorResource(id = R.color.appColor),
                                    label = stringResource(id = R.string.total_sales)
                                )
                            }
                        }
                    }

                    AnimatedBorderCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        borderWidth = 2.dp,
                        gradient = Brush.horizontalGradient(
                            listOf(Color(0xFF7fd8be), Color(0xFF5965b1))
                        ),
                        animationDuration = 100000,
                        onCardClick = {}
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth(),
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(id = R.string.nutrition_breakdown),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )

                                val piechartData = PieChartData(
                                    listOf(
                                        PieChartData.Slice(
                                            value = food.calories.toFloat(),
                                            color = Color(0xFF7fd8be)
                                        ),
                                        PieChartData.Slice(
                                            value = food.protein.toFloat(),
                                            color = Color(0xFF994c30)
                                        ),
                                        PieChartData.Slice(
                                            value = food.fats.toFloat(),
                                            color = Color(0xFFff7f50)
                                        )
                                    )
                                )
                                PieChart(
                                    pieChartData = piechartData,
                                    modifier = Modifier.size(200.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    LegendItem(color = Color(0xFF7fd8be), label = stringResource(id = R.string.calories))
                                    LegendItem(color = Color(0xFF994c30), label = stringResource(id = R.string.protein))
                                    LegendItem(color = Color(0xFFff7f50), label = stringResource(id = R.string.fats))
                                }
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {

                            val averageRating =
                                if (food.ratingCount > 0) food.rating / food.ratingCount else 0f

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DetailsRow(stringResource(id = R.string.rating), averageRating.toString())
                                Divider(color = Color.Black)
                                DetailsRow(stringResource(id = R.string.reviews), "${food.ratingCount}")
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFb5b4b0)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.revenue),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Last Week: \$ ${String.format("%.2f", food.lastWeekRevenue)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color.DarkGray
                            )
                            Text(
                                text = "Total Revenue: \$${food.totalRevenue}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color.DarkGray
                            )

                            Divider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = Color.Black
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Revenue Icon",
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.CenterHorizontally),
                                tint = Color.DarkGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
