package rs.raf.mealPlanner.presentation.view.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.data.models.utils.Filter
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.databinding.FilterDropdownDialogBinding
import rs.raf.mealPlanner.databinding.PlanFilterDialogBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.recycler.adapter.PlanDialogFilterAdapter
import rs.raf.mealPlanner.presentation.view.recycler.adapter.PlanDialogMealAdapter
import rs.raf.mealPlanner.presentation.view.states.FiltersState
import rs.raf.mealPlanner.presentation.view.states.MealsState
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel

class DialogPlanFilter: DialogFragment(R.layout.plan_filter_dialog) {
    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: PlanFilterDialogBinding? = null

    private val binding get() = _binding!!

    private lateinit var innerDialog: Dialog
    private lateinit var dropdownAdapter: PlanDialogFilterAdapter
    private lateinit var dialogBinding: FilterDropdownDialogBinding

    private lateinit var adapter: PlanDialogMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlanFilterDialogBinding.inflate(inflater, container, false)
        this.isCancelable=false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initObservers()
        initListeners()
        initStaticDropdown()
        initDropdownDialog()
        initRecycler()
    }

    private fun initObservers() {
        mainViewModel.filtersState.observe(viewLifecycleOwner, Observer {
            renderDialogState(it)
        })

        mainViewModel.planDialogSelectedFilter.observe(viewLifecycleOwner, Observer{
            setDynamicSpinnerValue(it.name)
        })

        mainViewModel.mealsState.observe(viewLifecycleOwner,Observer{
            renderState(it)
        })
    }

    private fun initListeners(){
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if(selectedItem=="Area"){
                    mainViewModel.fetchAreaNames()
                }
                else if(selectedItem=="Category"){
                    mainViewModel.fetchCategoryNames()
                }
                else if(selectedItem=="Ingredient"){
                    mainViewModel.fetchIngredientNames()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?){

            }
        }

        binding.dynamicSpinner.setOnClickListener{
            innerDialog.show()
        }

        binding.dynamicSpinner.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var selection:String = s.toString()
                var spinnerSelectedItem=binding.spinner.selectedItem.toString()

                if(spinnerSelectedItem=="Category"){
                    mainViewModel.fetchMealsByCategory(selection)
                }
                else if(spinnerSelectedItem=="Area"){
                    mainViewModel.fetchMealsByArea(selection)
                }
                else{
                    mainViewModel.fetchMealsByIngredient(selection)
                }
            }
        })

    }

    private fun initRecycler() {
        binding.listRv.layoutManager = LinearLayoutManager(this.context)
        adapter = PlanDialogMealAdapter(this)
        binding.listRv.adapter = adapter
    }

    private fun initStaticDropdown(){ //ovo je dobro
        val dropdown=binding.spinner
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this.context!!, R.array.filterDD,
                android.R.layout.simple_dropdown_item_1line
            )
        dropdown.adapter=staticAdapter
    }

    private fun initDropdownDialog(){ //ovo je dobro
        innerDialog=Dialog(this.requireContext())
        innerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        innerDialog.setCancelable(true)

        dialogBinding = FilterDropdownDialogBinding.inflate(LayoutInflater.from(this.context))
        innerDialog.setContentView(dialogBinding.getRoot())

        val width = (this.getResources().getDisplayMetrics().widthPixels * 0.9)
        innerDialog.window!!.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        innerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.search.clearFocus()
        dialogBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dropdownAdapter.filter.filter(newText)
                return true
            }
        })

        dialogBinding.listRv.layoutManager= LinearLayoutManager(this.context)
        dropdownAdapter = PlanDialogFilterAdapter(this)
        dialogBinding.listRv.adapter=dropdownAdapter
    }

    private fun renderDialogState(state: FiltersState) { //ovo je dobro
        when (state) {
            is FiltersState.Success -> {
                showDialogLoadingState(false)
                dropdownAdapter.setData(state.filters)
                val dropdown=binding.dynamicSpinner
                dropdown.setText(state.filters.get(0).name)
            }
            is FiltersState.Error -> {
                showDialogLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is FiltersState.DataFetched -> {
                showDialogLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is FiltersState.Loading -> {
                showDialogLoadingState(true)
            }
        }
    }

    private fun showDialogLoadingState(loading: Boolean) { //ovo je dobro
        dialogBinding.search.isVisible = !loading
        dialogBinding.listRv.isVisible = !loading
        dialogBinding.loadingPb.isVisible = loading
    }

    private fun renderState(state: MealsState) {
        when (state) {
            is MealsState.Success -> {
                showLoadingState(false)
                adapter.submitList(state.meals)
            }
            is MealsState.Error -> {
                showLoadingState(false)
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            is MealsState.DataFetched -> {
                showLoadingState(false)
                Toast.makeText(context, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is MealsState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun showLoadingState(loading: Boolean) {
        //binding.inputEt.isVisible = !loading
        binding.listRv.isVisible = !loading
        binding.loadingPb.isVisible = loading
    }

    fun setDynamicSpinnerValue(value: String) {
        val dropdown=binding.dynamicSpinner
        dropdown.setText(value)
    }

    fun dismissDialog(){
        dialogBinding = FilterDropdownDialogBinding.inflate(LayoutInflater.from(this.context))
        dialogBinding.search.setQuery("",true)
        dialogBinding.search.clearFocus()
        innerDialog.dismiss()
    }

    fun dismissDialogAndSaveMeal(meal:Meal){
        mainViewModel.setPlanDialogSelectedMeal(meal)
//        val parent = parentFragment as PlanFragment
//        parent.test()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun selectDialogFilterInMainViewModel(filter: Filter){
        mainViewModel.setplanDialogSelectedFilter(filter)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val parentFragment=parentFragment
        if(parentFragment is DialogInterface.OnDismissListener){
            parentFragment.onDismiss(dialog)
        }
    }

}