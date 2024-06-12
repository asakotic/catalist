package com.example.myapplication.scnd

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.repository.Repository
import com.example.myapplication.controller.Navigation
import com.example.myapplication.controller.Screen
import com.example.myapplication.model.Cat
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SingleView(
    onClickBack: ()->Unit,
    onWikiClick:(wiki: String)->Unit,
    state: SecondScreenState){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Preview")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onClickBack()
                    }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Menu icon")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(255,176,95)
                )
            )
        },
        content = {

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
            }else if(state.cat != null){
                MakeCatView(cat = state.cat ,it.calculateTopPadding(),onWikiClick)
            }else{
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "cat not found", color = Color.Red, fontSize = 25.sp)
                }
            }
        }
    )
}

@Composable
fun MakeCatView(cat: Cat, calculateTopPadding: Dp,onWikiClick: (wiki:String)->Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .padding(top = calculateTopPadding)
            .verticalScroll(state = rememberScrollState())
    ) {
        val catName = if(!cat.alternativeName.isNullOrEmpty())
            cat.name + " ("+ cat.alternativeName+")"
        else
            cat.name

        Text(
            text = catName,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 15.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        if(cat.image?.url != null)
            AsyncImage(model = cat.image.url,
                contentDescription = "",
                modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.maca)
            )
        Text(text = cat.description, fontStyle = FontStyle.Italic, modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .align(Alignment.CenterHorizontally),textAlign = TextAlign.Justify
        )
        DisplayAllTemperaments(cat = cat)
        DisplayOther(cat = cat)
        DisplayRating(cat = cat)
        if(cat.wikipediaUrl!= null)
            AssistChip(
                onClick = {
                    onWikiClick(cat.wikipediaUrl)
                },
                label = { Text(text = "Read more on Wikipedia!") },
                modifier = Modifier.padding(10.dp)
            )

    }
}

@Composable
fun DisplayOther(cat: Cat) {
    Column (
        modifier = Modifier
            .padding(10.dp)
            .padding(vertical = 5.dp)
    ){
        Text(text = "Origin: "+cat.origin)
        Text(text = "Life span: "+cat.lifeSpan)
        Text(text = "Weight: "+cat.weight.metric)
        Text(text = "Rare: ${if(cat.rare>0) "Yes" else "No"}")
    }
}

@Composable
fun DisplayRating(cat: Cat){
    Column (
        modifier = Modifier
            .padding(10.dp)
    ){
        Text(text = "Adaptability")
        RatingBar(rating = cat.adaptability)
        Text(text = "Child friendly")
        RatingBar(rating = cat.childFriendly)
        Text(text = "Dog friendly")
        RatingBar(rating = cat.dogFriendly)
        Text(text = "Energy level")
        RatingBar(rating = cat.energyLevel)
        Text(text = "Intelligence")
        RatingBar(rating = cat.intelligence)
    }
}

@Composable
fun DisplayAllTemperaments(cat: Cat){
    val list = cat.allTemperaments()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)

    ){
        list.forEach{ tmp ->
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
fun PreviewCat() {
    MyApplicationTheme {
        Navigation(Screen.DetailScreen.withArgs("1"))
    }
}
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    stars: Int = 5,
    starsColor: Color = Color(237, 118, 14),
) {
    val filledStars = if(rating != 0)rating else 1
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
    }
}


