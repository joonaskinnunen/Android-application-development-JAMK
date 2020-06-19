package com.example.roomwordsample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_new_word.*

const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

class NewWordActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    private var mIsLoading = false

    private lateinit var mRewardedAd: RewardedAd
    private lateinit var editWordView: EditText
    private lateinit var showVideoButton: Button
    private lateinit var button: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        loadRewardedAd()
        MobileAds.initialize(this) {}
        showVideoButton = findViewById(R.id.videoButton)
        showVideoButton.setOnClickListener { showRewardedVideo() }

        editWordView = findViewById(R.id.edit_word)
        button = findViewById(R.id.button_save)
        button.visibility = View.INVISIBLE
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editWordView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editWordView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    private fun loadRewardedAd() {
        if (!(::mRewardedAd.isInitialized) || !mRewardedAd.isLoaded) {
            mIsLoading = true
            mRewardedAd = RewardedAd(this, AD_UNIT_ID)
            mRewardedAd.loadAd(
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onRewardedAdLoaded() {
                        mIsLoading = false
                        Log.d("ADLOAD", "onRewardedAdLoaded")
                    }

                    override fun onRewardedAdFailedToLoad(errorCode: Int) {
                        mIsLoading = false
                        Log.d("ADLOAD", "onRewardedAdFailedToLoad")
                    }
                }
            )
        }
    }

    private fun showRewardedVideo() {
        if (mRewardedAd.isLoaded) {
            mRewardedAd.show(
                this,
                object : RewardedAdCallback() {
                    override fun onUserEarnedReward(
                        rewardItem: RewardItem
                    ) {
                        Log.d("RewardedAD", "onUserEarnedReward")
                        button.visibility = View.VISIBLE
                        videoButton.visibility = View.INVISIBLE
                    }

                    override fun onRewardedAdClosed() {
                        Log.d("RewardedAD", "onRewardedAdClosed")
                        loadRewardedAd()
                    }

                    override fun onRewardedAdFailedToShow(errorCode: Int) {
                        Log.d("RewardedAD", "onRewardedAdFailedToShow")
                    }

                    override fun onRewardedAdOpened() {
                        Log.d("RewardedAD", "onRewardedAdOpened")
                    }
                }
            )
        } else {
            Log.d("RewardedAD", "Ads not loaded yet")

        }
    }
}