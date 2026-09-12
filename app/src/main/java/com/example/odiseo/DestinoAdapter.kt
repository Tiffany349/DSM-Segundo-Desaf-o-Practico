package com.example.odiseo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DestinoAdapter(
    private val destinos: List<Destino>
) : RecyclerView.Adapter<DestinoAdapter.DestinoViewHolder>() {

    class DestinoViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val ivDestino: ImageView =
            itemView.findViewById(R.id.ivDestino)

        val tvNombreDestino: TextView =
            itemView.findViewById(R.id.tvNombreDestino)

        val tvPaisDestino: TextView =
            itemView.findViewById(R.id.tvPaisDestino)

        val tvPrecioDestino: TextView =
            itemView.findViewById(R.id.tvPrecioDestino)

        val tvDescripcionDestino: TextView =
            itemView.findViewById(R.id.tvDescripcionDestino)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DestinoViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_destino,
            parent,
            false
        )

        return DestinoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DestinoViewHolder,
        position: Int
    ) {

        val destino = destinos[position]

        holder.tvNombreDestino.text =
            destino.nombre

        holder.tvPaisDestino.text =
            destino.pais

        holder.tvPrecioDestino.text =
            holder.itemView.context.getString(
                R.string.precio_formato,
                destino.precio
            )

        holder.tvDescripcionDestino.text =
            destino.descripcion

        Glide.with(holder.itemView.context)
            .load(destino.imagenUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .centerCrop()
            .into(holder.ivDestino)
    }

    override fun getItemCount(): Int {
        return destinos.size
    }
}