package com.example.hacksheffield9app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hacksheffield9app.ui.theme.Hacksheffield9appTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.childEvents
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    var database: DatabaseReference = Firebase.database.reference

    var userId = "-OBr1AIAITcjYkaOXngs"
    var displayTasks = mutableStateListOf<String>()
    var deletionIds = mutableStateListOf<String>()
    var difficulties = mutableStateListOf<String>()
    var userData = mutableStateListOf<String>()
    var userPlots = mutableStateListOf<String>()
    var userEmail = mutableStateListOf<String>()
    var userPassword = mutableStateListOf<String>()

    var loggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var tasks = mutableStateListOf<MutableList<String>>()

        val intentAddTask = Intent(applicationContext, AddTask::class.java)

        database.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.child("Tasks")
                var users = snapshot.child("Users")

                displayTasks.clear()
                tasks.clear()
                deletionIds.clear()
                difficulties.clear()
                userData.clear()
                userPlots.clear()
                userEmail.clear()
                userPassword.clear()


                users.children.forEach { i ->
                    userPlots.add(
                        i.child("available_plots").value.toString()
                    )
                    userData.add(i.key.toString())
                    userEmail.add(i.child("email").value.toString())
                    userPassword.add(i.child("password").value.toString())
                }

                data.children.forEach { i ->
                    tasks.add(
                        mutableListOf(i.child("task").value.toString(),
                            i.child("user_id").value.toString(),
                            i.key.toString(),
                            i.child("difficulty").value.toString()
                        )
                    )
                }

                tasks.forEach { i ->
                    if (i[1].equals(userId)){
                        displayTasks.add(i[0])
                        deletionIds.add(i[2])
                        difficulties.add(i[3])
                    }
                }

                setContent {
                    Hacksheffield9appTheme {
                        if (loggedIn){
                        Column () {
                            SearchBar(displayTasks)
                            BottomNavigation(intentAddTask)
                        }}else {
                            Login()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


    }

    fun deleteTask(text: String, points:Boolean) {
        var index = displayTasks.indexOf(text)
        var index2 = userData.indexOf(userId)
        if (points) {
            database.child("Users").child(userId).child("available_plots")
                .setValue(difficulties[index].toInt() + userPlots[index2].toInt())
        }
        database.child("Tasks").child(deletionIds[index]).removeValue()
    }

    @Composable
    fun Login() {
        var text1 by remember { mutableStateOf("") }
        var text2 by remember { mutableStateOf("") }

        Column() {

            textFeild("Email", text = text1, onTextChange = { newText -> text1 = newText })
            textFeild("Password", text = text2, onTextChange = { newText -> text2 = newText })

            LoginButton() {
                var index = userEmail.indexOf(text1)
                if (userPassword[index].equals(text2)){
                    userId = userData[index]
                    loggedIn = true

                    database.child("Temp").setValue(0)
                    database.child("Temp").removeValue()
                }else{
                    text1 = ""
                    text2 = ""
                }
            }
            SignupButton() {
                var alphabet = listOf(
                    "a",
                    "b",
                    "c",
                    "d",
                    "e",
                    "f",
                    "g",
                    "h",
                    "i",
                    "j",
                    "k",
                    "l",
                    "m",
                    "n",
                    "o",
                    "p",
                    "q",
                    "r",
                    "s",
                    "t",
                    "u",
                    "v",
                    "w",
                    "x",
                    "y",
                    "z",
                    "A",
                    "B",
                    "C",
                    "D",
                    "E",
                    "F",
                    "G",
                    "H",
                    "I",
                    "J",
                    "K",
                    "L",
                    "M",
                    "N",
                    "O",
                    "P",
                    "Q",
                    "R",
                    "S",
                    "T",
                    "U",
                    "V",
                    "W",
                    "X",
                    "Y",
                    "Z"
                )
                var outputString = "-"
                for (i in 1..20) {
                    outputString += alphabet.random()
                }

                database.child("Users").child(outputString).child("available_plots").setValue(0)
                database.child("Users").child(outputString).child("email").setValue(text1)
                database.child("Users").child(outputString).child("password").setValue(text2)

                userId = outputString
                loggedIn = true
            }
        }
    }

    @Composable
    fun LoginButton(onClick: () -> Unit) {
        Button(onClick = { onClick() }) {
            Text("Login")
        }
    }

    @Composable
    fun SignupButton(onClick: () -> Unit) {
        Button(onClick = { onClick() }) {
            Text("SignUp")
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

    @Composable
    fun SearchBar(
        taskDataInput: MutableList<String>,
        modifier: Modifier = Modifier
    ) {
        var text1 by remember { mutableStateOf("") }
        var taskDataOutput = mutableListOf<String>()

        TextField(
            value = text1,
            onValueChange = {
                newText -> text1 = newText },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = {
                Text("Search")
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )

        taskDataInput.forEach {
                i ->
            try {
                if (i.lowercase().substring(0, text1.length).contains(text1.lowercase())) {
                    taskDataOutput.add(i)
                    println(taskDataOutput.size)
                }
            }catch (e: StringIndexOutOfBoundsException) {
                taskDataOutput = taskDataInput
            }
        }

        TasksGrid(taskDataOutput)
    }

    @Composable
    fun TasksGrid(
        taskData: MutableList<String>
        //todo: hard coded value so won't work on any screen
    ) {
        LazyColumn(
            modifier = Modifier.height(700.dp)
        ) {
            items(
                items = taskData,
            ) { text ->
                TaskCard(text = text)
            }
        }
    }

    @Composable
    fun TaskCard(
        text: String,
        modifier: Modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier
                .clickable {
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 10.dp, vertical = 25.dp)
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 10.dp, vertical = 25.dp)
                        .absoluteOffset(x=-25.dp)
                        .clickable {  -> deleteTask(text,false) }
                )
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 10.dp, vertical = 25.dp)
                        .clickable {  -> deleteTask(text,true) }
                )
            }
        }
    }

    @Composable
    private fun BottomNavigation( intent: Intent,modifier: Modifier = Modifier) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Tasks")
                },
                selected = true,
                onClick = {}
            )
            var context = LocalContext.current
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Add")
                },
                selected = false,
                onClick = {
                    val intentAddTask = Intent(context, AddTask::class.java)
                    intentAddTask.putExtra("userId", userId)
                    context.startActivity(intentAddTask)
                }
            )
        }
    }

}

