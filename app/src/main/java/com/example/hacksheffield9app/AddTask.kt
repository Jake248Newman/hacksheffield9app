package com.example.hacksheffield9app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hacksheffield9app.ui.theme.Hacksheffield9appTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class AddTask : ComponentActivity() {
    var database: DatabaseReference = Firebase.database.reference
    var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = intent.getStringExtra("userId").toString()

        setContent {
            Hacksheffield9appTheme {
                Column(){
                    var text1 by remember { mutableStateOf("") }
                    var text2 by remember { mutableStateOf("") }

                    textFeild("Task",text = text1, onTextChange = { newText -> text1 = newText })
                    textFeild("Difficulty", text = text2, onTextChange = { newText -> text2 = newText })
                    SubmitButton {
                        addToDb(text1, text2)
                        text1 = ""
                        text2 = ""
                    }
                }
            }
        }
    }

    fun addToDb(text1: String, text2: String, modifier: Modifier = Modifier.padding(16.dp)){

        var alphabet = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
        var outputString = "-"
        for (i in 1..20){
            outputString += alphabet.random()
        }

        database.child("Tasks").child(outputString).child("difficulty").setValue(text2)
        database.child("Tasks").child(outputString).child("task").setValue(text1)
        database.child("Tasks").child(outputString).child("user_id").setValue(userId)
    }

    @Composable
    fun SubmitButton(onClick: () -> Unit) {
        Button(onClick = { onClick() }) {
            Text("Add task")
        }
    }

    @Composable
    fun textFeild(label:String, text:String, onTextChange: (String) -> Unit){

        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )
    }
}

