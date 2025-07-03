package com.sks225.snapeat

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.databinding.FragmentProfileBinding
import com.sks225.snapeat.viewModel.MainFragmentViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MainFragmentViewModel
    private val recipientEmail = "sks225dv@gmail.com"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]

        setupUI()
        observeData()

        return binding.root
    }

    private fun setupUI() {
        binding.account.setOnClickListener {
            // Navigate or open account details activity
        }

        binding.notification.setOnClickListener {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                putExtra(Settings.EXTRA_CHANNEL_ID, "CHANNEL_ID")
                startActivity(this)
            }
        }

        binding.contactUs.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$recipientEmail")
                putExtra(Intent.EXTRA_SUBJECT, "Query regarding Discure")
                startActivity(this)
            }
        }

        binding.privacyPolicy.setOnClickListener {
            showPrivacyPolicyDialog(requireContext())
        }

        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            requireActivity().finishAffinity()
        }
    }

    private fun observeData() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.user = user
            // If you want to load profile picture:
            // user?.photoUrl?.let { Glide.with(this).load(it).into(binding.profilePic) }
        }
    }

    private fun showPrivacyPolicyDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Privacy Policy")
            .setMessage(R.string.privacyPolicy)
            .setNeutralButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
