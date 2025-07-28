package com.talha.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.talha.shoppinglist.model.Item
import com.talha.shoppinglist.screens.AddItemScreen
import com.talha.shoppinglist.screens.DetailScreen
import com.talha.shoppinglist.screens.ItemListScreen
import com.talha.shoppinglist.ui.theme.ShoppingListTheme
import com.talha.shoppinglist.viewmodel.ItemViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "list_screen"
                        ) {
                            // Liste Ekranı
                            composable("list_screen") {
                                // Veri çekme işlemini LaunchedEffect içine aldık.
                                // Bu blok, ekran ilk açıldığında sadece bir kere çalışır.
                                LaunchedEffect(Unit) {
                                    viewModel.getItemList()
                                }

                                val itemList by remember {
                                    viewModel.itemList
                                }
                                ItemListScreen(items = itemList, navController = navController)
                            }

                            // Eşya Ekleme Ekranı
                            composable("add_item_screen") {
                                AddItemScreen(saveFunction = { item: Item ->
                                    viewModel.saveItem(item)
                                    navController.navigate("list_screen") {
                                        popUpTo("list_screen") { inclusive = true }
                                    }
                                })
                            }

                            // Detay Ekranı
                            composable(
                                "details_screen/{itemId}",
                                arguments = listOf(
                                    navArgument("itemId") {
                                        type = NavType.StringType
                                    }
                                )
                            ) { backStackEntry ->
                                val itemIdString = remember {
                                    backStackEntry.arguments?.getString("itemId")
                                }

                                // Detay verisini de LaunchedEffect ile çektik.
                                // Bu blok, itemId değiştikçe sadece bir kere çalışır.
                                LaunchedEffect(itemIdString) {
                                    viewModel.getItem(itemIdString?.toIntOrNull() ?: 0)
                                }

                                val selectedItem by remember {
                                    viewModel.selecteditem
                                }

                                DetailScreen(
                                    item = selectedItem,
                                    deleteFunction = {
                                        viewModel.deleteItem(selectedItem)
                                        navController.navigate("list_screen") {
                                            popUpTo("list_screen") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}