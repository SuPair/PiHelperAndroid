package com.wbrawner.pihelper

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.wbrawner.pihelper.MainFragment.Companion.ACTION_DISABLE
import com.wbrawner.pihelper.MainFragment.Companion.ACTION_ENABLE
import com.wbrawner.pihelper.MainFragment.Companion.EXTRA_DURATION
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val addPiHoleViewModel: AddPiHelperViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setBackgroundDrawable(ColorDrawable(getColor(R.color.colorSurface)))
        val navController = findNavController(R.id.content_main)
        val analyticsBundle = Bundle()
        analyticsBundle.putString("intent_action", intent.action)
        val args = when (intent.action) {
            ACTION_ENABLE -> {
                if (addPiHoleViewModel.apiKey == null) {
                    Toast.makeText(this, R.string.configure_pihelper, Toast.LENGTH_SHORT).show()
                    null
                } else {
                    Bundle().apply { putBoolean(ACTION_ENABLE, true) }
                }
            }
            ACTION_DISABLE -> {
                if (addPiHoleViewModel.apiKey == null) {
                    Toast.makeText(this, R.string.configure_pihelper, Toast.LENGTH_SHORT).show()
                    null
                } else {
                    Bundle().apply {
                        putBoolean(ACTION_DISABLE, true)
                        putLong(EXTRA_DURATION, intent.getIntExtra(EXTRA_DURATION, 10).toLong())
                    }
                }
            }
            ACTION_FORGET_PIHOLE -> {
                if (intent.component?.packageName == packageName) {
                    while (navController.popBackStack()) {
                        // Do nothing, just pop all the items off the back stack
                    }
                    // Just return an empty bundle so that the navigation branch below will load
                    // the correct screen
                    Bundle()
                } else {
                    null
                }
            }
            else -> null
        }
        val navDestination = when {
            navController.currentDestination?.id != R.id.placeholder && args == null -> {
                return
            }
            addPiHoleViewModel.baseUrl.isNullOrBlank() -> {
                R.id.addPiHoleFragment
            }
            addPiHoleViewModel.apiKey.isNullOrBlank() -> {
                R.id.retrieveApiKeyFragment
            }
            else -> {
                R.id.mainFragment
            }
        }
        navController.navigate(navDestination, args)
    }

    override fun onBackPressed() {
        when (findNavController(R.id.content_main).currentDestination?.id) {
            R.id.addPiHoleFragment, R.id.mainFragment -> finish()
            else -> super.onBackPressed()
        }
    }

    companion object {
        const val ACTION_FORGET_PIHOLE = "com.wbrawner.pihelper.ACTION_FORGET_PIHOLE"
    }
}
