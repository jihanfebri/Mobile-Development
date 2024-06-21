package com.skinective.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.skinective.R
import com.skinective.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AboutActivity", "onCreate called")
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up back button to finish the activity
        binding.backButton.setOnClickListener {
            Log.d("AboutActivity", "Back button clicked")
            finish()
        }

        // Set up team member data if necessary (example)
        setupTeamMembers()
    }

    private fun setupTeamMembers() {
        // Example: setting up team member data
        Log.d("AboutActivity", "Setting up team members")
        binding.ivTeamMemberPhoto1.setImageResource(R.drawable.team_anju)
        binding.tvTeamMemberName1.text = getString(R.string.name_team1)
        binding.tvTeamMemberRole1.text = getString(R.string.role_team1)

        binding.ivTeamMemberPhoto2.setImageResource(R.drawable.team_jey)
        binding.tvTeamMemberName2.text = getString(R.string.name_team2)
        binding.tvTeamMemberRole2.text = getString(R.string.role_team1)

        binding.ivTeamMemberPhoto3.setImageResource(R.drawable.team_edo)
        binding.tvTeamMemberName3.text = getString(R.string.name_team3)
        binding.tvTeamMemberRole3.text = getString(R.string.role_team2)

        binding.ivTeamMemberPhoto4.setImageResource(R.drawable.team_ammar)
        binding.tvTeamMemberName4.text = getString(R.string.name_team4)
        binding.tvTeamMemberRole4.text = getString(R.string.role_team2)

        binding.ivTeamMemberPhoto5.setImageResource(R.drawable.team_bima)
        binding.tvTeamMemberName5.text = getString(R.string.name_team5)
        binding.tvTeamMemberRole5.text = getString(R.string.role_team3)

        binding.ivTeamMemberPhoto6.setImageResource(R.drawable.team_hilal)
        binding.tvTeamMemberName6.text = getString(R.string.name_team6)
        binding.tvTeamMemberRole6.text = getString(R.string.role_team3)

        binding.ivTeamMemberPhoto7.setImageResource(R.drawable.team_desi)
        binding.tvTeamMemberName7.text = getString(R.string.name_team7)
        binding.tvTeamMemberRole7.text = getString(R.string.role_team3)
    }
}
