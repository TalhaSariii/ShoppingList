package com.talha.shoppinglist.screens



import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.talha.shoppinglist.R
import com.talha.shoppinglist.model.Item
import java.io.ByteArrayOutputStream

@Composable
fun AddItemScreen(saveFunction: (item: Item) -> Unit) {


    val itemName = remember { mutableStateOf("") }
    val storeName = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))


        ImagePicker(onImageSelected = { uri ->
            selectedImageUri = uri
        })

        Spacer(modifier = Modifier.height(24.dp))

        // Girdi alanları (TextFields)
        TextField(
            value = itemName.value,
            onValueChange = { itemName.value = it },
            placeholder = { Text("Eşya Adı") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = storeName.value,
            onValueChange = { storeName.value = it },
            placeholder = { Text("Mağaza Adı (İsteğe Bağlı)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = price.value,
            onValueChange = { price.value = it },
            placeholder = { Text("Fiyat (İsteğe Bağlı)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(onClick = {

            val imageByteArray = selectedImageUri?.let {
                resizeImage(context, it, maxWidth = 800, maxHeight = 800)
            }


            val itemToInsert = Item(
                itemName = itemName.value,
                storeName = storeName.value,
                price = price.value,
                image = imageByteArray
            )


            saveFunction(itemToInsert)
        }) {
            Text("Kaydet")
        }
    }
}


@Composable
fun ImagePicker(onImageSelected: (Uri?) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        onImageSelected(uri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "İzin verilmedi", Toast.LENGTH_SHORT).show()
        }
    }


    Image(
        painter = if (selectedImageUri != null) rememberAsyncImagePainter(selectedImageUri) else painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Eşya Resmi",
        modifier = Modifier
            .size(300.dp, 200.dp)
            .clickable {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    galleryLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(permission)
                }
            }
    )
}


fun resizeImage(context: Context, uri: Uri, maxWidth: Int, maxHeight: Int): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
        var width = maxWidth
        var height = (width / aspectRatio).toInt()

        if (height > maxHeight) {
            height = maxHeight
            width = (height * aspectRatio).toInt()
        }

        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        byteArrayOutputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}