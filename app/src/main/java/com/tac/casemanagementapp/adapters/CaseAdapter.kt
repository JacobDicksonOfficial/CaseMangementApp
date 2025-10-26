package com.tac.casemanagementapp.adapters

import android.view.LayoutInflater // for inflating layout
import android.view.ViewGroup      // for ViewGroup parameter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tac.casemanagementapp.databinding.CardCaseBinding
import com.tac.casemanagementapp.models.CaseModel

// Listener interface for handling both edit and delete events
interface CaseListener {
    fun onCaseClick(case: CaseModel)   // when user taps a case card
    fun onCaseDelete(case: CaseModel)  // when user taps delete icon
}

// Adapter that bridges CaseModel data to RecyclerView cards
class CaseAdapter(
    private var cases: List<CaseModel>,  // the list of cases
    private val listener: CaseListener   // listener for user interactions
) : RecyclerView.Adapter<CaseAdapter.MainHolder>() {

    // Inflate each card layout (card_case.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainHolder(binding)
    }

    // Bind each case to the ViewHolder
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val case = cases[holder.adapterPosition] // current item
        holder.bind(case, listener)              // bind data + clicks
    }

    // Number of cases in the list
    override fun getItemCount(): Int = cases.size

    // ViewHolder caches views for smoother scrolling
    class MainHolder(private val binding: CardCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data and handle button clicks
        fun bind(case: CaseModel, listener: CaseListener) {
            // Set case name and details
            binding.caseName.text = case.title
            binding.caseDetails.text = case.description

            // Load image if available
            if (case.image != android.net.Uri.EMPTY) {
                Picasso.get().load(case.image).into(binding.caseImage)
            }

            // Click on the entire card opens/edit the case
            binding.root.setOnClickListener { listener.onCaseClick(case) }

            // 🗑️ Click on delete icon triggers delete callback
            // (Make sure card_case.xml has ImageButton with id btnDelete)
            binding.btnDelete.setOnClickListener { listener.onCaseDelete(case) }
        }
    }
}
