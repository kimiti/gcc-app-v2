package com.example.gccapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gccapp.models.Dvd
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class DvdFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var recyclerView: RecyclerView

    private val FireStoreDB = FirebaseFirestore.getInstance().collection("dvds")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dvd, container, false)

        recyclerView = mView.findViewById<RecyclerView?>(R.id.dvds_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val options = FirestoreRecyclerOptions
            .Builder<Dvd>()
            .setQuery(FireStoreDB, Dvd::class.java)
            .build()


        val adapter: FirestoreRecyclerAdapter<Dvd, ViewHolder> =
            object : FirestoreRecyclerAdapter<Dvd, ViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.dvds_row_layout, parent, false)

                    return ViewHolder(view)
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Dvd) {
                    holder.videoTitle.text = model.title
                    holder.subtitle.text= model.subtitle
                    holder.imageUrl.load(model.imageUrl) {
                        crossfade(600)
                    }
                }


            }

        adapter.startListening()
        recyclerView.adapter = adapter

        return mView
    }

}