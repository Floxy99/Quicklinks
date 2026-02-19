package com.example.quicklinks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicklinks.generators.DesktopFileGenerator
import java.awt.datatransfer.DataFlavor
import java.io.File
import javax.imageio.ImageIO


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    val name = rememberTextFieldState("")
    val url = rememberTextFieldState("")
    val icon = remember { mutableStateOf(File("/home/felix/.local/share/icons/quicklinks")) }
    val formState = remember { mutableStateOf("")}
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .onKeyEvent{
                    if (it.isAltPressed && it.key == Key.Enter){
                        if (icon.value.path.contains(".png") && name.text != "" && url.text != ""){
                            icon.value.copyTo(File("/home/felix/.local/share/icons/quicklinks/${icon.value.name}"))
                            handleInput(
                                name,
                                url,
                                "/home/felix/.local/share/icons/quicklinks/${icon.value.name}"
                            )
                            name.clearText()
                            url.clearText()
                            formState.value = "Success!"
                            true
                        }else{
                            formState.value = "Failed!"
                            false
                        }
                    }else {
                        false
                    }
                }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(20.dp)
                    .border(
                        3.dp, MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxSize(0.9f),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxSize()
                        .border(
                            5.dp,
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(20.dp))
                        .padding(20.dp)
                        .safeContentPadding(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
                ) {
                    OutlinedTextField(
                        state = name,
                        label = {Text("Name")},
                        lineLimits = TextFieldLineLimits.SingleLine,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    OutlinedTextField(
                        state = url,
                        label = {Text("URL")},
                        lineLimits = TextFieldLineLimits.SingleLine,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    var dropText = remember { mutableStateOf("Dorp a PNG 192x192 px") }
                    Box(
                        Modifier
                            .height(100.dp)
                            .border(BorderStroke(1.dp,Color.Gray),
                                shape = RoundedCornerShape(3.dp))
                            .fillMaxWidth()
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = {true},
                                target = remember {
                                    object: DragAndDropTarget{
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            if (event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
                                                val fileList = event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                                                val file: File? = fileList.firstOrNull()
                                                if (file?.name?.contains(".png") == true){
                                                    val image = ImageIO.read(file)
                                                    if (image.width == 192 && image.height == 192){
                                                        icon.value = file
                                                        dropText.value = "PNG drop successful!"
                                                        return true
                                                    }else {
                                                        dropText.value = "This PNG was not 192x192 px"
                                                    }
                                                }else {
                                                    dropText.value = "This was not a PNG"
                                                }
                                            }
                                            return false
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(dropText.value)
                    }
                    Text("alt + Enter to Create " + formState.value)
                }
            }
        }
    }
}

fun handleInput(name: TextFieldState, url: TextFieldState, iconPath: String) {
    val file = File("/home/felix/.local/share/applications/${name.text.toString()}.desktop")
    if (file.createNewFile()){
        file.writeText(DesktopFileGenerator.getDesktopFileString(name.text.toString(), url.text.toString(), iconPath))
    }
}