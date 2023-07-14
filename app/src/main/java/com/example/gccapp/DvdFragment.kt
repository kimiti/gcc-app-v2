package com.example.gccapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gccapp.models.Dvd
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DvdFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var recyclerView: RecyclerView

    private val FireStoreDB = FirebaseFirestore.getInstance().collection("dvds")

    fun shouldInterceptBackPress() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(shouldInterceptBackPress()){
//                    Toast.makeText(requireContext(), "Back press intercepted in:${this@DvdFragment}", Toast.LENGTH_SHORT).show()
                    // in here you can do logic when backPress is clicked
                    getActivity()?.moveTaskToBack(true);
                    getActivity()?.finish();
                }else{
                    isEnabled = false
                    activity?.onBackPressedDispatcher!!.onBackPressed()
                }
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dvd, container, false)

        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()


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
                    holder.subtitle.text = model.subtitle
                    holder.imageUrl.load(model.imageUrl) {
                        crossfade(600)
                        error(R.drawable.ic_error_placeholder)
                    }

                    holder.setOnClicklistener(object : ViewHolder.Clicklistener {
                        override fun onItemClick(view: View?, position: Int) {
                            findNavController().navigate(
                                DvdFragmentDirections.actionDvdFragmentToMediaFragment(model.videoUrl.toString())
                            )
                        }
                    })

                }


            }

        adapter.startListening()
        recyclerView.adapter = adapter


        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.logout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        // clearCompletedTasks()
                        Firebase.auth.signOut()
                        findNavController().navigate(R.id.action_dvdFragment_to_loginFragment)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        return mView
    }

}