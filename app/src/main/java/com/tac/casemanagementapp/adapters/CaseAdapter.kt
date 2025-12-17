package com.tac.casemanagementapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.databinding.CardCaseBinding
import com.tac.casemanagementapp.models.CaseModel

// This is a  Listener interface for handling clicks from the RecyclerView items
// onCaseClick for users to tap the card (edit/view)
// onCaseDelete for users to delete button
interface CaseListener {
    fun onCaseClick(case: CaseModel)
    fun onCaseDelete(case: CaseModel)
}

// Adapter connects a list of CaseModel objects to the RecyclerView UI
class CaseAdapter(
    private var cases: List<CaseModel>,      // Data shown in the list
    private val listener: CaseListener       // Handles user actions (click/delete)
) : RecyclerView.Adapter<CaseAdapter.MainHolder>() {

    // Creates a new ViewHolder (card layout) when RecyclerView needs one
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainHolder(binding)
    }

    // Fills the card with data for the current position
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        // Use holder.adapterPosition to get the latest correct position
        val case = cases[holder.adapterPosition]
        holder.bind(case, listener)
    }

    // Tells RecyclerView how many items it should display
    override fun getItemCount(): Int = cases.size

    // ViewHolder holds references to the views inside one card_case.xml layout
    class MainHolder(private val binding: CardCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binds one CaseModel object into the card UI
        fun bind(case: CaseModel, listener: CaseListener) {
            // Show the main text fields
            binding.caseName.text = case.title
            binding.caseDetails.text = case.description
            binding.caseGender.text = "Gender: ${case.gender}"

            // Load case image if available
            if (case.image.isNotEmpty()) {
                // Picasso loads image from a URL/Uri into the ImageView
                Picasso.get().load(case.image).into(binding.caseImage)
            } else {

            }

            // Shows a static Google map preview if the case has a saved location
            val hasLocation = case.latitude != 0.0 && case.longitude != 0.0
            if (hasLocation) {
                binding.mapPreview.visibility = View.VISIBLE

                // Build the Static Maps URL using the saved lat/lng
                val url = getStaticMapUrl(case.latitude, case.longitude)

                // Load the map image into the preview ImageView
                Picasso.get()
                    .load(url)
                    .fit()          // Fit the image into the ImageView size
                    .centerCrop()   // Crop from center to fill nicely
                    .into(binding.mapPreview)
            } else {
                // Hide the map preview if no location exists
                binding.mapPreview.visibility = View.GONE
            }

            // Card click = edit/view the case
            binding.root.setOnClickListener { listener.onCaseClick(case) }

            // Delete button click = delete the case
            binding.btnDelete.setOnClickListener { listener.onCaseDelete(case) }
        }

        // Builds a Google Static Maps URL using latitude and longitude
        private fun getStaticMapUrl(lat: Double, lng: Double): String {
            // TODO: replace with your real API key (restrict it to Static Maps API)
            val apiKey = "AIzaSyBkfcAJz0ELEAcND5jstBDT2duDPlvmkFk"

            // Returns a static map image URL (the marker shows the case location)
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
