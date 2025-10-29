package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration // <-- НОВЫЙ ИМПОРТ
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

data class Artwork(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val artistYearRes: Int,
    @StringRes val contentDescriptionRes: Int
)

@Composable
fun ArtSpaceApp() {
    val artworks = remember {
        listOf(
            Artwork(
                R.drawable.bridge,
                R.string.bridge_title,
                R.string.bridge_artist_year,
                R.string.bridge_content_description
            ),
            Artwork(
                R.drawable.flowers,
                R.string.flowers_title,
                R.string.flowers_artist_year,
                R.string.flowers_content_description
            ),
            Artwork(
                R.drawable.monolisa,
                R.string.monalisa_title,
                R.string.monalisa_artist_year,
                R.string.monalisa_content_description
            )
        )
    }

    var currentArtworkIndex by remember { mutableStateOf(0) }

    fun showNextArtwork() {
        currentArtworkIndex = (currentArtworkIndex + 1) % artworks.size
    }

    fun showPreviousArtwork() {
        currentArtworkIndex = (currentArtworkIndex - 1 + artworks.size) % artworks.size
    }


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val artworkDetails = @Composable {
        ArtworkDescriptor(artwork = artworks[currentArtworkIndex])
        Spacer(modifier = Modifier.height(16.dp))
        ArtworkController(
            onPreviousClick = { showPreviousArtwork() },
            onNextClick = { showNextArtwork() }
        )
    }

    if (screenWidth > 600.dp) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ArtworkWall(artwork = artworks[currentArtworkIndex])
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                artworkDetails()
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ArtworkWall(artwork = artworks[currentArtworkIndex])

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                artworkDetails()
            }
        }
    }
}


@Composable
fun ArtworkWall(artwork: Artwork) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Image(
            painter = painterResource(id = artwork.imageRes),
            contentDescription = stringResource(id = artwork.contentDescriptionRes),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ArtworkDescriptor(artwork: Artwork) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFECEBF4))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = artwork.titleRes),
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(id = artwork.artistYearRes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }
    }
}


@Composable
fun ArtworkController(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = onPreviousClick,
            modifier = Modifier.width(140.dp)
        ) {
            Text(text = stringResource(id = R.string.previous_button))
        }

        Button(
            onClick = onNextClick,
            modifier = Modifier.width(140.dp)
        ) {
            Text(text = stringResource(id = R.string.next_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}

@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun TabletPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}