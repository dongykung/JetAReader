package com.example.jetareader.screen.home

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetareader.R
import com.example.jetareader.components.BookRating
import com.example.jetareader.components.FABContent
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.components.ReaderLogo
import com.example.jetareader.components.RoundedButton
import com.example.jetareader.components.TitleSection
import com.example.jetareader.model.MBook
import com.example.jetareader.navigation.ReaderScreens
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,
               viewModel: HomeViewModel= viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listOfBooks = viewModel.data.value.data!!
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ReaderAppBar(
                title = "A.Reader",
                scrollBehavior = scrollBehavior,
                navController = navController
            )
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HomeContent(navController,listOfBooks)
        }
    }
}

@Composable
fun HomeContent(navController: NavController,
                listOfBooks: List<MBook>) {

    val scrollState = rememberScrollState()
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    else "N/A"

    Column(
        modifier = Modifier
            .padding(2.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,

    ) {
        Row(
            Modifier.align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleSection(label = " Your reading\n" + " activity right now")
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp))
                Text(
                    text = currentUserName.toString(),
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(2.dp),
                    overflow = TextOverflow.Clip
                )
            }
        }
        ReadingRightNowArea(books = listOf(), navController = navController)
        TitleSection(label = "Reading List")

        BookListArea(listOfBooks,navController
        )
    }
}


@Composable
fun ReadingRightNowArea(
    books: List<MBook>,
    navController: NavController
) {
    ListCard(onPressDetails = {

    })
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    LazyRow(modifier = Modifier.heightIn(280.dp)) {
        items(listOfBooks) {book->
            ListCard(book=book,onPressDetails = {
                Log.d("BookClicked", "BookListArea: $it")
            })
        }
    }
}

@Preview
@Composable
fun ListCard(
    book: MBook = MBook(id = "as", title = "Runng", authors = "dong", notes = "good"),
    onPressDetails: (String) -> Unit = {}
) {

    Card(shape = RoundedCornerShape(29.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .width(202.dp)
            .height(242.dp)
            .clickable { onPressDetails(book.title.toString()) }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.Center) {
                val imageLinks =
                    if (book.photoUrl != null) book.photoUrl
                    else ""
                AsyncImage(
                    model = imageLinks, contentDescription = "",
                    modifier = Modifier
                        .height(140.dp)
                        .width(140.dp)
                        .clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.weight(0.7f))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, contentDescription = "Fav Icon",
                        modifier = Modifier.padding(1.dp)
                    )
                    BookRating(3.5)
                }
            }
            Text(
                text = book.title.toString(),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = book.authors.toString(), modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                RoundedButton("Reading", radius = 70)
            }
        }
    }
}




