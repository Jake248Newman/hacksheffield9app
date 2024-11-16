package com.example.hacksheffield9app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hacksheffield9app.ui.theme.Hacksheffield9appTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    var database: DatabaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //writeNewUser("jake@gmail.com", "STRING","SecurePassword", "2", "Jake")

        setContent {
            Hacksheffield9appTheme {
                MyApp()
            }
        }
    }

    private fun writeNewUser(emailAddress: String? = null, gardenLayout: String? = null, password: String? = null, userId: String, username: String) {
        val user = User(userId, emailAddress, gardenLayout, password, username)

        database.child(userId).setValue(user)
    }
}

@IgnoreExtraProperties
data class User(
    val userId: String? = null,
    val email: String? = null,
    val gardenLayout: String? = null,
    val password: String? = null,
    val username: String? = null)

@Composable
fun MyApp(
) {
    Column () {
        SearchBar()
        FavoriteCollectionsGrid()
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
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
}

@Composable
fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier

) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = taskData) { text ->
            TaskCard(text = text)
        }
    }
}

@Composable
fun TaskCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(255.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

private val taskData = listOf("a","b","c","d","e","f")

