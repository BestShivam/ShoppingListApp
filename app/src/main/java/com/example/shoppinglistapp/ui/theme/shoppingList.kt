package com.example.shoppinglistapp.ui.theme

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.R

data class shoppingList (
    val id :Int=1,
    var name : String,
    var quantity : Int,
    var isEdited : Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp(){
    var sItems by remember { mutableStateOf(listOf<shoppingList>()) }
    var isAlertDialogShow by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column (
        Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center){
        Button(onClick = { isAlertDialogShow = true },
            Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){Item ->
                // do it yourself
               if (Item.isEdited){
                   ShoppingItemEdit(Item = Item) { editName,editQuantity ->
                       sItems = sItems.map { it.copy(isEdited = false) }
                       val editItem = sItems.find{it.id == Item.id}
                       editItem?.let {
                           it.name = editName
                           it.quantity = editQuantity
                       }


                   }
               }
                else{
                    ShoppingListItem(item = Item , onEditClick = {
                                                                 sItems = sItems.map { it.copy(isEdited = it.id == Item.id) }
                    } , onDeleteClick = {
                        sItems = sItems - Item
                    })

               }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // show alert Dialog to take name and quantity of item
    if (isAlertDialogShow){
        AlertDialog(onDismissRequest = { isAlertDialogShow = false },
            confirmButton = {
                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    Button(onClick = {
                        if(itemName.isNotBlank() && itemQuantity.isNotBlank()){
                            val newItem = shoppingList(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )

                            sItems = sItems + newItem
                            itemName = ""
                            itemQuantity = ""
                            isAlertDialogShow = false
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = { isAlertDialogShow = false
                    itemName = ""}) {
                        Text(text = "Cancel")
                    }
                }
                            },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName, onValueChange = {itemName = it},
                        modifier = Modifier.padding(8.dp),
                        label = {Text(text = "Enter Item name")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        Modifier.padding(8.dp),
                        label = { Text(text = "Enter Quntity")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            })
    }
}

@Composable
fun ShoppingListItem(
    item : shoppingList,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit
){
    Row (
        Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(3.dp, color = Color.Gray),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically

    ){
        Text(text = item.name, 
            style = MaterialTheme.typography.titleLarge)
        Text(text = "Qt: ${item.quantity.toString()}",
            style =  MaterialTheme.typography.titleLarge)
        Row {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit,
                    contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = null)
            }
        }

    }
}

@Composable
fun ShoppingItemEdit(Item : shoppingList,
                     onEditComplete : (String,Int) -> Unit
                     ){
    var editName by remember { mutableStateOf(Item.name) }
    var editQuantity by remember { mutableStateOf(Item.quantity.toString()) }
    Row (modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            // Item's Name
            BasicTextField(value = editName,
                onValueChange = {editName = it},
                singleLine = true,
                modifier = Modifier.padding(8.dp))
            // Item's Quantity
            BasicTextField(value = editQuantity,
                onValueChange = {editQuantity = it},
                singleLine = true,
                modifier = Modifier.padding(8.dp))
        }
        
        Button(onClick = {
            onEditComplete(editName,editQuantity.toIntOrNull()?:1)
            Item.isEdited = false
        }) {
            Text(text = "Save")
        }
    }
}