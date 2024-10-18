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
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.databinding.FragmentProfileBinding
import com.sks225.snapeat.viewModel.MainFragmentViewModel


class ProfileFragment(private var viewModel: MainFragmentViewModel) : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val recipientEmail = "sks225dv@gmail.com"
    private var photoUrl: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.user.observe(viewLifecycleOwner) {
            binding.user = it
            photoUrl = it?.photoUrl
            Glide.with(this).load(photoUrl).into(binding.profilePic)
        }

        /*binding.profilePic.setOnClickListener {
            val dialogFragment = ProfilePicDialogFragment().apply {
                photoUrl?.let { it1 -> setProfilePicUrl(it1) } // Assuming you set the URL as a tag or retrieve it from the ViewModel
                setOnChangePicClickListener {
                    Toast.makeText(context, "Change Pic Clicked", Toast.LENGTH_SHORT).show()
                    // Code to change the profile picture
                }
                setOnRemovePicClickListener {
                    Toast.makeText(context, "Remove Pic Clicked", Toast.LENGTH_SHORT).show()
                    // Code to remove the profile picture
                }
            }
            dialogFragment.show(parentFragmentManager, "ProfilePicDialog")
        }*/

        binding.account.setOnClickListener {
            //intent to edit account details activity
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

        return binding.root
    }

    private fun showPrivacyPolicyDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Privacy Policy")
            .setMessage(R.string.privacyPolicy)
            .setNeutralButton("OK") { dialog, _ ->
                dialog.dismiss()
            }


        val dialog = dialogBuilder.create()
        dialog.show()
    }
}