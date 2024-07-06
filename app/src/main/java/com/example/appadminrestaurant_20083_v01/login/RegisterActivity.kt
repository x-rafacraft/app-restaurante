package com.example.appadminrestaurant_20083_v01.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appadminrestaurant_20083_v01.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvYaTieneCuenta.setOnClickListener{
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegistrar.setOnClickListener{
            val name = binding.etNombres.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etRPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this , LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this , it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this , "Las contraseñas no coinciden" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "Rellene todos los campos!" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}