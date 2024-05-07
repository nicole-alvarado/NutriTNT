package com.example.nutritnt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.databinding.FragmentListEncuestasBinding
import kotlin.random.Random

class ListEncuestasFragment : Fragment(), ListAdapterFragment.OnItemClickListener {
    private lateinit var binding: FragmentListEncuestasBinding
    private lateinit var listAdapter: ListAdapterFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentListEncuestasBinding.inflate(inflater, container, false)
        val view = binding.root

        listAdapter = ListAdapterFragment(getItemList(), this)
        binding.RecyclerViewEncuestas.layoutManager = LinearLayoutManager(requireContext())
        binding.RecyclerViewEncuestas.adapter = listAdapter
        listAdapter.setOnItemClickListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return view
    }

    private fun generateRandomId(): String {
        return "ID-${Random.nextInt(1000)}"
    }

    private fun getItemList(): List<ListElementFragment> {
        return mutableListOf(
            ListElementFragment(generateRandomId(), "15-04-2024", "Finalizada"),
            ListElementFragment(generateRandomId(), "16-04-2024", "Comenzada"),
            ListElementFragment(generateRandomId(), "17-04-2024", "Finalizada"),
            ListElementFragment(generateRandomId(), "18-04-2024", "Finalizada"),
            ListElementFragment(generateRandomId(), "19-04-2024", "Comenzada"),
            ListElementFragment(generateRandomId(), "12-04-2024", "Comenzada"),
            ListElementFragment(generateRandomId(), "21-04-2024", "Comenzada"),
            ListElementFragment(generateRandomId(), "01-04-2024", "Finalizada"),
            ListElementFragment(generateRandomId(), "20-04-2024", "Finalizada")
        )
    }

    override fun onItemClick(nombre: String, fecha: String, estado: String) {
        Log.d("ListEncuestasFragment", "Nombre de la encuesta seleccionada: $nombre")
        findNavController().navigate(ListEncuestasFragmentDirections.actionListEncuestasFragmentToDetailEncuestaFragment(nombre, fecha, estado))
        //val context = binding.root.context
        //val bundle = Bundle().apply {
        //    putString("ENCUESTA_ID", nombre)
        //    putString("FECHA", fecha)
        //    putString("ESTADO", estado)
        //}
//        val detailFragment = DetailEncuestaFragment()
  //      detailFragment.arguments = bundle
    //    activity?.supportFragmentManager?.beginTransaction()
      //      ?.replace(R.id.fragmentContainer, detailFragment)
        //    ?.addToBackStack(null)
          //  ?.commit()
    }

}