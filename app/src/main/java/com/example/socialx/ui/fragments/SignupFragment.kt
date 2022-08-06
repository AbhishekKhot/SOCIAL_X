package com.example.socialx.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.socialx.R
import com.example.socialx.databinding.FragmentSignupBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    private var mCallbackManager: CallbackManager? = null
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCallbackManager = CallbackManager.Factory.create()

        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_forgotPasswordFragment)
        }

        binding.ivGoogle.setOnClickListener {
            val signInIntent = gsc.signInIntent
            startActivityForResult(signInIntent, 1000)
        }

        binding.ivFacebook.setOnClickListener {
            facebookSignIn()
        }

        binding.btnLogin.setOnClickListener {
            emailSignIn(it)
        }
    }

    private fun emailSignIn(v: View) {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Snackbar.make(v, "Successfully Login", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_mainFragment_to_newsActivity)
                } else {
                    Snackbar.make(v, it.exception?.message.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                    Toast.makeText(requireActivity(),
                        it.exception?.message.toString(),
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Snackbar.make(v, "Empty fields are not allowed", Snackbar.LENGTH_SHORT).show()
        }

    }


    private fun facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(requireActivity(),
            Arrays.asList("email", "public_profile"))
        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {

                }
            })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()
            ) { p0 ->
                if (p0.isSuccessful) {

                    findNavController().navigate(R.id.action_mainFragment_to_newsActivity)
                } else {
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1000) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                findNavController().navigate(R.id.action_mainFragment_to_newsActivity)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(),
                    "Something went wrong",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}