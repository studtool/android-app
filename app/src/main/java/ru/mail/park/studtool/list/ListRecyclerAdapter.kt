package ru.mail.park.studtool.list


import android.content.Intent
import ru.mail.park.studtool.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.mail.park.studtool.ItemDetailActivity
import ru.mail.park.studtool.NavigationActivity
import ru.mail.park.studtool.NavigationInterface
import ru.mail.park.studtool.document.DocumentInfo

class ListRecyclerAdapter(var documentList: Array<DocumentInfo>) : RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private lateinit  var item: DocumentInfo

    init {
        onClickListener = View.OnClickListener { v ->
            item = v.tag as DocumentInfo
            // call interface function to pass chosen document
//            NavigationActivity().openFile(item)
            val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_ITEM_TITLE, item.title)
                putExtra(ItemDetailFragment.ARG_ITEM_ID, item.documentId)
            }
            v.context.startActivity(intent)
//            (activity as? NavigationInterface)?.openFile(item)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListRecyclerAdapter.ViewHolder, position: Int) {
        val item = documentList[position]

        holder.desc.text = item.title
        holder.image.setImageResource(item.icon)

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }

    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        val desc: TextView
        val image: ImageView

        init {
            desc = mView.findViewById(R.id.item_desc)
            image = mView.findViewById(R.id.item_image)
        }
    }


}