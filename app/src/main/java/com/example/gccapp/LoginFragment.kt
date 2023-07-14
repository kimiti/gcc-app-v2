package com.example.gccapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var auth: FirebaseAuth
    private val TAG = "AuthLoginFragment"

    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout

    fun shouldInterceptBackPress() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(shouldInterceptBackPress()){
//                    Toast.makeText(requireContext(), "Back press intercepted in:${this@DvdFragment}", Toast.LENGTH_SHORT).show()
                    // in here you can do logic when backPress is clicked
                    getActivity()?.moveTaskToBack(true);
                    getActivity()?.finish();
                }else{
                    isEnabled = false
                    activity?.onBackPressedDispatcher!!.onBackPressed()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //redirect to dashboard
            findNavController().navigate(R.id.action_loginFragment_to_dvdFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        username = mView.findViewById(R.id.usernameTextField)
        password = mView.findViewById(R.id.passwordTextField)

        val loginButton = mView.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val usernameText = username.editText?.text.toString()
            val passwordText = password.editText?.text.toString()
            Log.d(TAG, "$usernameText ++ $passwordText")
            login(usernameText, passwordText)
        }

        return mView
    }

    private fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                // val user = auth.currentUser
                findNavController().navigate(R.id.action_loginFragment_to_dvdFragment)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    context, task.exception?.message.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}