package a3cv6.escom.ipn.mx.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val PREFS_NAME = "a3cv6.escom.ipn.mx.login.Preferences"
    private lateinit var user : EditText
    private lateinit var pass : EditText
    private lateinit var checkbox : CheckBox

    private lateinit var session : User
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user = findViewById<EditText>(R.id.user)
        pass = findViewById<EditText>(R.id.password)
        checkbox = findViewById<CheckBox>(R.id.checkBox)

        session = User.instance
        preferences = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)

        session.name = preferences.getString("name",null)
        session.token= preferences.getString("token",null)
        session.loggedIn = preferences.getBoolean("loggedIn",false)

        if(session.loggedIn)
            entrar(null)
    }

    fun entrar(view:View?){

        val temp = user.text.toString()+pass.text.toString()

        if(!session.loggedIn){

            if(!session.token.equals( temp.sha512())){
                Toast.makeText(this,"Credenciales Incorrectas",Toast.LENGTH_SHORT).show()

            } else if(checkbox.isChecked ) {
                val prefEditor = preferences.edit()
                session.loggedIn = true
                prefEditor.putString("name", session.name)
                prefEditor.putString("token", session.token)
                prefEditor.putBoolean("loggedIn", session.loggedIn)
                prefEditor.apply()

                Toast.makeText(this, "Writing Preferences data", Toast.LENGTH_SHORT).show()

            } else {
                session.loggedIn = true
            }
        }

        if(session.loggedIn){
            var intent = Intent(this,Perfil::class.java)
            startActivityForResult(intent,300)
        }
    }

    fun register(view:View?){

        val intentR = Intent(this,Registro::class.java)
        startActivityForResult(intentR,200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==200){

            if(resultCode==Activity.RESULT_OK){
                Toast.makeText(this,"User Created",Toast.LENGTH_SHORT).show()

            } else if(resultCode==Activity.RESULT_CANCELED){
                Toast.makeText(this,"User not Created",Toast.LENGTH_SHORT).show()

            } else{
                Toast.makeText(this,"RequestCode 200. ResultCode: " + resultCode.toString(),Toast.LENGTH_SHORT).show()
            }
        }
        else if(requestCode==300){

            if(resultCode==Activity.RESULT_CANCELED) {
                val prefEditor = preferences.edit()
                prefEditor.putString("name","")
                prefEditor.putString("token","")
                prefEditor.putBoolean("loggedIn",false)
                prefEditor.apply()

                Toast.makeText(this,"Deleting Preferences",Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this,"RequestCode 300. ResultCode: " + resultCode.toString(),Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this,"Unexpected Behave, RequestCode: " + requestCode.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}