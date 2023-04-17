package com.vinodpatildev.eventmaster.presentation.ui.profile

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileImageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
         AlertDialog.Builder(requireContext())
            .setTitle("Confirm Updation")
            .setMessage("Do you really want to change your profile picture")
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Yes"){_,_ ->
                // Do the function
            }
            .create()
}