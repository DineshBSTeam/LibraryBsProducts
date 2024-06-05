package com.lib.bslibrary.internal.leads.trackQuery

import ApiRepository
import TrackQueryViewModel
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lib.bslibrary.R
import com.lib.bslibrary.internal.helper.RetrofitService
import com.lib.bslibrary.internal.leads.leadlist.models.DocMedia
import com.lib.bslibrary.internal.leads.leadlist.models.LeadQueryData

class TrackQueryActivity : AppCompatActivity() {
    private val retrofitService = RetrofitService.getInstance()
    private val mainRepository = ApiRepository(retrofitService)
    private val viewModel: TrackQueryViewModel by viewModels { TrackQueryModelFactory(mainRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val leadId = intent.getIntExtra("LEAD_ID", 0)

        setContent { ChatScreen(viewModel, leadId, this) }

        viewModel.getQueriesListData(leadId)
    }
}

@Composable
fun ChatScreen(viewModel: TrackQueryViewModel, leadId: Int, activity: AppCompatActivity) {
    // State to manage the scroll position of the list
    val listState = rememberLazyListState()

    val chatListing by viewModel.queriesList.observeAsState()

    val isLoading by viewModel.loading.observeAsState(false)

    if (isLoading) {
        // Show loading indicator if isLoading is true
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                AppBar(activity, "Dispute")
            },
            content = { _QueriesBodyView(listState, chatListing) },
            bottomBar = {
                if (chatListing?.status != false) {
                    _BottomView(viewModel, leadId)
                }
            }
        )
    }

    // Scroll to the bottom when the page is opened
    LaunchedEffect(Unit) { listState.scrollToItem(Int.MAX_VALUE) }

    // Scroll to the bottom when a new message is submitted
    LaunchedEffect(chatListing) { listState.scrollToItem(chatListing?.queries?.size ?: 0) }
}

@Composable
fun AppBar(activity: AppCompatActivity, title: String, modifier: Modifier = Modifier) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.high),
        elevation = 0.dp,
        title = {
            Box(
                modifier
                    .fillMaxWidth()
                    .offset((-12).dp, 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { activity.finish() }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back_arrow), // replace with your back button asset
                            contentDescription = "Back button",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.W700,
                            color = Color.Black,
                            fontSize = 16.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Composable
fun _QueryStatusView(chatListing: LeadQueryData?, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.blueBgColorBottom)),
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text =
                if (chatListing?.queryType != null && chatListing.queryType.isNotEmpty()
                ) {
                    chatListing.queryType.toString()
                } else {
                    "Dispute Related to :\nHad a successful lead"
                },
                maxLines = 2,
                style = MaterialTheme.typography.body1.copy(color = colorResource(id = R.color.black))
            )
            Box(
                modifier
                    .background(
                        if (chatListing?.status == true) {
                            colorResource(id = R.color.yellowBgColor).copy(alpha = 0.30f)
                        } else {
                            colorResource(id = R.color.redBgColorExpired)
                        },
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text =
                    if (chatListing?.status == true) {
                        "Open"
                    } else {
                        "Closed"
                    },
                    style =
                    MaterialTheme.typography.caption.copy(
                        color =
                        if (chatListing?.status == true) {
                            colorResource(id = R.color.yellowTextColorPending)
                        } else {
                            colorResource(id = R.color.redTextColorExpired)
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun _QueriesBodyView(
    listState: LazyListState,
    chatListing: LeadQueryData?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(bottom = 90.dp)
            .background(colorResource(id = R.color.white))
    ) {

        Column {

            _QueryStatusView(chatListing)

            LazyColumn(
                modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                reverseLayout = false,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState
            ) {
                itemsIndexed(chatListing?.queries ?: emptyList()) { index, query ->
                    Bubble(
                        message = query.comment ?: "",
                        time = query.createdAt?.split(' ')?.get(0)?.replace('T', ' ') ?: "",
                        isShowDate = query.shouldShowDate,
                        documentsImages = query.documentImage ?: emptyList(),
                        documentsFiles = query.documentFile ?: emptyList(),
                        isMe = query.adminId == null
                    )
                }
            }
        }
    }
}

@Composable
fun _BottomView(viewModel: TrackQueryViewModel, leadId: Int, modifier: Modifier = Modifier) {
    val leadQueryData by viewModel.queriesList.observeAsState()
    val state = leadQueryData?.status
    val isButtonLeading by viewModel.buttonLoading.observeAsState()
    var typedMessage by remember { mutableStateOf(TextFieldValue(text = "")) }

    Box(modifier.fillMaxWidth()) {
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = typedMessage,
                placeholder = { Text(text = "Send Message") },
                onValueChange = { newValue: TextFieldValue -> typedMessage = newValue },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (typedMessage.text.isNotBlank()) {
                                viewModel.submitQueryData(
                                    typedMessage.text.trim(),
                                    leadId,
                                    null
                                )
                                typedMessage = TextFieldValue(text = "")
                            }
                        }
                    ) {
                        if (isButtonLeading == true) {
                            CircularProgressIndicator(modifier.size(24.dp))
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send message",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                enabled = state ?: false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.blueBgColorBottom))
                    .padding(16.dp),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors =
                TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    disabledIndicatorColor = Color.Green,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle =
                MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                )
            )
            Spacer(modifier.width(12.dp))
        }
    }
}

@OptIn(coil.annotation.ExperimentalCoilApi::class)
@Composable
fun Bubble(
    message: String,
    time: String,
    isMe: Boolean,
    isShowDate: Boolean,
    documentsImages: List<DocMedia>,
    documentsFiles: List<DocMedia>,
    modifier: Modifier = Modifier
) {
    val align = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    val radius =
        if (isMe) {
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 0.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        } else {
            RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        }

    val context = LocalContext.current

    Column {
        if (isShowDate) {
            Text(
                text = time,
                style = androidx.compose.ui.text.TextStyle(color = Color.Gray),
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.height(4.dp))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = modifier.fillMaxWidth()) {
                Column(
                    modifier = modifier
                        .align(align)
                        .padding(horizontal = 24.dp)
                        .shadow(
                            elevation = 0.dp,
                            shape = RoundedCornerShape(16.dp),
                            clip = true
                        )
                        .background(
                            color = colorResource(id = R.color.greyBgUserComment),
                            shape = radius
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = message,
                        style = androidx.compose.ui.text.TextStyle(color = Color.Black),
                    )

                    // Display images
                    documentsImages.forEach { docMedia ->
                        Image(
                            painter = rememberImagePainter(docMedia.url),
                            contentDescription = docMedia.name,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        )
                    }

                    // Display PDFs
                    documentsFiles.forEach { docMedia ->
                        Row(
                            modifier = modifier
                                .clickable {
                                    docMedia.url?.let { url ->
                                        openPdf(context, url)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_pdf),
                                contentDescription = "PDF Icon",
                                modifier = modifier.size(44.dp),
                                tint = Color.Unspecified // Keep the original icon colors
                            )
                        }
                    }
                }
                if (isMe) {
                    Box(
                        modifier = modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 0.dp, end = 0.dp)
                    ) {
                        Box(
                            modifier = modifier
                                .background(
                                    color = Color.Blue,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = modifier
                                    .size(10.dp)
                                    .background(
                                        color = colorResource(id = R.color.blueBgColorBottom),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = modifier
                            .align(Alignment.TopStart)
                            .padding(top = 0.dp, start = 0.dp)
                    ) {
                        Box(
                            modifier = modifier
                                .background(
                                    color = Color.Blue,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(6.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_bot),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}

fun openPdf(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(url), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No application available to view PDF, opening in browser",
            Toast.LENGTH_SHORT
        ).show()
        // Open PDF in browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}