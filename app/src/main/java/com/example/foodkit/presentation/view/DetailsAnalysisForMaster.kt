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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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

    val tabs = listOf("Details", "Edit")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Main Details Analysis") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
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
                    .background(Color.LightGray)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedBorderCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        borderWidth = 2.dp,
                        gradient = Brush.linearGradient(listOf(Color.Black, Color.White)),
                        animationDuration = 10000,
                        onCardClick = {}
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailsRow("Name", food.name)
                            Divider(
                                color = Color.Black
                            )
                            DetailsRow("Category", food.category)
                            Divider(
                                color = Color.Black
                            )
                            DetailsRow("Price", "\$${food.price}")
                            Divider(
                                color = Color.Black
                            )
                            DetailsRow("Available Quantity", food.availableQuantity.toString())
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                text = "Dual Dynamics",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            SalesProgressBar(food.totalSales, food.availableQuantity)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LegendItem(color = Color(0xFF2E7D32), label = "totalSales")
                            }
                        }
                    }

                    AnimatedBorderCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        borderWidth = 1.dp,
                        gradient = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFf76700),
                                Color(0xFF5965b1)
                            )
                        ),
                        animationDuration = 10000,
                        onCardClick = {}
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nutrition Breakdown",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            val piechartData = PieChartData(
                                listOf(
                                    PieChartData.Slice(
                                        value = food.calories.toFloat(),
                                        color = Color(0xFF0D47A1) // Blue shade
                                    ),
                                    PieChartData.Slice(
                                        value = food.protein.toFloat(),
                                        color = Color(0xFF2E7D32) // Green shade
                                    ),
                                    PieChartData.Slice(
                                        value = food.fats.toFloat(),
                                        color = Color(0xFFD32F2F) // Red shade
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
                                LegendItem(color = Color(0xFF0D47A1), label = "Calories")
                                LegendItem(color = Color(0xFF2E7D32), label = "Protein")
                                LegendItem(color = Color(0xFFD32F2F), label = "Fats")
                            }
                        }
                    }


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                DetailsRow("Rating", averageRating.toString())
                                Divider(
                                    color = Color.Black
                                )
                                DetailsRow("Reviews", "${food.ratingCount}")
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // استخدام لون خلفية أنظف
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Revenue",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Last Week: \$${
                                    String.format(
                                        "%.2f",
                                        food.lastWeekRevenue
                                    )
                                }",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Total Revenue: \$${food.totalRevenue}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Divider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Revenue Icon",
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.CenterHorizontally),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

