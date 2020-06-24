package com.mobile.azrinurvani.telephonymanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askReadPhoneStatePermission()
    }

    private fun askReadPhoneStatePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE),111)
        }else{
            getTelephonyDetail()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getTelephonyDetail()
        }
    }

    private fun getTelephonyDetail() {
        var tm : TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            )!= PackageManager.PERMISSION_GRANTED){
            return
        }

        var data = tm.deviceSoftwareVersion+"\n"+tm.simSerialNumber+"\n"+tm.networkCountryIso+"\n"+tm.networkOperatorName+"\n"

        var phoneType = tm.phoneType
        when(phoneType){
            TelephonyManager.PHONE_TYPE_CDMA -> {data = data+"CDMA\n"}
            TelephonyManager.PHONE_TYPE_GSM -> {
                data += "GSM\n"
            }
            TelephonyManager.PHONE_TYPE_NONE -> {
                data += "NONE\n"
            }
        }

        var simStae = tm.simState
        when (simStae){
            TelephonyManager.SIM_STATE_ABSENT -> {
                data += "Sim Card is Absent\n"
            }
            TelephonyManager.SIM_STATE_READY -> {
                data += "Sim Card is Ready\n"
            }
        }
        
        tm.listen(object: PhoneStateListener(){
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)
                when(state){
                    TelephonyManager.CALL_STATE_RINGING ->{data = "Phone is Ringing"}
                    TelephonyManager.CALL_STATE_IDLE ->{data = "Phone is Idle"}
                }
                Toast.makeText(applicationContext,data,Toast.LENGTH_LONG).show()
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)


        edtInfoTelephony.setText(data)
    }
}
