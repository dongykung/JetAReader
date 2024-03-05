package com.example.jetareader.screen.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetareader.R
import com.example.jetareader.components.InputField
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.model.MBook
import com.example.jetareader.navigation.ReaderScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiList = viewModel.bookList
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError
    val isEmpty = viewModel.isEmpty
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                scrollBehavior = scrollBehavior,
                navController = navController,
                onBack = true,
                showLogOut = false
            ) {
                navController.popBackStack()
            }
        }) { innerPadding ->
        Surface(Modifier.padding(innerPadding)) {
            Column {
                SearchForm(
                    searchQueryState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onSearch = { query ->
                        Log.d("SearchWord", "ReaderSearchScreen: $query")
                        viewModel.loadBooks(query)
                    }
                )
                Spacer(modifier = Modifier.height(13.dp))
                if (isLoading) {
                    LoadingScreen(Modifier.fillMaxSize())
                } else if (isError) {
                    ErrorScreen(Modifier.fillMaxSize(), "error") {
                        viewModel.loadBooks(searchQueryState.value.trim())
                    }
                }
                else if(isEmpty){
                    ErrorScreen(Modifier.fillMaxSize(),errorMessage = "not result") {
                        viewModel.loadBooks(searchQueryState.value.trim())
                    }
                }
                else {
                    SearchBookList(
                        BookList = uiList,
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBookList(
    BookList: List<Item>,
    onBookClick: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(BookList, key = { book -> book.id }) { book ->
            SearchBookItem(book = book, modifier = Modifier.clickable {
                onBookClick(book.id)
            })
        }
    }
}

@Composable
fun SearchBookItem(
    book: Item,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RectangleShape,
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            val imageLinks =
                if (book.volumeInfo.imageLinks != null) book.volumeInfo.imageLinks.thumbnail
                else ""
            AsyncImage(
                model = imageLinks, contentDescription = "book image",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.baseline_menu_book_24)
            )

            Column(modifier = Modifier.padding(6.dp)) {
                Text(
                    text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = "Author : ${book.volumeInfo.authors}", overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Date : ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "${book.volumeInfo.categories}", overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )

            }
        }
    }
}

@Composable
fun SearchForm(
    searchQueryState: MutableState<String>,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {},
) {
    Column {
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) { searchQueryState.value.trim().isNotEmpty() }
        InputField(valueState = searchQueryState, labelId = hint, enabled = true,
            keyboardActions = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                keyboardController?.hide()
            }
        )
    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        CircularProgressIndicator()

    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String,
    retryAction: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_error_24), contentDescription = ""
        )
        Text(text = errorMessage, modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(text = "retry")
        }
    }
}