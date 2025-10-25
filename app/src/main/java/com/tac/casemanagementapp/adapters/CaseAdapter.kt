package com.tac.casemanagementapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.databinding.CardCaseBinding
import com.tac.casemanagementapp.models.CaseModel

// Listener interface for click events on a case
interface CaseListener {
    fun onCaseClick(case: CaseModel)
}

// Adapter bridges data (list of cases) to RecyclerView items
class CaseAdapter(
    private var cases: List<CaseModel>,
    private val listener: CaseListener
) : RecyclerView.Adapter<CaseAdapter.MainHolder>() {

    // Inflates the layout for each card (list item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    // Binds each case to a card
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val case = cases[holder.adapterPosition]
        holder.bind(case, listener)
    }

    override fun getItemCount(): Int = cases.size

    // Inner class: holds reference to layout elements for performance
    class MainHolder(private val binding: CardCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(case: CaseModel, listener: CaseListener) {
            // Match IDs from your XML: caseName, caseDetails, caseImage
            binding.caseName.text = case.title
            binding.caseDetails.text = case.description

            // Show image if available
            if (case.image != android.net.Uri.EMPTY)
                Picasso.get().load(case.image).into(binding.caseImage)

            // Handle click
            binding.root.setOnClickListener { listener.onCaseClick(case) }
        }
    }
}
