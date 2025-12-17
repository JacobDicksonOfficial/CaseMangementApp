package com.tac.casemanagementapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.databinding.CardCaseBinding
import com.tac.casemanagementapp.models.CaseModel

// Listener interface for handling both edit and delete events
interface CaseListener {
    fun onCaseClick(case: CaseModel)
    fun onCaseDelete(case: CaseModel)
}

// Adapter that bridges CaseModel data to RecyclerView cards
class CaseAdapter(
    private var cases: List<CaseModel>,
    private val listener: CaseListener
) : RecyclerView.Adapter<CaseAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val case = cases[holder.adapterPosition]
        holder.bind(case, listener)
    }

    override fun getItemCount(): Int = cases.size

    class MainHolder(private val binding: CardCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(case: CaseModel, listener: CaseListener) {
            binding.caseName.text = case.title
            binding.caseDetails.text = case.description
            binding.caseGender.text = "Gender: ${case.gender}"

            // Load case image if available
            if (case.image.isNotEmpty()) {
                Picasso.get().load(case.image).into(binding.caseImage)
            } else {
                // Optional: reset to default launcher if no image
                // binding.caseImage.setImageResource(R.mipmap.ic_launcher)
            }

            // NEW: map preview
            val hasLocation = case.latitude != 0.0 && case.longitude != 0.0
            if (hasLocation) {
                binding.mapPreview.visibility = View.VISIBLE

                val url = getStaticMapUrl(case.latitude, case.longitude)
                Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(binding.mapPreview)
            } else {
                binding.mapPreview.visibility = View.GONE
            }

            // Card click -> edit
            binding.root.setOnClickListener { listener.onCaseClick(case) }

            // Delete click
            binding.btnDelete.setOnClickListener { listener.onCaseDelete(case) }
        }

        private fun getStaticMapUrl(lat: Double, lng: Double): String {
            // TODO: replace with your real API key (restrict it to Static Maps API)
            val apiKey = "AIzaSyBkfcAJz0ELEAcND5jstBDT2duDPlvmkFk"

            // size uses pixels. Using a wider size gives a nicer preview image.
            return "https://maps.googleapis.com/maps/api/staticmap" +
                    "?center=$lat,$lng" +
                    "&zoom=15" +
                    "&size=600x300" +
                    "&maptype=roadmap" +
                    "&markers=color:red%7C$lat,$lng" +
                    "&key=$apiKey"
        }
    }
}
