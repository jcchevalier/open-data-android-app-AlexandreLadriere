package com.ismin.opendataapp.sportsfragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.ismin.opendataapp.R

class SportsFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private var initSportsList: ArrayList<Sport> = ArrayList()
    private var sportsList: ArrayList<Sport> = ArrayList()
    private val adapter = SportsAdapter(sportsList, ::selectSport)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sports, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_sports)
        val layoutManager = GridLayoutManager(context, 4)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val searchInput = view.findViewById<TextInputEditText>(R.id.search_text_input_edit)
        searchInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                searchInput.isSelected = false
                true
            } else {
                false
            }
        }
        searchInput.doAfterTextChanged {
            val searchInputText = searchInput.text.toString().toLowerCase()
            sportsList.clear()
            for (sport in initSportsList) {
                if (sport.name.length > searchInputText.length) {
                    if (sport.name.toLowerCase().subSequence(
                            0,
                            searchInputText.length
                        ).toString().equals(searchInputText)
                    ) {
                        sportsList.add(sport)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteractionSports(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteractionSports(uri: Uri)
    }

    fun setSportsList(sportsList: ArrayList<Sport>) {
        this.initSportsList.clear()
        this.initSportsList.addAll(sportsList)
        this.sportsList.clear()
        this.sportsList.addAll(sportsList)
        adapter.notifyDataSetChanged()
    }

    private fun selectSport(position: Int) {

    }

}
