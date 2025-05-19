package com.example.mealmatchui.ui.screens

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mealmatchui.data.api.RetrofitClient
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    onNavigateBack: () -> Unit,
    onCaptureSuccess: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotoğraf Çek") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            }

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            isLoading = true
                            errorMessage = null
                            
                            // Burada gerçek kamera görüntüsünü alıp Base64'e çevirmeniz gerekiyor
                            // Şimdilik test amaçlı boş bir bitmap kullanıyoruz
                            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            val imageBytes = outputStream.toByteArray()
                            val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                            val response = RetrofitClient.mealApiService.analyzeImage(
                                ImageData(imageBase64 = base64Image)
                            )
                            
                            onCaptureSuccess(response.recipeId)
                        } catch (e: Exception) {
                            errorMessage = "Hata oluştu: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.padding(16.dp),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Camera, contentDescription = "Fotoğraf Çek")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fotoğraf Çek")
            }
        }
    }
} 