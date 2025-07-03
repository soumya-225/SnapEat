package com.sks225.snapeat.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentLoginBinding
import com.sks225.snapeat.viewModel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        checkUser()

        binding.etEmail.doAfterTextChanged { viewModel.email.value = it.toString() }
        binding.etPassword.doAfterTextChanged { viewModel.pass.value = it.toString() }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.login()
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { (success, message) ->
            binding.progressBar.visibility = View.GONE
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            } else {
                Snackbar.make(binding.root, message ?: "Login failed", Snackbar.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun checkUser() {
        if (viewModel.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }
}
