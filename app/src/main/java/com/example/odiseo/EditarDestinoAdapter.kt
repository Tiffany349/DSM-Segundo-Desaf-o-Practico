package com.example.odiseo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EditarDestinoAdapter(
    private val destinos: List<Destino>,
    private val onEditarClick: (Destino) -> Unit,
    private val onEliminarClick: (Destino) -> Unit
) : RecyclerView.Adapter<EditarDestinoAdapter.EditarDestinoViewHolder>() {

    class EditarDestinoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val ivEditarDestino: ImageView =
            itemView.findViewById(R.id.ivEditarDestino)

        val tvEditarNombre: TextView =
            itemView.findViewById(R.id.tvEditarNombre)

        val tvEditarPais: TextView =
            itemView.findViewById(R.id.tvEditarPais)

        val tvEditarPrecio: TextView =
            itemView.findViewById(R.id.tvEditarPrecio)

        val btnEditarDestino: Button =
            itemView.findViewById(R.id.btnEditarDestino)

        val btnEliminarDestino: Button =
            itemView.findViewById(R.id.btnEliminarDestino)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditarDestinoViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_editar_destino,
            parent,
            false
        )

        return EditarDestinoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EditarDestinoViewHolder,
        position: Int
    ) {

        val destino = destinos[position]

        holder.tvEditarNombre.text = destino.nombre
        holder.tvEditarPais.text = destino.pais

        holder.tvEditarPrecio.text =
            holder.itemView.context.getString(
                R.string.precio_formato,
                destino.precio
            )

        Glide.with(holder.itemView.context)
            .load(destino.imagenUrl)
            .placeholder(
                android.R.drawable.ic_menu_gallery
            )
            .error(
                android.R.drawable.ic_menu_report_image
            )
            .centerCrop()
            .into(holder.ivEditarDestino)

        holder.btnEditarDestino.setOnClickListener {
            onEditarClick(destino)
        }

        holder.btnEliminarDestino.setOnClickListener {
            onEliminarClick(destino)
        }
    }

    override fun getItemCount(): Int {
        return destinos.size
    }
}