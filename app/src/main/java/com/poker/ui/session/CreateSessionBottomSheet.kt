package com.poker.ui.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poker.databinding.DialogCreateSessionBinding

class CreateSessionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogCreateSessionBinding? = null
    private val binding get() = _binding!!

    private var onSessionCreated: ((String) -> Unit)? = null

    fun setOnSessionCreatedListener(listener: (String) -> Unit) {
        onSessionCreated = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCreateSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        // Set up create button click
        binding.createButton.setOnClickListener {
            val sessionName = binding.sessionNameInput.text.toString().trim()

            if (sessionName.isEmpty()) {
                binding.nameInputLayout.error = "Please enter a session name"
                return@setOnClickListener
            }

            onSessionCreated?.invoke(sessionName)
            dismiss()
        }

        // Set up cancel button click
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        // Auto-focus the input field and show keyboard
        binding.sessionNameInput.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CreateSessionBottomSheet"

        fun newInstance(): CreateSessionBottomSheet {
            return CreateSessionBottomSheet()
        }
    }
}
