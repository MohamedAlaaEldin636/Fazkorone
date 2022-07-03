package com.grand.ezkorone.core.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.grand.ezkorone.R

fun Context.launchTelegram() {
	val intent = packageManager?.getLaunchIntentForPackage("org.telegram.messenger")
	
	if (intent == null) {
		showErrorToast(getString(R.string.app_not_found))
	}
	
	launchActivitySafely {
		if (intent != null) {
			startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.launchDialNumber(phoneNumber: String) {
	val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.trim()}"))
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchWhatsApp(phoneNumber: String) {
	/*
	void openWhatsappContact(String number) {
    Uri uri = Uri.parse("smsto:" + number);
    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
    i.setPackage("com.whatsapp");
    startActivity(Intent.createChooser(i, ""));
}
	 */
	val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${phoneNumber.trim()}"))
	intent.`package` = "com.whatsapp"

	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchShareTextAndSayFromApp(title: String, body: String) {
	launchShareText(
		"$title\n$body\n${getString(R.string.done_by_app_name)}\n${getAppWebLinkOnGooglePay()}"
	)
}

fun Context.launchShareText(text: String) {
	val intent = Intent(Intent.ACTION_SEND).also {
		it.type = "text/plain"
		it.putExtra(Intent.EXTRA_TEXT, text)
	}
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchBrowser(link: String) {
	if (link.isEmpty()) {
		return
	}

	val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Intent.wrapInChooser(title: CharSequence): Intent {
	return Intent.createChooser(this, title)
}

fun Context.launchAppOnGooglePlay() {
	launchActivitySafely {
		try {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).wrapInChooser(getString(R.string.pick_app)))
		}catch (e: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getAppWebLinkOnGooglePay())).wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.getAppWebLinkOnGooglePay(): String {
	return "https://play.google.com/store/apps/details?id=$packageName"
}

private fun Context.launchActivitySafely(msg: String = getString(R.string.something_went_wrong), block: () -> Unit) {
	try {
		block()
	}catch (throwable: Throwable) {
		showErrorToast(msg)
	}
}
