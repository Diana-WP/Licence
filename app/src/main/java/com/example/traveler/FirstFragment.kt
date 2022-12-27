package com.example.traveler


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.traveler.databinding.FragmentFirstBinding

private class FirstFragment : Fragment() {
    private lateinit var binding: FragmentFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.nicknameText.text = binding.nicknameText.text.toString()

         fun addNickname(view: View) {
            binding.apply {
                binding.nicknameText.text = binding.nicknameText.text
                binding.nicknameText.visibility = View.GONE
                binding.doneButton.visibility = View.GONE
                binding.nicknameText.visibility = View.VISIBLE
            }

        }
         fun updateNickname (view: View) {
                binding.apply {
                    binding.doneButton.visibility = View.GONE
                    binding.nameEdit.text = binding.nameEdit.text
                binding.nameEdit.visibility = View.VISIBLE
    view.visibility = View.GONE
}

        }

        binding.doneButton.setOnClickListener {
            addNickname(it)
        }
        binding.doneButton.setOnClickListener{
            updateNickname(it)
        }
    }


}