package com.ismin.opendataapp.sportsfragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.ismin.opendataapp.R
import com.ismin.opendataapp.sportsfragment.database.SportEntity
import com.ramotion.fluidslider.FluidSlider

class SportsFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private var initSportsList: ArrayList<SportEntity> = ArrayList()
    private var sportsList: ArrayList<SportEntity> = ArrayList()

    private val adapter = SportsAdapter(sportsList, ::selectSport)
    private val max = 100
    private val min = 0
    private val total = max - min

    private lateinit var slider: FluidSlider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sports, container, false)

        slider = view.findViewById(R.id.f_fluid_slider)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_sports)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val searchInput = view.findViewById<TextInputEditText>(R.id.search_text_input_edit)
        searchInput.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                searchInput.isSelected = false
                true
            } else {
                false
            }
        }
        searchInput.doAfterTextChanged {
            val searchInputText = searchInput.text.toString()
            for (sport in initSportsList) {
                if (sport.name.contains(searchInputText, ignoreCase = true) && !sportsList.contains(
                        sport
                    )
                ) {
                    sportsList.add(sport)
                } else if (!sport.name.contains(
                        searchInputText,
                        ignoreCase = true
                    ) && sportsList.contains(sport)
                ) {
                    sportsList.remove(sport)
                }
            }
            sportsList.sortBy { it.name }
            adapter.notifyDataSetChanged()
        }
        val searchButton = view.findViewById<Button>(R.id.f_sport_button_search)
        searchButton.setOnClickListener {
            val selectedSports = ArrayList<SportEntity>()
            initSportsList.forEach {
                if (it.isEnabled) {
                    selectedSports.add(it)
                }
            }
            listener?.onFragmentInteractionSports(selectedSports, slider.bubbleText!!.toInt())
        }
        slider.positionListener = { pos -> slider.bubbleText = "${min + (total * pos).toInt()}" }
        slider.position = 0.3f
        slider.startText = "$min km"
        slider.endText = "$max km"
        return view
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
        fun onFragmentInteractionSports(list: ArrayList<SportEntity>, distance: Int)
    }

    fun setSportsList(sportsList: ArrayList<SportEntity>) {
        this.initSportsList.clear()
        this.initSportsList.addAll(sportsList)
        this.sportsList.clear()
        this.sportsList.addAll(sportsList)
        adapter.notifyDataSetChanged()
    }

    private fun selectSport(id: Int, isChecked: Boolean) {
        initSportsList.forEach {
            if (it.id == id) {
                it.isEnabled = isChecked
                return
            }
        }
    }
}
