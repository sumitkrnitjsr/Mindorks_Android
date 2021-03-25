package com.mindorks.notesapp.mynotes.clicklisteners

import com.mindorks.notesapp.data.local.db.Notes

interface ItemClickListener {

    fun onClick(notes: Notes)

    fun onUpdate(notes: Notes)

}
