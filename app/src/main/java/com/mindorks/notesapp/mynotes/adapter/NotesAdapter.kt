package com.mindorks.notesapp.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mindorks.notesapp.R
import com.mindorks.notesapp.data.local.db.Notes
import com.mindorks.notesapp.mynotes.clicklisteners.ItemClickListener

class NotesAdapter(private val list: List<Notes>,
                   private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notes = list[position]
        val title = notes.title
        val description = notes.description
        holder.textViewTitle.text = title
        holder.textViewDescription.text = description
        holder.checkBoxMarkStatus.isChecked = notes.isTaskCompleted
        Glide.with(holder.itemView).load(notes.imagePath).into(holder.imageView)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                itemClickListener.onClick(notes)
            }
        })

        holder.checkBoxMarkStatus.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                notes.isTaskCompleted = isChecked
                itemClickListener.onUpdate(notes)
            }
        })
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var checkBoxMarkStatus: CheckBox = itemView.findViewById(R.id.checkboxMarkStatus)
        var textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

    }
}
