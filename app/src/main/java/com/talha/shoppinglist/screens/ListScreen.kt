package com.talha.shoppinglist.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.talha.shoppinglist.model.Item
@Composable
fun ItemListScreen(items: List<Item>, navController: NavController) {
    Scaffold(
        topBar = { },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FAB {

                navController.navigate("add_item_screen")
            }
        },
        content = { padding ->
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                items(items) { item ->
                    ItemRow(item = item, navController = navController)
                }
            }
        }
    )
}

@Composable
fun ItemRow(item: Item, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                navController.navigate("details_screen/${item.id}")
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Elemanları iki uca yaslar
        ) {
       
            Text(
                text = item.itemName,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            item.price?.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = "$it TL",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
}

@Composable
fun FAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(Icons.Filled.Add, "Yeni Eşya Ekle", tint = Color.White)
    }
}