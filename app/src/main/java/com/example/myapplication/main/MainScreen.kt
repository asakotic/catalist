package com.example.myapplication.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.Cat
import com.example.myapplication.repository.Repository
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onClickHead:(String)->Unit,
    eventPublisher: (MainScreenEvent)->Unit,
    state: MainScreenState){
    val keyboard = LocalSoftwareKeyboardController.current
    //var query by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = "Home",fontSize = 20.sp)
                    },
                    navigationIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.maca),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(255,176,95)
                    )
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                SearchBar(query = state.query,
                    onQueryChange = {
                        eventPublisher(MainScreenEvent.FilterList(it))
                    },
                    placeholder = { Text(text = "Search here..") },
                    onSearch = { keyboard?.hide() },
                    active = false,
                    onActiveChange = {},
                    modifier = Modifier
                        .padding(start = 15.dp)

                ) {}
            }
        },
        content = {
            Spacer(modifier = Modifier.padding(15.dp))

            if(state.loading){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }else if(state.error != null){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = state.error, color = Color.Red, fontSize = 25.sp)
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding() + 15.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = state.listCats) { cat ->
                        MakeCatCard(cat = cat, onClickHead = {onClickHead(cat.id)})
                    }
                }
            }


        }
    )
}


@Composable
fun MakeCatCard(cat: Cat, onClickHead:(String)-> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(255,176,95)),
    ) {
        val catName =
            if(!cat.alternativeName.isNullOrEmpty())
                cat.name + " ("+ cat.alternativeName+")"
            else
                cat.name

        Text(
            text = catName,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        val desc = if (cat.description.length > 250) {
            cat.description.substring(0, 250)
        } else {
            cat.description
        }
        Text(text = desc, fontStyle = FontStyle.Italic, modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .align(Alignment.CenterHorizontally),textAlign = TextAlign.Justify
        )
        DisplayTemperaments(cat = cat)
        AssistChip(
            onClick = {
                onClickHead(cat.id)
            },
            label = { Text(text = "Read more") },
            modifier = Modifier.padding(10.dp)
        )
    }
}
fun findThreeRandom(cat: Cat): List<String> {

    val elements = cat.temperament.replace(" ","").split(",")

    return if (elements.size >= 3){
        listOf(elements[0],elements.last(),elements[elements.size/2])
    }

    else elements
}

@Composable
fun DisplayTemperaments(cat: Cat){
    val list = findThreeRandom(cat)
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()

    ){
        list.forEach(){ tmp ->
            Text(text = tmp,
                textAlign = TextAlign.Center,modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White, shape = RoundedCornerShape(15.dp))
                    .width(200.dp)
                    .height(25.dp)
              )
        }
    }
    
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewCatListScreen() {
    MyApplicationTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "ne moze se ucitati, rip :(", color = Color.Red, fontSize = 25.sp)
        }
    }
}




