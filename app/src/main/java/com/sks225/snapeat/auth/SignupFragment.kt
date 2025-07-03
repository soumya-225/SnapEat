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
import com.sks225.snapeat.databinding.FragmentSignupBinding
import com.sks225.snapeat.viewModel.SignUpViewModel

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.etEmail.doAfterTextChanged { viewModel.email.value = it.toString() }
        binding.etPassword.doAfterTextChanged { viewModel.pass.value = it.toString() }
        binding.etPassword2.doAfterTextChanged { viewModel.confirmPass.value = it.toString() }
        binding.etFullName.doAfterTextChanged { viewModel.name.value = it.toString() }

        binding.btnSignup.setOnClickListener {
            binding.prg.visibility = View.VISIBLE
            viewModel.signUp()
        }
    }

    private fun setupObservers() {
        viewModel.signUpStatus.observe(viewLifecycleOwner) { (success, message) ->
            binding.prg.visibility = View.GONE
            if (success) {
                findNavController().navigate(R.id.action_signupFragment_to_ageFragment)
            } else {
                Snackbar.make(binding.root, message ?: "Signup failed", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
