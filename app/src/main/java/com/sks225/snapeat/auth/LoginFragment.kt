package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentLoginBinding
import com.sks225.snapeat.viewModel.LoginViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding = FragmentLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth

        checkUser()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        binding.emailInput.doAfterTextChanged { viewModel.email.value = it.toString() }
        binding.passwordInput.doAfterTextChanged { viewModel.pass.value = it.toString() }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.login(binding, requireActivity())
        }
        binding.redirectSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        return binding.root
    }

    /*private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Sign in successful!")
            startActivity(Intent(this,MainActivity::class.java))
        } else {
            Toast.makeText(
                this,
                "There was an error signing in",
                Toast.LENGTH_SHORT).show()

            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }*/

    private fun checkUser() {
        if (auth.currentUser != null) {
            //go to main activity
        }
    }

    /*private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                viewModel.handleResult(task, binding, this, button)
            }
        }*/


}