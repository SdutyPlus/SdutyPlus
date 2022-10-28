package com.d108.sduty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.d108.sduty.databinding.ItemSpinnerBinding
import com.d108.sduty.model.dto.Task

class TaskSpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val values: List<Task>
) : ArrayAdapter<Task>(context, resId, values) {

    override fun getCount() = values.size


    override fun getItem(position: Int) = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.tvTask.text = "${model.title}"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.tvTask.text = "${model.title}"

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

}