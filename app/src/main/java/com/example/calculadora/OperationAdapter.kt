package com.example.calculadora

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadora.model.Operacao
import com.example.calculadora.utils.formatNumber

class OperationAdapter(private val operations: List<Operacao>) :

    RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.tvId)
        val value1: TextView = itemView.findViewById(R.id.Value1)
        val value2: TextView = itemView.findViewById(R.id.Value2)
        val resultTextView: TextView = itemView.findViewById(R.id.tvResult)
        val timestampTextView: TextView = itemView.findViewById(R.id.tvTimestamp)
        val OperationIcon: ImageView = itemView.findViewById(R.id.OperationIcon)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_operation_history, parent, false)
        return OperationViewHolder(view)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = operations[position]
        holder.idTextView.text = "ID: ${operation.id}"

        if (operation.operation.equals("+")) {
            holder.OperationIcon.setImageResource(R.drawable.add_mini)
        } else if (operation.operation.equals("-")) {
            holder.OperationIcon.setImageResource(R.drawable.sub_mini)
        } else if (operation.operation.equals("*")) {
            holder.OperationIcon.setImageResource(R.drawable.multi_mini)
        } else if (operation.operation.equals("÷")) {
            holder.OperationIcon.setImageResource(R.drawable.div_mini)
        }

        holder.value1.text = "${formatNumber(operation.value1)}"
        holder.value2.text = "${formatNumber(operation.value2)}"
        holder.resultTextView.text = "${formatNumber(operation.result)}"
        holder.timestampTextView.text = "${operation.date}"
    }

    override fun getItemCount() = operations.size
}

