package com.ahmetsirim.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmetsirim.designsystem.R
import com.ahmetsirim.designsystem.theme.UnicoCaseAiVoiceChatAppTheme
import com.ahmetsirim.designsystem.utility.ResponsivenessCheckerPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationalDialog(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int = R.drawable.ic_info_box_error,
    description: String,
    buttonTextAndActionPair: Pair<String, () -> Unit>,
) {

    // In order to solve the problem that InformationalDialog stays on the
    // screen for a long time when navigating to another place with the
    // button in InformationalDialog, this state is managed in the component
    var isInformationalDialogOpen by rememberSaveable { mutableStateOf(true) }

    if (!isInformationalDialogOpen) return

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { /* no-op */ },
    ) {
        ElevatedCard {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(all = 24.dp),
            ) {
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(icon),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        buttonTextAndActionPair.second()
                        isInformationalDialogOpen = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = buttonTextAndActionPair.first,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@ResponsivenessCheckerPreview
@Composable
@Preview
private fun InformationalDialogPreview() {
    UnicoCaseAiVoiceChatAppTheme {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InformationalDialog(
                icon = R.drawable.ic_info_box_error,
                description = "We encountered an error. Please try again.",
                buttonTextAndActionPair = Pair(
                    first = "Ok",
                    second = {}
                )
            )
        }
    }
}