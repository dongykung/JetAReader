package com.example.jetareader.screen.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jetareader.R
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.components.RoundedButton
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.model.MBook
import com.example.jetareader.navigation.ReaderScreens
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember{ SnackbarHostState() }
    val scope = rememberCoroutineScope() //snack바가 suspend fun 이기 때문

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ReaderAppBar(title = "Book details", navController = navController,
                onBack = true,
                showLogOut = false,
                showProfile = false,
                onBackArrowClicked = { navController.popBackStack() })
        }) { innerPadding ->
        Surface(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp, start = 10.dp, end = 10.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookById(bookId)
                }.value
                if (bookInfo.data == null) {
                    CircularProgressIndicator()
                } else {
                    ShowBookDetails(bookInfo.data,
                        onCancelButtonClicked = {
                            navController.popBackStack()},
                        saveBookItem = {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(message = "Save Complete",
                                    actionLabel = "close",
                                    duration = SnackbarDuration.Short)
                                when(result){
                                    SnackbarResult.Dismissed ->{println("dissmiss")}
                                    SnackbarResult.ActionPerformed->{println("performed")}
                                }
                            }
                        })


                }

            }
        }
    }
}

@Composable
fun ShowBookDetails(item: Item,
                    onCancelButtonClicked : ()-> Unit,
                    saveBookItem : ()->Unit) {
    val bookdata = item.volumeInfo
    val imageLinks =
        if (item.volumeInfo.imageLinks != null) item.volumeInfo.imageLinks.thumbnail
        else ""
    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        AsyncImage(
            model = imageLinks, contentDescription = "Book Image",
            modifier = Modifier
                .height(90.dp)
                .width(90.dp)
                .padding(1.dp),
            error = painterResource(id = R.drawable.baseline_menu_book_24),
            placeholder = painterResource(id = R.drawable.baseline_data_saver_off_24),
            contentScale = ContentScale.Crop
        )
    }
    Text(
        text = bookdata.title,
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        maxLines = 19
    )

    Text(text = "Authors : ${bookdata.authors}",
        textAlign = TextAlign.Center,)
    Text(text = "Page Count : ${bookdata.authors}",
        textAlign = TextAlign.Center,)
    Text(text = "Categorys : ${bookdata.categories}",
        textAlign = TextAlign.Center,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis)
    Text(
        text = "Published : ${bookdata.publishedDate}",
        style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(10.dp))

    val cleanDescription = if(!bookdata.description.isNullOrEmpty())
        HtmlCompat.fromHtml(bookdata.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
    else ""
    val localDims = LocalContext.current.resources.displayMetrics
    if(!cleanDescription.isNullOrEmpty()) {
        Surface(
            modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.09f))
                .padding(4.dp), shape = RectangleShape,
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            LazyColumn(Modifier.padding(3.dp)) {
                item {
                    Text(text = cleanDescription.toString())
                }
            }
        }
    }
    Row(modifier=Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center) {
        RoundedButton(label = "Save"){
            //save this book to the firebase database
            if(bookdata.categories.isNullOrEmpty())
                bookdata.categories = listOf("")
            val book=MBook(
                title = bookdata.title,
                authors = bookdata.authors.toString(),
                description = bookdata.description,
                categories = bookdata.categories.toString(),
                notes = "",
                photoUrl = imageLinks,
                publishedDate = bookdata.publishedDate,
                pageCount = bookdata.pageCount.toString(),
                rating = 0.0,
                googleBookId = item.id,
                userId = FirebaseAuth.getInstance().currentUser?.uid
            )
            saveToFirebase(book, saveBookItem = saveBookItem)
        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label="Cancel"){
            onCancelButtonClicked()
        }
    }

}


fun saveToFirebase(book:MBook,
                   saveBookItem: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    val uuid : UUID = UUID.randomUUID()
    book.id=uuid.toString()
    if(book.toString().isNotEmpty()){
        dbCollection.document(uuid.toString()).set(book).addOnCompleteListener{task->
            if(task.isSuccessful){
                saveBookItem()
            }
        }.addOnFailureListener{
            Log.w("ErrorSaveFirebase", "saveToFirebase : Error Updating doc",it)
        }

    }else{

    }
}
