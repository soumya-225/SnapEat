package com.sks225.snapeat.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.R
import com.sks225.snapeat.databinding.FragmentSignupBinding
import com.sks225.snapeat.viewModel.SignUpViewModel


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: SignUpViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        navController = findNavController()

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.etEmail.addTextChangedListener { viewModel.email.value = it.toString() }
        binding.etPassword.doAfterTextChanged { viewModel.pass.value = it.toString() }
        binding.etPassword2.doAfterTextChanged { viewModel.confirmPass.value = it.toString() }
        binding.etFullName.doAfterTextChanged { viewModel.name.value = it.toString() }

//        binding.tvChoosePic.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            launcher.launch(intent)
//        }

        binding.btnSignup.setOnClickListener {
            binding.prg.visibility = View.VISIBLE
            viewModel.signUp(binding) {
                Log.d("Test", "Reach")
                navController.navigate(R.id.action_signupFragment_to_ageFragment)
            }
        }

//        binding.back.setOnClickListener {
//            navController.navigateUp()
//        }

        return binding.root
    }

//    private val launcher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
//                binding.done.visibility = View.VISIBLE
//                viewModel.uriAvail.value = true
//                viewModel.uri.value = it.data!!.data
//            }
//        }
}