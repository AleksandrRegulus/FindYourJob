package ru.practicum.android.diploma.data.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.practicum.android.diploma.domain.share.ExternalNavigator
import javax.inject.Inject

class ExternalNavigatorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ExternalNavigator {

    override fun openEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("mailto:$email")
        }
        context.startActivity(emailIntent)
    }

    override fun openPhone(phoneNumber: String) {
        val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(phoneIntent)
    }

    override fun shareVacancy(vacancyUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, vacancyUrl)
        }
        context.startActivity(shareIntent)
    }
}
