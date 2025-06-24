package com.sks225.snapeat.viewModel

import android.app.Activity
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sks225.snapeat.databinding.FragmentSignupBinding
import com.sks225.snapeat.model.User

class SignUpViewModel : ViewModel() {
    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pass: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val confirmPass: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val uri: MutableLiveData<Uri> by lazy { MutableLiveData<Uri>() }
    val uriAvail = MutableLiveData(false)

    private val emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private val mAuth = Firebase.auth
    private val database = Firebase.database
    private val storage = Firebase.storage


    fun signUp(binding: FragmentSignupBinding, onSuccess: () -> Unit) {
        if (email.value.isNullOrBlank()) {
            binding.etEmail.error = "Please Provide Email"
            binding.prg.visibility = View.GONE
            return
        } else if (pass.value.isNullOrBlank()) {
            binding.etPassword.error = "Please provide password"
            binding.prg.visibility = View.GONE
            return
        } else if (name.value.isNullOrBlank()) {
            binding.etFullName.error = "Please provide Name"
            binding.prg.visibility = View.GONE
            return

//        } else if (uriAvail.value == false) {
//            binding.prg.visibility = View.GONE
//            Snackbar.make(binding.root, "No profile image selected", Snackbar.LENGTH_LONG).show()
//            return
        } else if (!email.value!!.matches(emailPattern.toRegex())) {
            binding.etEmail.error = "Enter correct E-mail"
            return
        } else if (pass.value!!.length < 6) {
            binding.etPassword.error = "Password too short!"
            return
        } else if (!pass.value.equals(confirmPass.value)) {
            binding.etPassword2.error = "Both passwords do not match"
            return
        } else {
            mAuth.createUserWithEmailAndPassword(email.value!!, pass.value!!).addOnSuccessListener {
//                saveProfileImage(mAuth.currentUser!!.uid, activity, binding)
                saveData(mAuth.currentUser!!.uid, binding)
                onSuccess()
            }.addOnFailureListener {
                binding.prg.visibility = View.GONE
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }

    }

//    private fun saveProfileImage(
//        uid: String,
//        activity: Activity,
//        binding: FragmentSignupBinding
//    ) {
//        val ref = storage.getReference("photos/${uid}.${getFileExtension(uri.value!!, activity)}")
//        ref.putFile(uri.value!!).addOnSuccessListener {
//            ref.downloadUrl.addOnSuccessListener {
//                saveData(uid, activity, it, binding)
//            }.addOnFailureListener {
//                binding.prg.visibility = View.GONE
//                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
//            }
//        }.addOnFailureListener {
//            binding.prg.visibility = View.GONE
//            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
//        }
//    }

    private fun saveData(
        uid: String,
        binding: FragmentSignupBinding
    ) {
        val user = User(name.value, email.value, uid)
        database.getReference("users").child(uid).setValue(user).addOnFailureListener {
            binding.prg.visibility = View.GONE
            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getFileExtension(uri: Uri, activity: Activity): String? {
        val cr = activity.contentResolver
        val map = MimeTypeMap.getSingleton()
        return map.getExtensionFromMimeType(cr.getType(uri))
    }
}