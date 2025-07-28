package com.talha.shoppinglist.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.talha.shoppinglist.R
import com.talha.shoppinglist.model.Item

@Composable
fun DetailScreen(
    item: Item?,
    deleteFunction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp), // Dışarıdan boşluk vererek daha iyi görünmesini sağladım.
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // İçerikleri ortalamak için.
    ) {

        // Item Adı
        Text(
            text = item?.itemName ?: "Eşya Adı Yok",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Item Resmi
        val imageBitmap = item?.image?.let { byteArray ->
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
        }

        Image(
            bitmap = imageBitmap ?: ImageBitmap.imageResource(id = R.drawable.selectimage),
            contentDescription = item?.itemName ?: "Resim Seç",
            modifier = Modifier.size(300.dp, 200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Mağaza: ${item?.storeName ?: "Belirtilmemiş"}",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fiyat: ${item?.price ?: "0"} TL",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            deleteFunction()
        }) {
            Text(text = "Delete")
        }
    }
}