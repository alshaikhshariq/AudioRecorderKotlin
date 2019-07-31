package com.centosquare.devatease.audiorecorderkotlin

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener
{
    private val RECORD_AUDIO_REQUEST_CODE = 123
    private val permissionRequestCode = 123
    private val permissions = arrayOf(RECORD_AUDIO, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        if (!hasPermissions(this, permissions))
        {
            getUserPermissions()
        }
        else
        {
            prepareForRecording()
        }

        //Initializing the view Components
        clickListeners()
    }

    //this method is checking if permissions are given or not
    private fun hasPermissions(context: Context?, permissions: Array<String>?): Boolean
    {
        if (context != null && permissions != null)
        {
            for (permission in permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false
                }
            }
        }
        return true
    }

    private fun clickListeners()
    {
        view_recordings_.setOnClickListener {
            val intent = Intent(this, ListRecordings::class.java)
            startActivity(intent)
        }
    }

    private fun getUserPermissions()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, RECORD_AUDIO

            ) && ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
         && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE))
                {
            //displaying custom alert dialog box
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle("Permission necessary")
            alertBuilder.setMessage("Permissions are required otherwise app will not work")
            alertBuilder.setPositiveButton("ok")
            { _, _ ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                    permissionRequestCode
                )
            }
            alertBuilder.setNegativeButton("Cancel")
            { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            val alert = alertBuilder.create()
            alert.show()
        }
        else
        {
            //if permissions are not given then ask permissions again
            ActivityCompat.requestPermissions(
                this,
                arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                permissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        //If the user grant permission
        if (requestCode == permissionRequestCode)
        {
            if (grantResults.isNotEmpty())
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                    prepareForRecording()

                } else
                {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
                    finish()
                }//otherwise this code will run
            }
        }
    }

    private fun prepareForRecording()
    {
        Toast.makeText(this, "You Can Record An Audio Now", Toast.LENGTH_SHORT).show()

    }

    override fun onClick(p0: View?)
    {

    }
}
