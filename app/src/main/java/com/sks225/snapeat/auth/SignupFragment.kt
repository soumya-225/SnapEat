package com.sks225.snapeat.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.databinding.FragmentSignupBinding
import com.sks225.snapeat.viewModel.SignUpViewModel


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.etEmail.addTextChangedListener { viewModel.email.value = it.toString() }
        binding.etPass.doAfterTextChanged { viewModel.pass.value = it.toString() }
        binding.etConfirmPass.doAfterTextChanged { viewModel.confirmPass.value = it.toString() }
        binding.etName.doAfterTextChanged { viewModel.name.value = it.toString() }

        binding.tvChoosePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            launcher.launch(intent)
        }

        binding.btnSignUp.setOnClickListener {
            binding.prg.visibility = View.VISIBLE
            viewModel.signUp(binding, requireActivity())
        }

        binding.back.setOnClickListener {
            //send to login activity
        }

        return binding.root
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                binding.done.visibility = View.VISIBLE
                viewModel.uriAvail.value = true
                viewModel.uri.value = it.data!!.data
            }
        }
}