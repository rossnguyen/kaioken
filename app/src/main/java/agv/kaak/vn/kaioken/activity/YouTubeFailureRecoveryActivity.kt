package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.content.Intent
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

abstract class YouTubeFailureRecoveryActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val RECOVERY_DIALOG_REQUEST = 1

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        if (p1!!.isUserRecoverableError) {
            p1.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(getString(R.string.all_error_global), p1.toString())
            GlobalHelper.showMessage(this, errorMessage,true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOVERY_DIALOG_REQUEST)
            getYouTubePlayerProvider().initialize(resources.getString(R.string.google_maps_key), this)

    }

    protected abstract fun getYouTubePlayerProvider(): YouTubePlayer.Provider
}